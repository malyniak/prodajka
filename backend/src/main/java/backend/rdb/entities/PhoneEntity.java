package backend.rdb.entities;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "phones")
@Getter
@Setter
public class PhoneEntity extends BaseEntity {

    @Column(name = "product_id", nullable = false)
    private String productId;
    private String display;
    private String frequencies;
    private String camera;
    private String versionOS;
    private String batteryPower;
    private String otherFunctions;
    private Boolean is3g;
    private Boolean isLTE;
    private Boolean is5g;
    private Boolean isNanoSim;
    private Boolean is_2Sim;

    @PersistenceCreator
    public PhoneEntity(String id, String productId, String display, String frequencies, String camera, String versionOS, String batteryPower, String otherFunctions, Boolean is3g, Boolean isLTE, Boolean is5g, Boolean isNanoSim, Boolean is_2Sim) {
        super(id);
        this.productId = productId;
        this.display = display;
        this.frequencies = frequencies;
        this.camera = camera;
        this.versionOS = versionOS;
        this.batteryPower = batteryPower;
        this.otherFunctions = otherFunctions;
        this.is3g = is3g;
        this.isLTE = isLTE;
        this.is5g = is5g;
        this.isNanoSim = isNanoSim;
        this.is_2Sim = is_2Sim;
    }

    public PhoneEntity() {
    }
}
