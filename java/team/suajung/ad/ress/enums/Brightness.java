package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum Brightness implements Attribute {

    WHITE("white"),
    HIGH("high"),
    MEDIUM("medium"),
    LOW("low"),
    BLACK("black");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public Brightness from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (Brightness brightness : Brightness.values()) {
            if (brightness.jsonValue.equals(normalizedValue)) {
                return brightness;
            }
        }

        throw new IllegalArgumentException("Unknown Brightness: " + value);
    }
}

