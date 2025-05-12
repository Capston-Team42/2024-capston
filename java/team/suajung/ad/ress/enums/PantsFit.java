package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum PantsFit implements Attribute {

    WIDE("wide"),
    STRAIGHT("straight"),
    TAPERED("tapered"),
    SLIM_SKINNY("slim/skinny"),
    BOOT_CUT("boot cut"),
    BAGGY_FIT("baggy fit"),
    JOGGER_FIT("jogger fit");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public PantsFit from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (PantsFit fit : PantsFit.values()) {
            if (fit.jsonValue.equals(normalizedValue)) {
                return fit;
            }
        }

        throw new IllegalArgumentException("Unknown PantsFit: " + value);
    }
}

