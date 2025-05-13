package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum TopLength implements Attribute {

    HALF("half"),
    CROP("crop"),
    REGULAR("regular"),
    LONG("long");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public TopLength from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (TopLength length : TopLength.values()) {
            if (length.jsonValue.equals(normalizedValue)) {
                return length;
            }
        }

        throw new IllegalArgumentException("Unknown TopLength: " + value);
    }
}

