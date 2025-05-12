package team.suajung.ad.ress.repository1;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.suajung.ad.ress.dto.OuterwearDto;

public interface OuterwearRepository extends MongoRepository<OuterwearDto, String> {

}
