package team.suajung.ad.ress.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "dress")
public class DressDto extends ClothesDto {

    private String skirtLength;

    private String skirtFit;

    private String sleeveLength;

    private String fit;

    private String skirtType;

    private Boolean isSeeThrough;

    private Boolean isTopRequired;
}
