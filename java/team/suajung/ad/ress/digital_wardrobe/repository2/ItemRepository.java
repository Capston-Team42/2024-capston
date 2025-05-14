package team.suajung.ad.ress.digital_wardrobe.repository2;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.suajung.ad.ress.digital_wardrobe.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByWardrobeId(String wardrobeId);

    Optional<Item> findById(String id);
    Optional<Item> findByProductUrl(String productUrl);
}