package backend.configs;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(S3ConfigProperties.class)
public class S3ClientConfiguration {
    private final S3ConfigProperties properties;

    public S3ClientConfiguration(S3ConfigProperties properties) {
        this.properties = properties;
    }

    @Bean
    public S3AsyncClient s3AsyncClient() {
        SdkAsyncHttpClient sdkAsyncHttpClient = NettyNioAsyncHttpClient.builder()
                .writeTimeout(Duration.ofSeconds(10))
                .maxConcurrency(64)
                .readTimeout(Duration.ofSeconds(5))
                .build();
        var serviceConfiguration = S3Configuration.builder()
                .chunkedEncodingEnabled(true)
                .build();
        var builder = S3AsyncClient.builder()
                .region(Region.of(properties.getRegion()))
                .httpClient(sdkAsyncHttpClient)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey())))
                .serviceConfiguration(serviceConfiguration);
        return builder.build();
    }
}
