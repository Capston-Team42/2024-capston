package team.suajung.ad.ress.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "skirt")
public class SkirtDto extends ClothesDto {

    private String skirtLength;

    private String skirtFit;

    private String skirtType;

}
