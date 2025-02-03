package backend.rdb.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "feedback")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackEntity extends BaseEntity {
    UUID productId;
    String description;
}
