package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum SkirtType implements Attribute {

    PLEATS("pleats"),
    WRAP("wrap"),
    TIERED("tiered"),
    SKIRT_PANTS("skirt pants"),
    CANCAN("cancan"),
    PLAIN("plain");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public SkirtType from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (SkirtType type : SkirtType.values()) {
            if (type.jsonValue.equals(normalizedValue)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown SkirtType: " + value);
    }
}

