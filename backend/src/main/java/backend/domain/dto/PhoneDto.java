package backend.domain.dto;


import backend.rdb.entities.enums.ProductCategory;
import backend.rdb.entities.enums.SaleStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhoneDto {
    private String id;
    private String display;
    private String frequencies;
    private String camera;
    private String versionOS;
    private String batteryPower;
    private String otherFunctions;
    private Boolean is3g;
    private Boolean isLTE;
    private Boolean is5g;
    private Boolean isNanoSim;
    private Boolean is_2Sim;
    private String productId;
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
