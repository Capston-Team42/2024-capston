package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum Pattern implements Attribute {

    STRIPE("stripe"),
    CHECK("check"),
    FLOWER("flower"),
    DOT("dot"),
    PATCHWORK("patchwork"),
    CAMOUFLAGE("camouflage"),
    PAISLEY("paisley"),
    TROPICAL("tropical"),
    HOUND_TOOTH("hound tooth"),
    HERRINGBONE("herringbone"),
    OTHER_PATTERN("other pattern"),
    PLAIN("plain");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public Pattern from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (Pattern pattern : Pattern.values()) {
            if (pattern.jsonValue.equals(normalizedValue)) {
                return pattern;
            }
        }

        throw new IllegalArgumentException("Unknown Pattern: " + value);
    }
}

