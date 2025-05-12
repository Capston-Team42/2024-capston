package team.suajung.ad.ress.repository1;

import org.springframework.data.mongodb.repository.MongoRepository;

import team.suajung.ad.ress.dto.CoordinationRule;

import java.util.Optional;

public interface CoordinationRuleRepository extends MongoRepository<CoordinationRule, String> {

    Optional<CoordinationRule> findByUserId(String userId);
}
