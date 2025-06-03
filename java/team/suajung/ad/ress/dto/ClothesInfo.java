package team.suajung.ad.ress.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import team.suajung.ad.ress.enums.*;
import team.suajung.ad.ress.util.OpenAIClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TopInfo.class, name = "top1"),
        @JsonSubTypes.Type(value = TopInfo.class, name = "top2"),
        @JsonSubTypes.Type(value = PantsInfo.class, name = "pants"),
        @JsonSubTypes.Type(value = SkirtInfo.class, name = "skirt"),
        @JsonSubTypes.Type(value = DressInfo.class, name = "dress"),
        @JsonSubTypes.Type(value = OuterwearInfo.class, name = "outerwear1"),
        @JsonSubTypes.Type(value = OuterwearInfo.class, name = "outerwear2"),
        @JsonSubTypes.Type(value = OuterwearInfo.class, name = "outerwear as top1"),
        @JsonSubTypes.Type(value = OuterwearInfo.class, name = "outerwear as top2"),
})
public class ClothesInfo {

    private String _id;

    private Style style1;
    private Style style2;
    private Style style3;

    private List<Color> color;
    private List<Saturation> saturation;
    private List<Brightness> brightness;

    private List<Pattern> pattern;

    private List<Season> season;

    private String tpo;

    private String detail1;
    private String detail2;
    private String detail3;

    private Boolean isUnique;

    private List<Double> tpoEmbedding;

    private List<Double> detail1Embedding;
    private List<Double> detail2Embedding;
    private List<Double> detail3Embedding;

    public void setEmbeddingFields(OpenAIClient openAIClient) throws IOException {

        if (tpo != null) tpoEmbedding = openAIClient.embeddings(tpo);

        if (detail1 != null) detail1Embedding = openAIClient.embeddings(detail1);
        if (detail2 != null) detail2Embedding = openAIClient.embeddings(detail2);
        if (detail3 != null) detail3Embedding = openAIClient.embeddings(detail3);
    }

    public Map<String, Style> getNotNullStyleFields() {

        Map<String, Style> notNullStyleFields = new HashMap<>();

        if (style1 != null) notNullStyleFields.put("style1", style1);
        if (style2 != null) notNullStyleFields.put("style2", style2);
        if (style3 != null) notNullStyleFields.put("style3", style3);

        return notNullStyleFields;
    }

    public Map<String, List<Attribute>> getNotNullListFields() {

        Map<String, List<Attribute>> notNullListFields = new HashMap<>();

        if (color != null) {
            List<Attribute> colorTemp = color.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("color", colorTemp);
        }
        if (saturation != null) {
            List<Attribute> saturationTemp = saturation.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("saturation", saturationTemp);
        }
        if (brightness != null) {
            List<Attribute> brightnessTemp = brightness.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("brightness", brightnessTemp);
        }

        if (pattern != null) {
            List<Attribute> patternTemp = pattern.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("pattern", patternTemp);
        }

        if (season != null) {
            List<Attribute> seasonTemp = season.stream().map(e -> (Attribute) e).toList();
            notNullListFields.put("season", seasonTemp);
        }

        return notNullListFields;
    }

    public Map<String, Boolean> getNotNullBooleanFields() {

        Map<String, Boolean> notNullBooleanFields = new HashMap<>();

        if (isUnique != null) notNullBooleanFields.put("isUnique", isUnique);

        return notNullBooleanFields;
    }

    public Map<String, List<Double>> getNotNullDetailEmbeddingFields() {

        Map<String, List<Double>> notNullDetailEmbeddingFields = new HashMap<>();

        if (detail1Embedding != null) notNullDetailEmbeddingFields.put("detail1Embedding", detail1Embedding);
        if (detail2Embedding != null) notNullDetailEmbeddingFields.put("detail2Embedding", detail2Embedding);
        if (detail3Embedding != null) notNullDetailEmbeddingFields.put("detail3Embedding", detail3Embedding);

        return notNullDetailEmbeddingFields;
    }

    public Map<String, List<Double>> getNotNullOtherEmbeddingFields() {

        Map<String, List<Double>> notNullOtherEmbeddingFields = new HashMap<>();

        if (tpoEmbedding != null) notNullOtherEmbeddingFields.put("tpoEmbedding", tpoEmbedding);

        return notNullOtherEmbeddingFields;
    }

}
