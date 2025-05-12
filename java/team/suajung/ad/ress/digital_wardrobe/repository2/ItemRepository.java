package team.suajung.ad.ress.digital_wardrobe.repository2;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.suajung.ad.ress.digital_wardrobe.model.Item;

import java.util.List;

public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByWardrobeId(String wardrobeId);
}