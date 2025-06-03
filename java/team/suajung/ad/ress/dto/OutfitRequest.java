package team.suajung.ad.ress.dto;

import lombok.Data;

import team.suajung.ad.ress.enums.UniqueCoordinationType;

import java.util.List;

@Data
public class OutfitRequest {

    private Integer maxTemperature;

    private Integer minTemperature;

    private String schedule;

    private String requirements;

    private List<String> necessaryClothesIds;

    private UniqueCoordinationType uniqueCoordinationType;

    private List<String> wardrobeNames;

    private Boolean useBasicWardrobe;
}
