package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum SkirtLength implements Attribute {

    MINI_SKIRT("mini skirt"),
    MIDI_SKIRT("midi skirt"),
    LONG_SKIRT("long skirt");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public SkirtLength from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (SkirtLength length : SkirtLength.values()) {
            if (length.jsonValue.equals(normalizedValue)) {
                return length;
            }
        }

        throw new IllegalArgumentException("Unknown SkirtLength: " + value);
    }
}

