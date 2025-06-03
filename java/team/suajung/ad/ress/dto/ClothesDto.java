package team.suajung.ad.ress.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@ToString
public class ClothesDto {

    @Id
    private String _id;

    private String imageUrl; 
    private String productUrl;

    private String style1;
    private String style2;
    private String style3;

    private String color;
    private String saturation;
    private String brightness;

    private String pattern;

    private String season;

    private String tpo;

    private String detail1;
    private String detail2;
    private String detail3;

    private Boolean isUnique;

    private List<Double> tpoEmbedding;

    private List<Double> detail1Embedding;
    private List<Double> detail2Embedding;
    private List<Double> detail3Embedding;
}
