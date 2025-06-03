package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum ClothesKey {

    TOP1("top1"),
    TOP2("top2"),
    PANTS("pants"),
    DRESS("dress"),
    SKIRT("skirt"),
    OUTERWEAR1("outerwear1"),
    OUTERWEAR2("outerwear2"),
    OUTERWEAR_AS_TOP1("outerwear as top1"),
    OUTERWEAR_AS_TOP2("outerwear as top2");

    private String jsonValue;

    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @JsonCreator
    public static ClothesKey from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (ClothesKey key : ClothesKey.values()) {
            if (key.jsonValue.equals(normalizedValue)) {
                return key;
            }
        }

        throw new IllegalArgumentException("Unknown ClothesKey: " + value);
    }
}

