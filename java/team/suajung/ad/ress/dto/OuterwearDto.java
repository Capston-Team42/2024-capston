package team.suajung.ad.ress.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "outerwear")
@ToString(callSuper = true)
public class OuterwearDto extends ClothesDto {

    private String category;

    private String topLength;

    private String sleeveLength;

    private String fit;

    private Boolean isSeeThrough;
}
