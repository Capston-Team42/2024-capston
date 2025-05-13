package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum Fit implements Attribute {

    SLIM("slim"),
    REGULAR("regular"),
    OVERSIZE("oversize");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public Fit from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (Fit fit : Fit.values()) {
            if (fit.jsonValue.equals(normalizedValue)) {
                return fit;
            }
        }

        throw new IllegalArgumentException("Unknown Fit: " + value);
    }
}
