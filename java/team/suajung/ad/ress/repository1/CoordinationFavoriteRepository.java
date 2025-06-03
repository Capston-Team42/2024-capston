package team.suajung.ad.ress.repository1;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.suajung.ad.ress.dto.CoordinationFavorite;

import java.util.List;

public interface CoordinationFavoriteRepository extends MongoRepository<CoordinationFavorite, String> {
    List<CoordinationFavorite> findByUserId(String userId);
}
