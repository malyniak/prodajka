package backend.rdb.entities;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table(name ="laptops")
@Getter
@Setter
public class LaptopEntity extends BaseEntity {
    @Column(name = "product_id", nullable = false,  columnDefinition = "CHAR(36)")
   private String productId;
   private String display;
   private String frequencies;
   private String camera;
   private String versionOS;
   private String memory;

    @PersistenceCreator
    public LaptopEntity(String id, String productId, String display, String frequencies, String camera, String versionOS, String memory) {
        super(id);
        this.productId = productId;
        this.display = display;
        this.frequencies = frequencies;
        this.camera = camera;
        this.versionOS = versionOS;
        this.memory = memory;
    }

    public LaptopEntity() {
    }
}
