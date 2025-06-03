package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum SkirtFit implements Attribute {

    A_LINE("a-line"),
    H_LINE("h-line"),
    BALLOON("balloon"),
    PENCIL("pencil");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public SkirtFit from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (SkirtFit fit : SkirtFit.values()) {
            if (fit.jsonValue.equals(normalizedValue)) {
                return fit;
            }
        }

        throw new IllegalArgumentException("Unknown SkirtFit: " + value);
    }
}

