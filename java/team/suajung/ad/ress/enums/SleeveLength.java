package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum SleeveLength implements Attribute {

    SLEEVELESS("sleeveless"),
    SHORT_SLEEVES("short sleeves"),
    THREE_QUARTER_SLEEVES("three-quarter sleeves"),
    LONG_SLEEVES("long sleeves");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public SleeveLength from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (SleeveLength length : SleeveLength.values()) {
            if (length.jsonValue.equals(normalizedValue)) {
                return length;
            }
        }

        throw new IllegalArgumentException("Unknown SleeveLength: " + value);
    }
}

