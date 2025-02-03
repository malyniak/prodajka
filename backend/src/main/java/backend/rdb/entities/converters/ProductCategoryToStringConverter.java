package backend.rdb.entities.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import backend.rdb.entities.enums.ProductCategory;

@WritingConverter
public class ProductCategoryToStringConverter implements Converter <ProductCategory, String>{
    @Override
    public String convert(ProductCategory source) {
        return source.name();
    }
}
