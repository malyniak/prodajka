package backend.rdb.entities;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("tvs")
@Getter
@Setter
public class TvEntity extends BaseEntity {
    @Column(name = "product_id", nullable = false)
    String productId;
    Double diagonal;
    String resolvingPower;
    String platform;
    String frequency;

    @PersistenceCreator
    public TvEntity(String id, String productId, Double diagonal, String resolvingPower, String platform, String frequency) {
        super(id);
        this.productId = productId;
        this.diagonal = diagonal;
        this.resolvingPower = resolvingPower;
        this.platform = platform;
        this.frequency = frequency;
    }

    public TvEntity() {
    }
}
