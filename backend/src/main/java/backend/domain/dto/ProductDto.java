package backend.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import backend.rdb.entities.enums.ProductCategory;
import backend.rdb.entities.enums.SaleStatus;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
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
