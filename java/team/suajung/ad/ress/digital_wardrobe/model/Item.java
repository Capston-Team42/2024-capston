package team.suajung.ad.ress.digital_wardrobe.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import team.suajung.ad.ress.dto.ClothesDto;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "UserItem")
public class Item extends ClothesDto {

    private String userId;
    private String wardrobeId;

	private String category;

	private String topLength;

	private String sleeveLength;

	private String fit;

	private String print;

	private Boolean isSeeThrough;

	private Boolean isSimple;

	private String bottomLength;
	private String pantsFit;

	private String skirtLength;

	private String skirtFit;

	private String skirtType;
}