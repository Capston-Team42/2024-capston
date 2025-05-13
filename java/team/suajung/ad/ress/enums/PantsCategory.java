package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum PantsCategory implements Attribute {

    DENIM_PANTS("denim pants"),
    TRAINING_PANTS("training pants"),
    COTTON_PANTS("cotton pants"),
    SUIT_PANTS_SLACKS("suit pants/slacks"),
    LEGGINGS("leggings"),
    JUMPSUIT_OVERALL("jumpsuit/overall");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public PantsCategory from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (PantsCategory category : PantsCategory.values()) {
            if (category.jsonValue.equals(normalizedValue)) {
                return category;
            }
        }

        throw new IllegalArgumentException("Unknown PantsCategory: " + value);
    }
}
