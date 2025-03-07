package backend.domain.dto;

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
public class LaptopDto {
   private String id;
   private String productId;
   private String display;
   private String frequencies;
   private String camera;
   private String versionOS;
   private String memory;
}
