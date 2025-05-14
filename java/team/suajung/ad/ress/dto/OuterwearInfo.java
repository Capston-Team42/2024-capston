package team.suajung.ad.ress.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import team.suajung.ad.ress.enums.*;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class OuterwearInfo extends ClothesInfo {

    private List<OuterwearCategory> category;

    private List<TopLength> topLength;

    private List<SleeveLength> sleeveLength;

    private List<Fit> fit;

    private Boolean isSeeThrough;

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

        return notNullBooleanFields;
    }
}
