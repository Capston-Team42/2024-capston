package team.suajung.ad.ress.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import team.suajung.ad.ress.enums.Attribute;
import team.suajung.ad.ress.enums.Style;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class NotNullFields {

    private Map<String, Style> notNullStyleFields;

    private Map<String, List<Attribute>> notNullListFields;

    private Map<String, Boolean> notNullBooleanFields;
}
