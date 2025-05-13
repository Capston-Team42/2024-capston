package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum Season implements Attribute {

    SPRING("spring"),
    SUMMER("summer"),
    AUTUMN("autumn"),
    WINTER("winter");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public Season from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (Season season : Season.values()) {
            if (season.jsonValue.equals(normalizedValue)) {
                return season;
            }
        }

        throw new IllegalArgumentException("Unknown Season: " + value);
    }
}
