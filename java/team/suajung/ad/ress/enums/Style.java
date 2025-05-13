package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum Style implements Attribute {

    CASUAL("casual"),
    STREET("street"),
    GORPCORE("gorpcore"),
    WORKWEAR("workwear"),
    PREPPY("preppy"),
    SPORTY("sporty"),
    ROMANTIC("romantic"),
    GIRLISH("girlish"),
    CLASSIC("classic"),
    MINIMAL("minimal"),
    CHIC("chic"),
    RETRO("retro"),
    ETHNIC("ethnic"),
    RESORT("resort"),
    BALLETCORE("balletcore");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public Style from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (Style style : Style.values()) {
            if (style.jsonValue.equals(normalizedValue)) {
                return style;
            }
        }

        throw new IllegalArgumentException("Unknown Style: " + value);
    }
}

