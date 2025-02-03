package backend.rdb.entities.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import backend.rdb.entities.enums.SaleStatus;

@ReadingConverter
public class StringToSaleStatusConverter implements Converter<String, SaleStatus> {
    @Override
    public SaleStatus convert(String source) {
        return SaleStatus.valueOf(source);
    }
}
