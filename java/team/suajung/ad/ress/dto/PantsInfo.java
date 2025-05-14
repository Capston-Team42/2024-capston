package team.suajung.ad.ress.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import team.suajung.ad.ress.enums.*;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class PantsInfo extends ClothesInfo {

    private List<PantsCategory> category;

    private List<BottomLength> bottomLength;

    private List<PantsFit> pantsFit;

    @Override
    public Map<String, List<Attribute>> getNotNullListFields() {

        Map<String, List<Attribute>> notNullListFields = super.getNotNullListFields();

        if (category != null) {
            List<Attribute> categoryTemp = category.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("category", categoryTemp);
        }

        if (bottomLength != null) {
            List<Attribute> bottomLengthTemp = bottomLength.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("bottomLength", bottomLengthTemp);
        }

        if (pantsFit != null) {
            List<Attribute> pantsFitTemp = pantsFit.stream().map(e -> (Attribute) e).toList();

            notNullListFields.put("pantsFit", pantsFitTemp);
        }

        return notNullListFields;
    }
}
