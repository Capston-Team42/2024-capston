package team.suajung.ad.ress.auth.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "blacklisted_tokens")
public class BlacklistedToken {
    @Id
    private String id;
    private String token;

    @Indexed(expireAfter = "0s")
    private Date expirationDate;
}

