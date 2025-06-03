package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum UniqueCoordinationType implements Attribute {

    PATTERN_ON_PATTERN("pattern on pattern"),
    LAYERED_COORDINATION("layered coordination"),
    CROSSOVER_COORDINATION("crossover coordination"),
    NO_UNIQUE_COORDINATION("no unique coordination");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public UniqueCoordinationType from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (UniqueCoordinationType type : UniqueCoordinationType.values()) {
            if (type.jsonValue.equals(normalizedValue)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown UniqueCoordinationType: " + value);
    }
}
