package backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductDetailsDto<T> {
    private final ProductDto productFullDto;
    private final T details;
}
