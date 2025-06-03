package team.suajung.ad.ress.repository1;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.suajung.ad.ress.digital_wardrobe.model.Item;
import team.suajung.ad.ress.dto.SkirtDto;

import java.util.Optional;

public interface SkirtRepository extends MongoRepository<SkirtDto, String> {

    Optional<SkirtDto> findByProductUrl(String productUrl);
}