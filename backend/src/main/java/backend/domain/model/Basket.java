package backend.domain.model;



import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
public class Basket {
    private String id;
    private String userId;
    private Map<Product, Integer> products;
}
