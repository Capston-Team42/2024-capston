package team.suajung.ad.ress.repository1;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.suajung.ad.ress.dto.SkirtDto;

public interface SkirtRepository extends MongoRepository<SkirtDto, String> {

}