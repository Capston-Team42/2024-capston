package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public interface Attribute {

    @JsonValue
    String getJsonValue();

    @JsonCreator
    Attribute from(String value);
}
