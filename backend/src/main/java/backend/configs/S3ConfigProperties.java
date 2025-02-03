package backend.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.s3")
@Getter
@Setter
public class S3ConfigProperties {

    private String region;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
