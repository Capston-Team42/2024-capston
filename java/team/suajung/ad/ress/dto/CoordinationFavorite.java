package team.suajung.ad.ress.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Builder
@Document(collection = "coordination_favorite")
public class CoordinationFavorite {
    @Id
    private String id;
    private String userId;
    private Map<String, String> clothesMap;
}