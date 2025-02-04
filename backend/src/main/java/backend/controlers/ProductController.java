package backend.controlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import backend.dto.ProductDto;
import backend.rdb.entities.ProductEntity;
import backend.services.AwsService;
import backend.services.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

@RestController
@RequestMapping(value = "/api/product")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    private final ProductService productService;
    private final AwsService awsService;

    @GetMapping
    public Flux<ProductDto> getAllPhones() {
        return productService.getAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<ProductDto> getById(@PathVariable String id) {
        return productService.getById(id);
    }

    @PutMapping
    public Mono<ProductEntity> create(@RequestBody ProductDto productFullDto) {
        return productService.create(productFullDto);
    }

    @PatchMapping(value = "/{id}")
    public Mono<ProductEntity> update(@RequestBody ProductDto productFullDto, @PathVariable String id) {
        return productService.update(productFullDto, id);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return productService.delete(id);
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ProductEntity> uploadHandler(@RequestPart("file") FilePart filePart, @RequestParam String id) {
        return productService.updateImageFile(filePart, id);
    }

    @GetMapping(path = "/{filekey}")
    public Mono<ResponseEntity<Flux<ByteBuffer>>> downloadFile(@PathVariable("filekey") String filekey) {
        return awsService.downloadFile(filekey);
    }

    @DeleteMapping(path = "/delete/{filekey}")
    public Mono<Void> deleteFile (@PathVariable("filekey") String filekey) {
       return productService.deleteFile(filekey);
    }
    @GetMapping("/sorted")
    public Flux<ProductDto> products(@RequestParam String sortedBy, @RequestParam String type) {
        return productService.getProductsDetails(sortedBy, type);
    }
}
