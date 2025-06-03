package team.suajung.ad.ress.repository1;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.suajung.ad.ress.auth.model.BlacklistedToken;

public interface BlacklistedTokenRepository extends MongoRepository<BlacklistedToken, String> {
    BlacklistedToken findByToken(String token);
}

