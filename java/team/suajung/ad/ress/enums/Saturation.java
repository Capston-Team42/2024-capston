package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum Saturation implements Attribute {

    HIGH("high"),
    MEDIUM("medium"),
    LOW("low"),
    ACHROMATIC("achromatic");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public Saturation from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (Saturation saturation : Saturation.values()) {
            if (saturation.jsonValue.equals(normalizedValue)) {
                return saturation;
            }
        }

        throw new IllegalArgumentException("Unknown Saturation: " + value);
    }
}

