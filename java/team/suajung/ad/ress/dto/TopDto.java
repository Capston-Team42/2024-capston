package team.suajung.ad.ress.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "top")
public class TopDto extends ClothesDto {

    private String category;

    private String topLength;

    private String sleeveLength;

    private String fit;

    private String print;

    private Boolean isSeeThrough;

    private Boolean isSimple;
}
