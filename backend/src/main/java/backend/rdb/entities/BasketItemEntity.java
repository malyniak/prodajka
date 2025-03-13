package backend.rdb.entities;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "basket_elements")
@Getter
@Setter
public class BasketItemEntity extends BaseEntity{
    String basketId;
    @Nullable
    String productId;
    Integer count;

    @PersistenceCreator
    public BasketItemEntity(String id, String basketId, String productId, Integer count) {
        super(id);
        this.basketId = basketId;
        this.productId = productId;
        this.count = count;
    }

    public BasketItemEntity() {
    }
}
