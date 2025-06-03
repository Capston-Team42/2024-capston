package team.suajung.ad.ress.dto;

import lombok.*;
import team.suajung.ad.ress.enums.*;
import team.suajung.ad.ress.util.OpenAIClient;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class DressInfo extends ClothesInfo {

    private List<SkirtLength> skirtLength;

    private List<SkirtFit> skirtFit;

    private List<SleeveLength> sleeveLength;

    private List<Fit> fit;

    private List<SkirtType> skirtType;

    private Boolean isSeeThrough;

    private Boolean isTopRequired;

    @Override
    public Map<String, List<Attribute>> getNotNullListFields() {

        Map<String, List<Attribute>> notNullListFields = super.getNotNullListFields();

        if (skirtLength != null) {
            List<Attribute> skirtLengthTemp = skirtLength.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("skirtLength", skirtLengthTemp);
        }
        if (skirtFit != null) {
            List<Attribute> skirtFitTemp = skirtFit.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("skirtFit", skirtFitTemp);
        }
        if (sleeveLength != null) {
            List<Attribute> sleeveLengthTemp = sleeveLength.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("sleeveLength", sleeveLengthTemp);
        }
        if (fit != null) {
            List<Attribute> fitTemp = fit.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("fit", fitTemp);
        }
        if (skirtType != null) {
            List<Attribute> skirtTypeTemp = skirtType.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("skirtType", skirtTypeTemp);
        }

        return notNullListFields;
    }
}
