package backend.rdb.entities.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import backend.rdb.entities.enums.ProductCategory;

@ReadingConverter
public class StringToProductCategoryConverter implements Converter<String, ProductCategory> {
    @Override
    public ProductCategory convert(String source) {
        return ProductCategory.valueOf(source);
    }
}
