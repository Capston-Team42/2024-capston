package team.suajung.ad.ress.repository1;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.suajung.ad.ress.digital_wardrobe.model.Item;
import team.suajung.ad.ress.dto.TopDto;

import java.util.Optional;

public interface TopRepository extends MongoRepository<TopDto, String> {

    Optional<TopDto> findByProductUrl(String productUrl);
}
