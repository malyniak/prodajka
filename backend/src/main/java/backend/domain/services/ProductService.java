package backend.domain.services;

import backend.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import backend.domain.dto.ProductDto;
import backend.rdb.entities.ProductEntity;
import backend.rdb.repositories.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.util.Arrays;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AwsService awsService;
    private final TransactionalOperator transactionalOperator;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public Flux<ProductDto> getAll() {
        var products = productRepository.findAll()
                .doOnNext(phoneEntity -> System.out.println("Product " + phoneEntity))
                .doOnError(System.out::println);
        return products.map(this::toProductFullDto);
    }

    public Flux<ProductDto> getProductsDetails(String sortedBy, String productType) {
        Query query = Query.query(Criteria.where("productCategory").is(productType))
                .sort(Sort.by(Sort.Direction.ASC, sortedBy))
                .limit(10)
                .offset(0);
        return r2dbcEntityTemplate.select(query, ProductEntity.class).map(this::toProductFullDto);
    }

    public Mono<ProductDto> getById(String id) {
        return productRepository.findById(id).map(this::toProductFullDto);
    }


    public Mono<ProductEntity> create(ProductDto productFullDto) {
        var productEntity = toProductEntity(productFullDto);
        return productRepository.save(productEntity)
                .doOnNext(__ -> System.out.println("Product created"))
                .doOnError(__ -> System.out.println("Save error"));
    }

    public Mono<ProductEntity> update(ProductDto productFullDto, String id) {

        return productRepository.findById(id)
                .flatMap(product -> {
                    System.out.println(product);
                    if (productFullDto.getIsAccess() != null) {
                        product.setIsAccess(productFullDto.getIsAccess());
                    }
                    if (productFullDto.getFullName() != null) {
                        product.setFullName(productFullDto.getFullName());
                    }
                    if (productFullDto.getProductCategory() != null) {
                        product.setProductCategory(productFullDto.getProductCategory());
                    }
                    if (productFullDto.getBrand() != null) {
                        product.setBrand(productFullDto.getBrand());
                    }
                    if (productFullDto.getAvgGrade() != null) {
                        product.setAvgGrade(productFullDto.getAvgGrade());
                    }
                    if (productFullDto.getColor() != null) {
                        product.setColor(productFullDto.getColor());
                    }
                    if (productFullDto.getDescription() != null) {
                        product.setDescription(productFullDto.getDescription());
                    }
                    if (productFullDto.getImageUrl() != null) {
                        product.setImageUrl(productFullDto.getImageUrl());
                    }
                    if (productFullDto.getModel() != null) {
                        product.setModel(productFullDto.getModel());
                    }
                    if (productFullDto.getPrice() != null) {
                        product.setPrice(productFullDto.getPrice());
                    }
                    if (productFullDto.getSaleStatus() != null) {
                        product.setSaleStatus(productFullDto.getSaleStatus());
                    }
                    if (productFullDto.getVideoUrl() != null) {
                        product.setVideoUrl(productFullDto.getVideoUrl());
                    }
                    product.setNewEntity(false);
                    return productRepository.save(product);
                });
    }

    public Mono<ProductEntity> updateImageFile(FilePart filePart, String productId) {
        return awsService.uploadFileToS3(filePart)
                .flatMap(uploadResultResponseEntity -> {
                    return productRepository.findById(productId).map(this::toProductFullDto).flatMap(productFullDto -> {
                        var maybeKey = Arrays.stream(uploadResultResponseEntity.getBody().getFileKeys()).findFirst();
                        if (maybeKey.isEmpty()) {
                            return Mono.error(new RuntimeException("Image upload failed."));
                        }
                        maybeKey.ifPresent(productFullDto::setImageUrl);
                        return this.update(productFullDto, productId);
                    });

                });
    }

    public Product toProduct(ProductDto productDto) {
      return Product.builder()
               .id(productDto.getId())
               .brand(productDto.getBrand())
               .color(productDto.getColor())
               .price(productDto.getPrice())
               .productCategory(productDto.getProductCategory())
               .avgGrade(productDto.getAvgGrade())
               .description(productDto.getDescription())
               .fullName(productDto.getFullName())
               .isAccess(productDto.getIsAccess())
               .imageUrl(productDto.getImageUrl())
               .saleStatus(productDto.getSaleStatus())
               .videoUrl(productDto.getVideoUrl())
               .model(productDto.getModel())
               .build();
    }


    public ProductDto toProductFullDto(ProductEntity productEntity) {
        var productFullDto = new ProductDto();
        productFullDto.setId(productEntity.getId());
        productFullDto.setIsAccess(productEntity.getIsAccess());
        productFullDto.setFullName(productEntity.getFullName());
        productFullDto.setProductCategory(productEntity.getProductCategory());
        productFullDto.setBrand(productEntity.getBrand());
        productFullDto.setAvgGrade(productEntity.getAvgGrade());
        productFullDto.setColor(productEntity.getColor());
        productFullDto.setDescription(productEntity.getDescription());
        productFullDto.setImageUrl(productEntity.getImageUrl());
        productFullDto.setModel(productEntity.getModel());
        productFullDto.setPrice(productEntity.getPrice());
        productFullDto.setSaleStatus(productEntity.getSaleStatus());
        productFullDto.setVideoUrl(productEntity.getVideoUrl());
        return productFullDto;
    }

    public ProductEntity toProductEntity(ProductDto productFullDto) {
        var productEntity = new ProductEntity();
        productEntity.setId(UUID.randomUUID().toString());
        productEntity.setIsAccess(productFullDto.getIsAccess());
        productEntity.setFullName(productFullDto.getFullName());
        productEntity.setProductCategory(productFullDto.getProductCategory());
        productEntity.setBrand(productFullDto.getBrand());
        productEntity.setAvgGrade(productFullDto.getAvgGrade());
        productEntity.setColor(productFullDto.getColor());
        productEntity.setDescription(productFullDto.getDescription());
        productEntity.setImageUrl(productFullDto.getImageUrl());
        productEntity.setModel(productFullDto.getModel());
        productEntity.setPrice(productFullDto.getPrice());
        productEntity.setSaleStatus(productFullDto.getSaleStatus());
        productEntity.setVideoUrl(productFullDto.getVideoUrl());
        productEntity.setNewEntity(true);
        return productEntity;
    }


    public Mono<Void> delete(String id) {
        return productRepository.findById(id).doOnNext(System.out::println)
                .flatMap(productRepository::delete);
    }

    public Mono<Void> deleteFile(String filekey) {
        var deleteRequest = DeleteObjectRequest.builder()
                .bucket(awsService.getS3ConfigProperties().getBucket())
                .key(filekey)
                .build();

        return transactionalOperator.execute(status -> {
              return   productRepository.findByImageUrl(filekey)
                .switchIfEmpty(Mono.error(new RuntimeException("Product with imageUrl not found")))
                .flatMap(productEntity -> {
                    productEntity.setImageUrl(null);

                    return productRepository.save(productEntity)
                            .then(Mono.fromFuture(awsService.getS3AsyncClient().deleteObject(deleteRequest)))
                            .doOnSuccess(response -> System.out.println("File successfully deleted from AWS: " + filekey))
                            .doOnError(e -> System.err.println("Failed to delete file from AWS: " + e.getMessage()));
                });
        }).then();

    }
}