package team.suajung.ad.ress.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import team.suajung.ad.ress.enums.*;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class SkirtInfo extends ClothesInfo {

    private List<SkirtLength> skirtLength;

    private List<SkirtFit> skirtFit;

    private List<SkirtType> skirtType;

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

        if (skirtType != null) {
            List<Attribute> skirtTypeTemp = skirtType.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("skirtType", skirtTypeTemp);
        }

        return notNullListFields;
    }
}
