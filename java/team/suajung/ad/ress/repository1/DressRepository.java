package team.suajung.ad.ress.repository1;

import org.springframework.data.mongodb.repository.MongoRepository;

import team.suajung.ad.ress.dto.DressDto;

public interface DressRepository extends MongoRepository<DressDto, String> {

}
