package team.suajung.ad.ress.repository1;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.suajung.ad.ress.dto.TopDto;

public interface TopRepository extends MongoRepository<TopDto, String> {

}
