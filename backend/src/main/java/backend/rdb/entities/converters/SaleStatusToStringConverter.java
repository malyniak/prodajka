package backend.rdb.entities.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import backend.rdb.entities.enums.SaleStatus;

@WritingConverter
public class SaleStatusToStringConverter implements Converter<SaleStatus, String> {
    @Override
    public String convert(SaleStatus source) {
        return source.name();
    }
}
