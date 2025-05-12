package team.suajung.ad.ress.repository1;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.suajung.ad.ress.dto.PantsDto;

public interface PantsRepository extends MongoRepository<PantsDto, String> {

}
