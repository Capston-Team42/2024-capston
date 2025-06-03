package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum Color implements Attribute {

    RED("red"),
    ORANGE("orange"),
    YELLOW("yellow"),
    LIGHT_GREEN("light green"),
    GREEN("green"),
    BLUE_GREEN("blue green"),
    BLUE("blue"),
    NAVY("navy"),
    PURPLE("purple"),
    RED_PURPLE("red purple"),
    ACHROMATIC("achromatic"),
    PINK("pink");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public Color from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (Color color : Color.values()) {
            if (color.jsonValue.equals(normalizedValue)) {
                return color;
            }
        }

        throw new IllegalArgumentException("Unknown Color: " + value);
    }
}
