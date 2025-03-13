package backend.domain.model;

import backend.rdb.entities.enums.ProductCategory;
import backend.rdb.entities.enums.SaleStatus;
import lombok.Builder;

@Builder
public class Product {
    private String id;
    private String fullName;
    private Integer price;
    private String brand;
    private String model;
    private Double avgGrade;
    private SaleStatus saleStatus;
    private ProductCategory productCategory;
    private Boolean isAccess;
    private String description;
    private String imageUrl;
    private String videoUrl;
    private String color;

}
