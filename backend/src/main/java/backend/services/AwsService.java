package backend.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import backend.configs.S3ConfigProperties;
import backend.configs.UploadResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.ByteBuffer;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
public class AwsService {
    private final S3AsyncClient s3AsyncClient;
    private final S3ConfigProperties s3ConfigProperties;

    private Mono<byte[]> getBytes(FilePart filePart) {
        return filePart.content().collectList().flatMap(
                dataBuffers -> {
                    int totalSize = dataBuffers.stream().mapToInt(DataBuffer::readableByteCount).sum();
                    byte[] bytes = new byte[totalSize];
                    int offset = 0;

                    for (DataBuffer dataBuffer : dataBuffers) {
                        int readableByteCount = dataBuffer.readableByteCount();
                        dataBuffer.read(bytes, offset, readableByteCount);
                        offset += readableByteCount;
                        DataBufferUtils.release(dataBuffer);
                    }

                    return Mono.just(bytes);
                });

    }

    public Mono<ResponseEntity<UploadResult>> uploadFileToS3(FilePart filePart) {
        var key = generateKey(filePart);
        return getBytes(filePart).flatMap(bytes -> {
            String contentType = filePart.headers().getContentType().toString();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3ConfigProperties.getBucket())
                    .key(key)
                    .contentType(contentType)
                    .build();

            return Mono.fromFuture(s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromBytes(bytes)))
                    .map(response ->
                            ResponseEntity.status(HttpStatus.CREATED)
                                    .body(new UploadResult(HttpStatus.CREATED, new String[]{key})));
        }).onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new UploadResult(HttpStatus.BAD_REQUEST, null))));
    }


    public Mono<ResponseEntity<Flux<ByteBuffer>>> downloadFile(String filekey) {
        var objectRequest = GetObjectRequest.builder()
                .bucket(s3ConfigProperties.getBucket())
                .key(filekey)
                .build();

        return Mono.fromFuture(s3AsyncClient.getObject(objectRequest, AsyncResponseTransformer.toPublisher()))
                .flatMap(responsePublisher -> {
                    var response = responsePublisher.response();
                    var body = Flux.from(responsePublisher);

                    return Mono.just(ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(response.contentType()))
                            .body(body));
                })
                .onErrorResume(throwable -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    public String generateKey(FilePart filePart) {
        return UUID.randomUUID().toString().substring(10) + filePart.filename();
    }

}

