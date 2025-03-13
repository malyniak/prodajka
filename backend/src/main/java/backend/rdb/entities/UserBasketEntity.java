package backend.rdb.entities;

import jakarta.persistence.Column;

import lombok.Getter;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "baskets")
@Getter
public class UserBasketEntity extends BaseEntity{
    @Column(nullable = false)
    String userId;

    @PersistenceCreator
    public UserBasketEntity(String id, String userId) {
        super(id);
        this.userId = userId;
    }

    public UserBasketEntity() {
    }
}
