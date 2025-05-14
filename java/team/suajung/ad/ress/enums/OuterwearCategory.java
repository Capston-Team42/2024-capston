package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum OuterwearCategory implements Attribute {

    HOODED_ZIP_UP("hooded zip-up"),
    BLOUSON_MA_1("blouson/MA-1"),
    LEATHER_RIDERS_JACKET("leather/riders jacket"),
    CARDIGAN("cardigan"),
    TRUCKER_JACKET("trucker jacket"),
    SUIT_BLAZER_JACKET("suit/blazer jacket"),
    STADIUM_JACKET("stadium jacket"),
    NYLON_COACH_JACKET("nylon/coach jacket"),
    ANORAK_JACKET("anorak jacket"),
    TRAINING_JACKET("training jacket"),
    SEASON_CHANGE_COAT("season change coat"),
    SAFARI_HUNTING_JACKET("safari/hunting jacket"),
    PADDING("padding"),
    MUSTANG_FUR("mustang/fur"),
    FLEECE("fleece"),
    WINTER_COAT("winter coat"),
    TWEED_JACKET("tweed jacket");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public OuterwearCategory from(String value) {
        if (value == null) return null;

        String normalizedValue = value.trim().toLowerCase();

        for (OuterwearCategory category : OuterwearCategory.values()) {
            if (category.jsonValue.equals(normalizedValue)) {
                return category;
            }
        }

        throw new IllegalArgumentException("Unknown OuterwearCategory: " + value);
    }
}

