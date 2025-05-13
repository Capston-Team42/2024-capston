package team.suajung.ad.ress.repository1;

import org.springframework.data.mongodb.repository.MongoRepository;

import team.suajung.ad.ress.digital_wardrobe.model.Item;
import team.suajung.ad.ress.dto.DressDto;

import java.util.Optional;

public interface DressRepository extends MongoRepository<DressDto, String> {

    Optional<DressDto> findByProductUrl(String productUrl);
}
