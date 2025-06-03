package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum OutfitKey {

    OUTFIT1("outfit1"),
    OUTFIT2("outfit2"),
    OUTFIT3("outfit3");

    private String jsonValue;

    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @JsonCreator
    public static OutfitKey from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (OutfitKey key : OutfitKey.values()) {
            if (key.jsonValue.equals(normalizedValue)) {
                return key;
            }
        }

        throw new IllegalArgumentException("Unknown OutfitKey: " + value);
    }
}

