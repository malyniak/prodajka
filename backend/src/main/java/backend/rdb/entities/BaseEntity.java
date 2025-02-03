package backend.rdb.entities;

import jakarta.persistence.Column;
import jakarta.persistence.PostLoad;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@AllArgsConstructor
public class BaseEntity implements Persistable<String> {
    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    String id;

    @Transient
    boolean isNewEntity;

    public BaseEntity(String id) {
        this.id = id;
        this.isNewEntity = false;
    }

    public BaseEntity() {
        this.isNewEntity = false;
    }

    @Override
    public boolean isNew() {
        return isNewEntity;
    }

    @PostLoad
    public void markNotNew() {
        this.isNewEntity = false;
    }
}
