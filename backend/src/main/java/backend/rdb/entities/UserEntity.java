package backend.rdb.entities;

import backend.rdb.entities.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@AllArgsConstructor
@Getter
@Setter
@Table("users")
public class UserEntity extends BaseEntity {
    UserRole userRole;
    String firstname;
    String lastname;
    String email;
    String phoneNumber;
    String password;

    @PersistenceCreator
    public UserEntity(String id, boolean isNewEntity, UserRole userRole, String firstname, @Nullable String lastname, String email, String phoneNumber, String password) {
        super(id, isNewEntity);
        this.userRole = userRole;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public UserEntity() {
    }
}
