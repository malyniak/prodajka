package backend.rdb.entities;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;
import backend.rdb.entities.enums.ProductCategory;
import backend.rdb.entities.enums.SaleStatus;

@Table(name = "products")
@Getter
@Setter
public class ProductEntity extends BaseEntity {

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

    @PersistenceCreator
    public ProductEntity(String id, String fullName, Integer price, String brand, String model, Double avgGrade, SaleStatus saleStatus, ProductCategory productCategory, Boolean isAccess, String description, String imageUrl, String videoUrl, String color) {
        super(id);
        this.fullName = fullName;
        this.price = price;
        this.brand = brand;
        this.model = model;
        this.avgGrade = avgGrade;
        this.saleStatus = saleStatus;
        this.productCategory = productCategory;
        this.isAccess = isAccess;
        this.description = description;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.color = color;
    }

    public ProductEntity() {
    }
}
