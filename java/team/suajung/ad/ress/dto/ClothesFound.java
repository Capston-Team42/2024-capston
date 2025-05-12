package team.suajung.ad.ress.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ClothesFound {

    private String _id;

    private String imageUrl;

    private String productUrl;

    private Map<String, Double> scores;

    private Double similarity;
}