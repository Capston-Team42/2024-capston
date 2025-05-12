package team.suajung.ad.ress.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@AllArgsConstructor
public enum BottomLength implements Attribute {

    SHORTS("shorts"),
    BERMUDA_PANTS("bermuda pants"),
    CAPRI_PANTS("capri pants"),
    ANKLE_PANTS("ankle pants"),
    LONG_PANTS("long pants");

    private String jsonValue;

    @Override
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    @JsonCreator
    public BottomLength from(String value) {

        String normalizedValue = value.toLowerCase();
        
        for (BottomLength length : BottomLength.values()) {
            if (length.jsonValue.equals(normalizedValue)) {
                return length;
            }
        }
        
        throw new IllegalArgumentException("Unknown BottomLength: " + value);
    }
}
