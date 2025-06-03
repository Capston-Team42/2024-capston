package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum TopCategory implements Attribute {

    SWEATSHIRT("sweatshirt"),
    HOODED_SWEATSHIRT("hooded sweatshirt"),
    SHIRT_BLOUSE("shirt/blouse"),
    T_SHIRT("t-shirt"),
    KNIT("knit");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public TopCategory from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (TopCategory category : TopCategory.values()) {
            if (category.jsonValue.equals(normalizedValue)) {
                return category;
            }
        }

        throw new IllegalArgumentException("Unknown TopCategory: " + value);
    }
}
