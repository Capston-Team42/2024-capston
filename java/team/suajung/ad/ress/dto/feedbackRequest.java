package team.suajung.ad.ress.dto;

import lombok.Data;
import team.suajung.ad.ress.enums.ClothesKey;

import java.util.Map;

@Data
public class feedbackRequest {

    Map<ClothesKey, String> outfit;

    String feedback;
}
