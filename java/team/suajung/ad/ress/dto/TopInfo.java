package team.suajung.ad.ress.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.suajung.ad.ress.enums.*;
import team.suajung.ad.ress.util.OpenAIClient;

import java.io.IOException;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class TopInfo extends ClothesInfo {

    private List<TopCategory> category;

    private List<TopLength> topLength;

    private List<SleeveLength> sleeveLength;

    private List<Fit> fit;

    private String print;

    private Boolean isSeeThrough;

    private Boolean isSimple;

    private List<Double> printEmbedding;

    @Autowired
    private OpenAIClient openAIClient;

    @Override
    public void setEmbeddingFields(OpenAIClient openAIClient) throws IOException {

        super.setEmbeddingFields(openAIClient);

        if (print != null) printEmbedding = openAIClient.embeddings(print);
    }

    @Override
    public Map<String, List<Attribute>> getNotNullListFields() {

        Map<String, List<Attribute>> notNullListFields = super.getNotNullListFields();

        if (category != null) {
            List<Attribute> categoryTemp = category.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("category", categoryTemp);
        }

        if (topLength != null) {
            List<Attribute> topLengthTemp = topLength.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("topLength", topLengthTemp);
        }

        if (sleeveLength != null) {
            List<Attribute> sleeveLengthTemp = sleeveLength.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("sleeveLength", sleeveLengthTemp);
        }

        if (fit != null) {
            List<Attribute> fitTemp = fit.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("fit", fitTemp);
        }

        return notNullListFields;
    }

    @Override
    public Map<String, Boolean> getNotNullBooleanFields() {

        Map<String, Boolean> notNullBooleanFields = super.getNotNullBooleanFields();

        if (isSeeThrough != null) notNullBooleanFields.put("isSeeThrough", isSeeThrough);

        if (isSimple != null) notNullBooleanFields.put("isSimple", isSimple);

        return notNullBooleanFields;
    }

    @Override
    public Map<String, List<Double>> getNotNullOtherEmbeddingFields() {

        Map<String, List<Double>> notNullOtherEmbeddingFields = super.getNotNullOtherEmbeddingFields();

        if (printEmbedding != null) notNullOtherEmbeddingFields.put("printEmbedding", printEmbedding);

        return notNullOtherEmbeddingFields;
    }

}
