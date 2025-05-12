package team.suajung.ad.ress.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import team.suajung.ad.ress.auth.service.UserDetailsImpl;
import team.suajung.ad.ress.dto.*;
import team.suajung.ad.ress.enums.*;
import team.suajung.ad.ress.service.CoordinationService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/outfit")
public class OutfitController {

    @Autowired
    private CoordinationService coordinationService;

    private String getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userDetails.getId();
    }

    @PostMapping("/recommend")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<OutfitKey, OutfitDto>> recommendOutfit(@RequestBody OutfitRequest request) throws IOException {

        String userId = getCurrentUserId();

        Integer maxTemperature = request.getMaxTemperature();

        Integer minTemperature = request.getMinTemperature();

        String schedule = request.getSchedule();

        String requirements = request.getRequirements();

        List<String> necessaryClothesIds = request.getNecessaryClothesIds();

        UniqueCoordinationType uniqueCoordinationType = request.getUniqueCoordinationType();

        List<String> wardrobeNames = request.getWardrobeNames();

        Boolean useBasicWardrobe = request.getUseBasicWardrobe();

        String coordinationRule = coordinationService.getCoordinationRule(userId);

        String baseJsons = coordinationService.getBaseJsons(minTemperature, uniqueCoordinationType);
        System.out.println(baseJsons);

        List<ClothesDto> necessaryClothesList = coordinationService.getNecessaryClothesList(necessaryClothesIds);
        System.out.println(necessaryClothesList);

        String prompt = "";

        if(uniqueCoordinationType == UniqueCoordinationType.PATTERN_ON_PATTERN) {
            prompt = String.format("""
                    maxTemperature: %d
                    minTemperature: %d
                    schedule: %s
                    requirements: %s
                    baseJsons:
                    coordinationRule: %s
                    %s
                    
                    꼭 포함해야 하는 옷(만약 있다면)과 schedule, requirements에 가장 부합하는 baseJson을 선택하세요.
                    그리고 schedule과 requirements를 반영하여 baseJson에 속성을 추가하세요.
                    반드시 필요한 속성만 사용하세요. 속성이 너무 많으면 검색이 잘 안됩니다.
                    season이 아닌 복수 선택 가능한 속성에는 최대한 많은 값을 넣으세요. 값이 너무 적으면 검색이 잘 안됩니다.
                    
                    사용 가능한 속성들:
                    <공통>
                    - “style1” (“casual”, “street”, “gorpcore”, “workwear”, “preppy”, “sporty”, “romantic”, “girlish”, “classic”. “minimal”, “chic”, “retro”, “ethnic”, “resort”, “balletcore” 중 택 1, 일반 검색)
                    - “style2” (“casual”, “street”, “gorpcore”, “workwear”, “preppy”, “sporty”, “romantic”, “girlish”, “classic”. “minimal”, “chic”, “retro”, “ethnic”, “resort”, “balletcore” 중 택 1, 일반 검색)
                    - “style3” (“casual”, “street”, “gorpcore”, “workwear”, “preppy”, “sporty”, “romantic”, “girlish”, “classic”. “minimal”, “chic”, “retro”, “ethnic”, “resort”, “balletcore” 중 택 1, 일반 검색)
                    - “color” (“red”, “orange”, “yellow”, “light green”, “green”, “blue green”, “blue”, “navy”, “purple”, “red purple”, “achromatic”, "pink" 중 복수 선택, 일반 검색, requirements나 schedule에서 직/간접적으로 color를 제시하지 않았으면 사용 불가)
                    - “saturation” (“high”, “medium”, “low”, “achromatic” 중 복수 선택, 일반 검색)
                    - “brightness” (“white”, “high”, “medium”, “low”, “black” 중 복수 선택, 일반 검색)
                    - “pattern” (“stripe”, “check”, “flower”, “dot”, “patchwork”, “camouflage”, “paisley”, “tropical”, “hound tooth”, “herringbone”, “other pattern”, “plain” 중 복수 선택, 일반 검색)
                    - “season” (“spring”, “summer”, “autumn”, “winter” 중 복수 선택, 일반 검색)
                    - “tpo” (영어로 자유 입력, 벡터 검색)
                    - “detail1” (기타 속성을 영어로 자유 입력, 벡터 검색)
                    - “detail2”
                    - “detail3”
                    <top>
                    - “category” (“sweatshirt”, “hooded sweatshirt”, “shirt/blouse”, “t-shirt”, “knit”, 중 복수 선택, 일반 검색)
                    - “topLength” (“half”, “crop”, “regular”, “long” 중 복수 선택, 일반 검색)
                    - “sleeveLength” (“sleeveless”, “short sleeves”, “three-quarter sleeves”, “long sleeves” 중 복수 선택, 일반 검색)
                    - “fit” (“slim”, “regular”, “oversize” 중 복수 선택, 일반 검색)
                    - “print” (영어로 자유 입력, 벡터 검색)
                    - “isSeeThrough” (true, false 택 1, 일반 검색)
                    - “isSimple” (true, false 중 택 1, 일반 검색)
                    <pants>
                    - “category” (“denim pants”, “training pants”, “cotton pants”, “suit pants/slacks”, “leggings”, “jumpsuit/overall” 중 복수 선택, 일반 검색)
                    - “bottomLength” (“shorts”, “bermuda pants”, “capri pants”, “ankle pants”, “long pants” 중 복수 선택, 일반 검색)
                    - “pantsFit” (“wide”, “straight”, “tapered”, “slim/skinny”, “boot cut”, "baggy fit" “jogger fit” 중 복수 선택, 일반 검색)
                    <skirt>
                    - “skirtLength” (“mini skirt”, “midi skirt”, “long skirt” 중 복수 선택, 일반 검색)
                    - “skirtFit” (“a-line”, “h-line”, “balloon” "pencil" 중 중 복수 선택, 일반 검색)
                    - “skirtType” (“pleats”, “wrap”, “tiered”, “skirt pants”, “cancan”, "plain" 중 복수 선택, 일반 검색)
                    <dress>
                    - “skirtLength” (“mini skirt”, “midi skirt”, “long skirt” 중 복수 선택, 일반 검색)
                    - "skirtFit” (“a-line”, “h-line”, “balloon” "pencil" 중 복수 선택, 일반 검색)
                    - “sleeveLength” (“sleeveless”, “short sleeves”, “three-quarter sleeves”, “long sleeves” 중 복수 선택, 일반 검색)
                    - “fit” (“slim”, “regular”, “oversize” 중 복수 선택, 일반 검색)
                    - “skirtType” (“pleats”, “wrap”, “tiered”, “skirt pants”, “cancan”, "plain" 중 복수 선택, 일반 검색)
                    - “isTopRequired” (true, false 중 택 1, 일반 검색)
                    - “isSeeThrough” (true, false 택 1, 일반 검색)
                    <outerwear>
                    - “category” (“hooded zip-up”, “blouson/MA-1”, “leather/riders jacket”, “cardigan”, “trucker jacket”, “suit/blazer jacket”, “stadium jacket”, “nylon/coach jacket”, “anorak jacket”, “training jacket”, "season change coat", “safari/hunting jacket”, “padding”, “mustang/fur”, “fleece”, “winter coat”, “tweed jacket” 중 복수 선택, 일반 검색)
                    - “topLength” (“half”, “crop”, “regular”, “long” 중 복수 선택, 일반 검색)
                    - “sleeveLength” (“sleeveless”, “short sleeves”, “three-quarter sleeves”, “long sleeves” 중 복수 선택, 일반 검색)
                    - “fit” (“slim”, “regular”, “oversize” 중 복수 선택, 일반 검색)
                    - “isSeeThrough” (true, false 택 1, 일반 검색)
                    
                    만약 부합하는 baseJson이 없다면 꼭 포함해야 하는 옷(만약 있다면)과 minTemperature, schedule, requirements을 참고하여 baseJson을 직접 생성하세요.
                    
                    'top1', 'top2', 'pants', 'skirt', 'dress', 'outerwear1', 'outerwear2', 'outerwear as top1', 'outerwear as top2' 중 하나를 이름으로, "type": (이름, 예: "top1")이 포함된 속성의 json을 값으로 하여 (만약 꼭 포함해야 하는 옷이라면 그 옷의 _id만 있는 json을 값으로 하여) json 형식으로 응답하세요.
                    만약 복수 선택 가능한 속성이라면 값을 배열로 설정하세요.
                    
                    'outerwear1', 'outerwear2'에 대하여 'fit'을 slim이 아닌 모든 값의 배열로 설정하세요.
                    
                    baseJson이 null인 경우 부합하는 json이 없는 경우와 동일한 과정을 수행하세요. 다만 그 과정에서 minTemperature는 고려하지 않아도 됩니다.
                    
                    만약 일교차가 10도 이상이라면 더워졌을 때 아우터를 벗을 수 있게 아우터가 포함된 baseJson을 선택 또는 생성하세요.
                    
                    모든 옷 타입에 대하여 "pattern"을 "plain"이 아닌 모든 값의 배열로 설정하세요.
                    """, maxTemperature, minTemperature, schedule, requirements, coordinationRule, baseJsons);
        }

        if(uniqueCoordinationType == UniqueCoordinationType.CROSSOVER_COORDINATION) {
            prompt = String.format("""
                    maxTemperature: %s
                    minTemperature: %s
                    schedule: %s
                    requirements: %s
                    coordinationRule: %s
                    
                    crossoverCoordinationMethod:
                    - sporty style sweatshirt + suit pants/slacks + classic style season change coat
                    - sporty style sweatshirt + training pants + classic style season change coat
                    - sporty style sweatshirt + suit pants/slacks + classic style winter coat
                    - sporty style sweatshirt + training pants + classic style winter coat
                    - hooded sweatshirt + suit pants/slacks + classic style season change coat
                    - hooded sweatshirt + training pants + classic style season change coat
                    - hooded sweatshirt + suit pants/slacks + classic style winter coat
                    - hooded sweatshirt + training pants + classic style winter coat
                    - sporty style t-shirt + suit pants/slacks + classic style season change coat
                    - sporty style t-shirt + training pants + classic style winter coat
                    - sporty style t-shirt + suit pants/slacks + classic style winter coat
                    - sporty style t-shirt + training pants + classic style winter coat
                    - sporty style t-shirt + classic style skirt + classic style season change coat
                    - sporty style t-shirt + classic style skirt + classic style winter coat
                    - classic style shirt/blouse + training pants
                    - classic style shirt/blouse + training pants + classic style season change coat
                    - classic style shirt/blouse + training pants + classic style winter coat
                    - classic style shirt/blouse + training pants + hooded zip-up
                    - classic style shirt/blouse + training pants + training jacket
                    - classic style shirt/blouse + suit pants/slacks + hooded zip-up
                    - classic style shirt/blouse + suit pants/slacks + training jacket
                    - classic style shirt/blouse + classic style skirt + hooded zip-up
                    - classic style shirt/blouse + classic style skirt + training jacket
                    - classic style dress + hooded zip-up
                    - classic style dress + training jacket
                    - romantic or girlish style shirt/blouse + cargo detail denim pants
                    - romantic or girlish style shirt/blouse + cargo detail denim pants + romantic or girlish style outerwear
                    - romantic or girlish style shirt/blouse + camouflage pattern pants
                    - romantic or girlish style shirt/blouse + camouflage pattern pants + romantic or girlish style outerwear
                    - romantic or girlish style t-shirt + cargo detail denim pants
                    - romantic or girlish style t-shirt + cargo detail denim pants + romantic or girlish style outerwear
                    - romantic or girlish style t-shirt + camouflage pattern pants
                    - romantic or girlish style t-shirt + camouflage pattern pants + romantic or girlish style outerwear
                    - romantic or girlish style knit + cargo detail denim pants
                    - romantic or girlish style knit + cargo detail denim pants + romantic or girlish style outerwear
                    - romantic or girlish style knit + camouflage pattern pants
                    - romantic or girlish style knit + camouflage pattern pants + romantic or girlish style outerwear
                    - romantic or girlish style shirt/blouse + romantic or girlish style pants + safari/hunting jacket
                    - romantic or girlish style shirt/blouse + romantic or girlish style pants + leather/riders jacket
                    - romantic or girlish style shirt/blouse + romantic or girlish style skirt + safari/hunting jacket
                    - romantic or girlish style shirt/blouse + romantic or girlish style skirt + leather/riders jacket
                    - romantic or girlish style t-shirt + romantic or girlish style pants + safari/hunting jacket
                    - romantic or girlish style t-shirt + romantic or girlish style pants + leather/riders jacket
                    - romantic or girlish style t-shirt + romantic or girlish style skirt + safari/hunting jacket
                    - romantic or girlish style t-shirt + romantic or girlish style skirt + leather/riders jacket
                    - romantic or girlish style knit + romantic or girlish style pants + safari/hunting jacket
                    - romantic or girlish style knit + romantic or girlish style pants + leather/riders jacket
                    - romantic or girlish style knit + romantic or girlish style skirt + safari/hunting jacket
                    - romantic or girlish style knit + romantic or girlish style skirt + leather/riders jacket
                    - romantic or girlish style dress + safari/hunting jacket
                    - romantic or girlish style dress + leather/riders jacket
                    
                    꼭 포함해야 하는 옷(만약 있다면)과 minTemperature, schedule, requirements에 부합하는 crossoverCoordinationMethod를 모두 선택하세요.
                    만약 부합하는 crossoverCoordinationMethod이 없다면 꼭 포함해야 하는 옷(만약 있다면)과 minTemperature, schedule, requirements을 참고하여 crossoverCoordinationMethod을 직접 생성하세요.
                    
                    crossoverCoordinationMethod라는 문자열을 이름으로, 값을 배열로 하여 json 형식으로 응답하세요.
                    단, requirements에 crossoverCoordinationMethod에 대한 내용이 있다면 "requirements 참고"을 반환하세요.
                    
                    만약 일교차가 10도 이상이라면 더워졌을 때 아우터를 벗을 수 있게 아우터가 포함된 crossoverCoordinationMethod을 선택 또는 생성하세요.
                    """, maxTemperature, minTemperature, schedule, requirements, coordinationRule);

            List<String> crossoverCoordinationTypeList = coordinationService.getCrossoverCoordinationType(necessaryClothesList, prompt);
            System.out.println(crossoverCoordinationTypeList);

            Random random = new Random();

            int randomInt = random.nextInt(crossoverCoordinationTypeList.size());

            String crossoverCoordinationType = crossoverCoordinationTypeList.get(randomInt);
            System.out.println(crossoverCoordinationType);

            prompt = String.format("""
                    maxTemperature: %d
                    minTemperature: %d
                    schedule: %s
                    requirements: %s
                    coordinationRule: %s
                    baseJsons:
                    %s
                    crossoverCoordinationType: %s
                    
                    꼭 포함해야 하는 옷(만약 있다면)과 schedule, requirements, crossoverCoordinationType에 가장 부합하는 baseJson을 선택하세요.
                    그리고 schedule과 requirements, crossoverCoordinationType를 반영하여 baseJson에 속성을 추가하세요.
                    반드시 필요한 속성만 사용하세요. 속성이 너무 많으면 검색이 잘 안됩니다.
                    season이 아닌 복수 선택 가능한 속성에는 최대한 많은 값을 넣으세요. 값이 너무 적으면 검색이 잘 안됩니다.
                    
                    사용 가능한 속성들:
                    <공통>
                    - “style1” (“casual”, “street”, “gorpcore”, “workwear”, “preppy”, “sporty”, “romantic”, “girlish”, “classic”. “minimal”, “chic”, “retro”, “ethnic”, “resort”, “balletcore” 중 택 1, 일반 검색)
                    - “style2” (“casual”, “street”, “gorpcore”, “workwear”, “preppy”, “sporty”, “romantic”, “girlish”, “classic”. “minimal”, “chic”, “retro”, “ethnic”, “resort”, “balletcore” 중 택 1, 일반 검색)
                    - “style3” (“casual”, “street”, “gorpcore”, “workwear”, “preppy”, “sporty”, “romantic”, “girlish”, “classic”. “minimal”, “chic”, “retro”, “ethnic”, “resort”, “balletcore” 중 택 1, 일반 검색)
                    - “color” (“red”, “orange”, “yellow”, “light green”, “green”, “blue green”, “blue”, “navy”, “purple”, “red purple”, “achromatic”, "pink" 중 복수 선택, 일반 검색, requirements나 schedule에서 직/간접적으로 color를 제시하지 않았으면 사용 불가)
                    - “saturation” (“high”, “medium”, “low”, “achromatic” 중 복수 선택, 일반 검색)
                    - “brightness” (“white”, “high”, “medium”, “low”, “black” 중 복수 선택, 일반 검색)
                    - “pattern” (“stripe”, “check”, “flower”, “dot”, “patchwork”, “camouflage”, “paisley”, “tropical”, “hound tooth”, “herringbone”, “other pattern”, “plain” 중 복수 선택, 일반 검색)
                    - “season” (“spring”, “summer”, “autumn”, “winter” 중 복수 선택, 일반 검색)
                    - “tpo” (영어로 자유 입력, 벡터 검색)
                    - “detail1” (기타 속성을 영어로 자유 입력, 벡터 검색)
                    - “detail2”
                    - “detail3”
                    <top>
                    - “category” (“sweatshirt”, “hooded sweatshirt”, “shirt/blouse”, “t-shirt”, “knit”, 중 복수 선택, 일반 검색)
                    - “topLength” (“half”, “crop”, “regular”, “long” 중 복수 선택, 일반 검색)
                    - “sleeveLength” (“sleeveless”, “short sleeves”, “three-quarter sleeves”, “long sleeves” 중 복수 선택, 일반 검색)
                    - “fit” (“slim”, “regular”, “oversize” 중 복수 선택, 일반 검색)
                    - “print” (영어로 자유 입력, 벡터 검색)
                    - “isSeeThrough” (true, false 택 1, 일반 검색)
                    - “isSimple” (true, false 중 택 1, 일반 검색)
                    <pants>
                    - “category” (“denim pants”, “training pants”, “cotton pants”, “suit pants/slacks”, “leggings”, “jumpsuit/overall” 중 복수 선택, 일반 검색)
                    - “bottomLength” (“shorts”, “bermuda pants”, “capri pants”, “ankle pants”, “long pants” 중 복수 선택, 일반 검색)
                    - “pantsFit” (“wide”, “straight”, “tapered”, “slim/skinny”, “boot cut”, "baggy fit" “jogger fit” 중 복수 선택, 일반 검색)
                    <skirt>
                    - “skirtLength” (“mini skirt”, “midi skirt”, “long skirt” 중 복수 선택, 일반 검색)
                    - “skirtFit” (“a-line”, “h-line”, “balloon” "pencil" 중 중 복수 선택, 일반 검색)
                    - “skirtType” (“pleats”, “wrap”, “tiered”, “skirt pants”, “cancan”, "plain" 중 복수 선택, 일반 검색)
                    <dress>
                    - “skirtLength” (“mini skirt”, “midi skirt”, “long skirt” 중 복수 선택, 일반 검색)
                    - "skirtFit” (“a-line”, “h-line”, “balloon” "pencil" 중 복수 선택, 일반 검색)
                    - “sleeveLength” (“sleeveless”, “short sleeves”, “three-quarter sleeves”, “long sleeves” 중 복수 선택, 일반 검색)
                    - “fit” (“slim”, “regular”, “oversize” 중 복수 선택, 일반 검색)
                    - “skirtType” (“pleats”, “wrap”, “tiered”, “skirt pants”, “cancan”, "plain" 중 복수 선택, 일반 검색)
                    - “isTopRequired” (true, false 중 택 1, 일반 검색)
                    - “isSeeThrough” (true, false 택 1, 일반 검색)
                    <outerwear>
                    - “category” (“hooded zip-up”, “blouson/MA-1”, “leather/riders jacket”, “cardigan”, “trucker jacket”, “suit/blazer jacket”, “stadium jacket”, “nylon/coach jacket”, “anorak jacket”, “training jacket”, "season change coat", “safari/hunting jacket”, “padding”, “mustang/fur”, “fleece”, “winter coat”, “tweed jacket” 중 복수 선택, 일반 검색)
                    - “topLength” (“half”, “crop”, “regular”, “long” 중 복수 선택, 일반 검색)
                    - “sleeveLength” (“sleeveless”, “short sleeves”, “three-quarter sleeves”, “long sleeves” 중 복수 선택, 일반 검색)
                    - “fit” (“slim”, “regular”, “oversize” 중 복수 선택, 일반 검색)
                    - “isSeeThrough” (true, false 택 1, 일반 검색)
                    
                    만약 부합하는 baseJson이 없다면 꼭 포함해야 하는 옷(만약 있다면)과 minTemperature, schedule, requirements, crossoverCoordinationType을 참고하여 baseJson을 직접 생성하세요.
                    만약 부합하는 baseJson이 여러 개라면 그중 랜덤으로 하나를 선택하세요.
                    
                    'top1', 'top2', 'pants', 'skirt', 'dress', 'outerwear1', 'outerwear2', 'outerwear as top1', 'outerwear as top2' 중 하나를 이름으로, "type": (이름, 예: "top1")이 포함된 속성의 json을 값으로 하여 (만약 꼭 포함해야 하는 옷이라면 그 옷의 _id만 있는 json을 값으로 하여) json 형식으로 응답하세요.
                    만약 복수 선택 가능한 속성이라면 값을 배열로 설정하세요.
                    
                    baseJson이 null인 경우 부합하는 json이 없는 경우와 동일한 과정을 수행하세요. 다만 그 과정에서 minTemperature는 고려하지 않아도 됩니다.
                    
                    'outerwear1', 'outerwear2'에 대하여 'fit'을 slim이 아닌 모든 값의 배열로 설정하세요.
                    
                    만약 일교차가 10도 이상이라면 더워졌을 때 아우터를 벗을 수 있게 아우터가 포함된 baseJson을 선택 또는 생성하세요.
                    """, maxTemperature, minTemperature, schedule, requirements, coordinationRule, baseJsons, crossoverCoordinationType);
        }

        if(uniqueCoordinationType == UniqueCoordinationType.LAYERED_COORDINATION) {
            prompt = String.format("""
                    maxTemperature: %d
                    minTemperature: %d
                    schedule: %s
                    requirements: %s
                    coordinationRule: %s
                    baseJsons:
                    %s
                    
                    꼭 포함해야 하는 옷(만약 있다면)과 schedule, requirements에 가장 부합하는 baseJson을 선택하세요.
                    그리고 schedule과 requirements를 반영하여 baseJson에 속성이나 속성의 값을 추가 또는 삭제하세요. 이외에는 baseJson을 최대한 수정하지 마세요.
                    사용자가 무슨 
                    schedule이 있는 경우 style1 또는 tpo 속성을 추가하세요.
                    isSimple 속성은 requirements에서 요구했을 때나 상의끼리 레이어드할 때만 사용 가능합니다.
                    isSeeThrough 속성은 requirements에서 요구했을 때만 사용 가능합니다.
                    
                    반드시 레이어드 코디네이션을 활용하세요.
                    
                    top1과 dress를 레이어드할 때 top의 sleeveLength는 dress의 sleeveLength보다 길어야 합니다. 단, 기온도 고려해야 합니다.
                    
                    사용 가능한 속성들:
                    <top, pants, skirt, dress, outerwear 모두 사용 가능한 속성, 다만 json에서는 top, pants, skirt, dress, outerwear에 속해 있어야 함>
                    - "type" (필수 속성, "top", "pants", "skirt", "dress", "outerwear" 중 택 1)
                    - “style1” (첫번째로 눈에 띄는 style, 여러 의류 유형에서 중복 사용 가능, “casual”, “street”, “gorpcore”, “workwear”, “preppy”, “sporty”, “romantic”, “girlish”, “classic”. “minimal”, “chic”, “retro”, “ethnic”, “resort”, “balletcore” 중 택 1, 일반 검색)
                    - “style2” (두번째로 눈에 띄는 style, 여러 의류 유형에서 중복 사용 가능, “casual”, “street”, “gorpcore”, “workwear”, “preppy”, “sporty”, “romantic”, “girlish”, “classic”. “minimal”, “chic”, “retro”, “ethnic”, “resort”, “balletcore” 중 택 1, 일반 검색)
                    - “style3” (세번째로 눈에 띄는 style, 여러 의류 유형에서 중복 사용 가능, “casual”, “street”, “gorpcore”, “workwear”, “preppy”, “sporty”, “romantic”, “girlish”, “classic”. “minimal”, “chic”, “retro”, “ethnic”, “resort”, “balletcore” 중 택 1, 일반 검색)
                    - “color” (“red”, “orange”, “yellow”, “light green”, “green”, “blue green”, “blue”, “navy”, “purple”, “red purple”, “achromatic”, "pink" 중 복수 선택, 일반 검색, requirements나 schedule에서 직/간접적으로 color를 제시하지 않았으면 사용 불가)
                    - “saturation” (“high”, “medium”, “low”, “achromatic” 중 복수 선택, 일반 검색)
                    - “brightness” (“white”, “high”, “medium”, “low”, “black” 중 복수 선택, 일반 검색)
                    - “pattern” (“stripe”, “check”, “flower”, “dot”, “patchwork”, “camouflage”, “paisley”, “tropical”, “hound tooth”, “herringbone”, “other pattern”, “plain” 중 복수 선택, 일반 검색)
                    - “season” (“spring”, “summer”, “autumn”, “winter” 중 복수 선택, 일반 검색)
                    - “tpo” (영어로 자유 입력, 벡터 검색)
                    - “detail1” (기타 속성을 영어로 자유 입력, 벡터 검색)
                    - “detail2”
                    - “detail3”
                    <top>
                    - “category” (“sweatshirt”, “hooded sweatshirt”, “shirt/blouse”, “t-shirt”, “knit”, 중 복수 선택, 일반 검색)
                    - “topLength” (“half”, “crop”, “regular”, “long” 중 복수 선택, 일반 검색)
                    - “sleeveLength” (“sleeveless”, “short sleeves”, “three-quarter sleeves”, “long sleeves” 중 복수 선택, 일반 검색)
                    - “fit” (“slim”, “regular”, “oversize” 중 복수 선택, 일반 검색)
                    - “print” (영어로 자유 입력, 벡터 검색)
                    - “isSeeThrough” (true, false 택 1, 일반 검색)
                    - “isSimple” (true, false 중 택 1, 일반 검색)
                    <pants>
                    - “category” (“denim pants”, “training pants”, “cotton pants”, “suit pants/slacks”, “leggings”, “jumpsuit/overall” 중 복수 선택, 일반 검색)
                    - “bottomLength” (“shorts”, “bermuda pants”, “capri pants”, “ankle pants”, “long pants” 중 복수 선택, 일반 검색)
                    - “pantsFit” (“wide”, “straight”, “tapered”, “slim/skinny”("둘이 하나임"), “boot cut”, "baggy fit" “jogger fit” 중 복수 선택, 일반 검색)
                    <skirt>
                    - “skirtLength” (“mini skirt”, “midi skirt”, “long skirt” 중 복수 선택, 일반 검색)
                    - “skirtFit” (“a-line”, “h-line”, “balloon” "pencil" 중 중 복수 선택, 일반 검색)
                    - “skirtType” (“pleats”, “wrap”, “tiered”, “skirt pants”, “cancan”, "plain" 중 복수 선택, 일반 검색)
                    <dress>
                    - “skirtLength” (“mini skirt”, “midi skirt”, “long skirt” 중 복수 선택, 일반 검색)
                    - "skirtFit” (“a-line”, “h-line”, “balloon” "pencil" 중 복수 선택, 일반 검색)
                    - “sleeveLength” (“sleeveless”, “short sleeves”, “three-quarter sleeves”, “long sleeves” 중 복수 선택, 일반 검색)
                    - “fit” (“slim”, “regular”, “oversize” 중 복수 선택, 일반 검색)
                    - “skirtType” (“pleats”, “wrap”, “tiered”, “skirt pants”, “cancan”, "plain" 중 복수 선택, 일반 검색)
                    - “isSeeThrough” (true, false 택 1, 일반 검색)
                    <outerwear>
                    - “category” (“hooded zip-up”, “blouson/MA-1”, “leather/riders jacket”, “cardigan”, “trucker jacket”, “suit/blazer jacket”, “stadium jacket”, “nylon/coach jacket”, “anorak jacket”, “training jacket”, "season change coat", “safari/hunting jacket”, “padding”, “mustang/fur”, “fleece”, “winter coat”, “tweed jacket” 중 복수 선택, 일반 검색)
                    - “topLength” (“half”, “crop”, “regular”, “long” 중 복수 선택, 일반 검색)
                    - “sleeveLength” (“sleeveless”, “short sleeves”, “three-quarter sleeves”, “long sleeves” 중 복수 선택, 일반 검색)
                    - “fit” (“slim”, “regular”, “oversize” 중 복수 선택, 일반 검색)
                    - “isSeeThrough” (true, false 택 1, 일반 검색)
                    
                    만약 requirement에서 의류 구성/의류 유형을 명시했고 해당하는 의류 구성/의류 유형이 baseJsons에 없다면 꼭 포함해야 하는 옷(만약 있다면)과 minTemperature, schedule, requirements, crossoverCoordinationType을 참고하여 baseJson을 직접 생성하세요.
                    만약 부합하는 baseJson이 여러 개라면 그중 랜덤으로 하나를 선택하세요.
                    
                    'top1', 'top2', 'pants', 'skirt', 'dress', 'outerwear1', 'outerwear2', 'outerwear as top1', 'outerwear as top2' 중 하나를 이름으로, 속성의 json을 값으로 하여 (만약 꼭 포함해야 하는 옷이라면 그 옷의 _id만 있는 json을 값으로 하여) json 형식으로 응답하세요.
                    속성의 json에는 모든 옷에 대하여 "type": (이름, 예: "top1")을 반드시 포함하세요.
                    만약 복수 선택 가능한 속성이라면 값을 배열로 설정하세요.
                    
                    baseJson이 null인 경우 부합하는 json이 없는 경우와 동일한 과정을 수행하세요. 다만 그 과정에서 minTemperature는 고려하지 않아도 됩니다.
                    
                    'outerwear1', 'outerwear2'에 대하여 'fit'을 slim이 아닌 모든 값의 배열로 설정하세요.
                    
                    만약 일교차가 10도 이상이라면 더워졌을 때 아우터를 벗을 수 있게 아우터가 포함된 baseJson을 선택 또는 생성하세요.
                    """, maxTemperature, minTemperature, schedule, requirements, coordinationRule, baseJsons);
        }

        if(uniqueCoordinationType == UniqueCoordinationType.NO_UNIQUE_COORDINATION) {
            prompt = String.format("""
                    maxTemperature: %d
                    minTemperature: %d
                    schedule: %s
                    requirements: %s
                    coordinationRule: %s
                    baseJsons:
                    %s
                    
                    꼭 포함해야 하는 옷(만약 있다면)과 schedule, requirements에 가장 부합하는 baseJson을 선택하세요.
                    그리고 schedule과 requirements를 반영하여 baseJson에 속성을 추가하세요.
                    반드시 필요한 속성만 사용하세요. 속성이 너무 많으면 검색이 잘 안됩니다.
                    season이 아닌 복수 선택 가능한 속성에는 최대한 많은 값을 넣으세요. 값이 너무 적으면 검색이 잘 안됩니다.
                    
                    사용 가능한 속성들:
                    <공통>
                    - “style1” (“casual”, “street”, “gorpcore”, “workwear”, “preppy”, “sporty”, “romantic”, “girlish”, “classic”. “minimal”, “chic”, “retro”, “ethnic”, “resort”, “balletcore” 중 택 1, 일반 검색)
                    - “style2” (“casual”, “street”, “gorpcore”, “workwear”, “preppy”, “sporty”, “romantic”, “girlish”, “classic”. “minimal”, “chic”, “retro”, “ethnic”, “resort”, “balletcore” 중 택 1, 일반 검색)
                    - “style3” (“casual”, “street”, “gorpcore”, “workwear”, “preppy”, “sporty”, “romantic”, “girlish”, “classic”. “minimal”, “chic”, “retro”, “ethnic”, “resort”, “balletcore” 중 택 1, 일반 검색)
                    - “color” (“red”, “orange”, “yellow”, “light green”, “green”, “blue green”, “blue”, “navy”, “purple”, “red purple”, “achromatic”, "pink" 중 복수 선택, 일반 검색, requirements나 schedule에서 직/간접적으로 color를 제시하지 않았으면 사용 불가)
                    - “saturation” (“high”, “medium”, “low”, “achromatic” 중 복수 선택, 일반 검색)
                    - “brightness” (“white”, “high”, “medium”, “low”, “black” 중 복수 선택, 일반 검색)
                    - “pattern” (“stripe”, “check”, “flower”, “dot”, “patchwork”, “camouflage”, “paisley”, “tropical”, “hound tooth”, “herringbone”, “other pattern”, “plain” 중 복수 선택, 일반 검색)
                    - “season” (“spring”, “summer”, “autumn”, “winter” 중 복수 선택, 일반 검색)
                    - “tpo” (영어로 자유 입력, 벡터 검색)
                    - “detail1” (기타 속성을 영어로 자유 입력, 벡터 검색)
                    - “detail2”
                    - “detail3”
                    <top>
                    - “category” (“sweatshirt”, “hooded sweatshirt”, “shirt/blouse”, “t-shirt”, “knit”, 중 복수 선택, 일반 검색)
                    - “topLength” (“half”, “crop”, “regular”, “long” 중 복수 선택, 일반 검색)
                    - “sleeveLength” (“sleeveless”, “short sleeves”, “three-quarter sleeves”, “long sleeves” 중 복수 선택, 일반 검색)
                    - “fit” (“slim”, “regular”, “oversize” 중 복수 선택, 일반 검색)
                    - “print” (영어로 자유 입력, 벡터 검색)
                    - “isSeeThrough” (true, false 택 1, 일반 검색)
                    - “isSimple” (true, false 중 택 1, 일반 검색)
                    <pants>
                    - “category” (“denim pants”, “training pants”, “cotton pants”, “suit pants/slacks”, “leggings”, “jumpsuit/overall” 중 복수 선택, 일반 검색)
                    - “bottomLength” (“shorts”, “bermuda pants”, “capri pants”, “ankle pants”, “long pants” 중 복수 선택, 일반 검색)
                    - “pantsFit” (“wide”, “straight”, “tapered”, “slim/skinny”, “boot cut”, "baggy fit" “jogger fit” 중 복수 선택, 일반 검색)
                    <skirt>
                    - “skirtLength” (“mini skirt”, “midi skirt”, “long skirt” 중 복수 선택, 일반 검색)
                    - “skirtFit” (“a-line”, “h-line”, “balloon” "pencil" 중 중 복수 선택, 일반 검색)
                    - “skirtType” (“pleats”, “wrap”, “tiered”, “skirt pants”, “cancan”, "plain" 중 복수 선택, 일반 검색)
                    <dress>
                    - “skirtLength” (“mini skirt”, “midi skirt”, “long skirt” 중 복수 선택, 일반 검색)
                    - "skirtFit” (“a-line”, “h-line”, “balloon” "pencil" 중 복수 선택, 일반 검색)
                    - “sleeveLength” (“sleeveless”, “short sleeves”, “three-quarter sleeves”, “long sleeves” 중 복수 선택, 일반 검색)
                    - “fit” (“slim”, “regular”, “oversize” 중 복수 선택, 일반 검색)
                    - “skirtType” (“pleats”, “wrap”, “tiered”, “skirt pants”, “cancan”, "plain" 중 복수 선택, 일반 검색)
                    - “isTopRequired” (true, false 중 택 1, 일반 검색)
                    - “isSeeThrough” (true, false 택 1, 일반 검색)
                    <outerwear>
                    - “category” (“hooded zip-up”, “blouson/MA-1”, “leather/riders jacket”, “cardigan”, “trucker jacket”, “suit/blazer jacket”, “stadium jacket”, “nylon/coach jacket”, “anorak jacket”, “training jacket”, "season change coat", “safari/hunting jacket”, “padding”, “mustang/fur”, “fleece”, “winter coat”, “tweed jacket” 중 복수 선택, 일반 검색)
                    - “topLength” (“half”, “crop”, “regular”, “long” 중 복수 선택, 일반 검색)
                    - “sleeveLength” (“sleeveless”, “short sleeves”, “three-quarter sleeves”, “long sleeves” 중 복수 선택, 일반 검색)
                    - “fit” (“slim”, “regular”, “oversize” 중 복수 선택, 일반 검색)
                    - “isSeeThrough” (true, false 택 1, 일반 검색)
                    
                    만약 부합하는 baseJson이 없다면 꼭 포함해야 하는 옷(만약 있다면)과 minTemperature, schedule, requirements을 참고하여 baseJson을 직접 생성하세요.
                    
                    'top1', 'top2', 'pants', 'skirt', 'dress', 'outerwear1', 'outerwear2', 'outerwear as top1', 'outerwear as top2' 중 하나를 이름으로, "type": (이름, 예: "top1")이 포함된 속성의 json을 값으로 하여 (만약 꼭 포함해야 하는 옷이라면 그 옷의 _id만 있는 json을 값으로 하여) json 형식으로 응답하세요.
                    만약 복수 선택 가능한 속성이라면 값을 배열로 설정하세요.
                    
                    'outerwear1', 'outerwear2'에 대하여 'fit'을 slim이 아닌 모든 값의 배열로 설정하세요.
                    
                    baseJson이 null인 경우 부합하는 json이 없는 경우와 동일한 과정을 수행하세요. 다만 그 과정에서 minTemperature는 고려하지 않아도 됩니다.
                    
                    만약 일교차가 10도 이상이라면 더워졌을 때 아우터를 벗을 수 있게 아우터가 포함된 baseJson을 선택 또는 생성하세요.
                    
                    평범한 코디네이션으로 합니다.
                    """, maxTemperature, minTemperature, schedule, requirements, coordinationRule, baseJsons);
        }

        Map<ClothesKey, ClothesInfo> completeJson = coordinationService.getCompleteJson(necessaryClothesList, prompt);
        System.out.println(completeJson);

        Map<ClothesKey, List<ClothesFound>> clothesMap = new HashMap<>();

        Map<ClothesKey, ClothesDto> necessaryClothesMap = new HashMap<>();

        for (ClothesKey key : completeJson.keySet()) {
            ClothesInfo value = completeJson.get(key);

            String _id = value.get_id();

            if (_id != null) {
                for (ClothesDto clothes : necessaryClothesList) {
                    if (clothes.get_id().equals(_id)) {
                        necessaryClothesMap.put(key, clothes);

                        break;
                    }
                }
            } else {
                NotNullFields notNullFields = new NotNullFields(value.getNotNullStyleFields(), value.getNotNullListFields(), value.getNotNullBooleanFields());

                List<ClothesFound> clothesList = coordinationService.searchClothesList(key, value, notNullFields.getNotNullStyleFields(), notNullFields.getNotNullListFields(), notNullFields.getNotNullBooleanFields(), userId, wardrobeNames, useBasicWardrobe);

                while (clothesList.size() < 7) {

                    notNullFields = coordinationService.removeField(coordinationService.getCollection(key), notNullFields.getNotNullStyleFields(), notNullFields.getNotNullListFields(), notNullFields.getNotNullBooleanFields(), uniqueCoordinationType);

                    System.out.println(notNullFields);

                    clothesList = coordinationService.searchClothesList(key, value, notNullFields.getNotNullStyleFields(), notNullFields.getNotNullListFields(), notNullFields.getNotNullBooleanFields(), userId, wardrobeNames, useBasicWardrobe);

                    System.out.println(clothesList);
                }

                clothesMap.put(key, clothesList);
            }
        }

        System.out.println(clothesMap);

        System.out.println(necessaryClothesMap);

        if(uniqueCoordinationType == UniqueCoordinationType.PATTERN_ON_PATTERN) {
            prompt = String.format("""
                    첨부한 옷을 활용하여 3가지 옷차림을 생성하세요.
                    
                    아래 내용은 참고만 하세요.
                    maxTemperature: %d
                    minTemperature: %d
                    schedule: %s
                    requirements: %s
                    coordinationRule: %s
                    
                    ** 첨부된 옷을 종류 별로 하나씩 사용하세요. 예를 들어 만약 top1, pants, outerwear1을 첨부했다면 top1에서 하나, pants에서 하나, outerwear에서 하나를 사용합니다. 어울리는 옷을 찾을 수 없어도 무조건 그렇게 해야 합니다.
                    
                    ** url을 혼동하지 마세요. 예를 들어 top1의 url을 pants에 넣으면 안됩니다.
                    
                    ** 의류 타입을 변경하지 마세요. 예를 들어 skirt로 첨부한 이미지에 상의가 있다고 해서 top1으로 사용해서는 안됩니다.
                    
                    knit와 cardican을 조합하지 마세요.
                    안쪽에 입는 옷은 바깥쪽에 입는 옷보다 핏이 슬림해야 합니다.
                    만약 아우터의 핏이 안에 상의를 입지 못할 정도로 너무 slim하다면 top은 제외하세요.
                    
                    pattern on pattern 기법을 사용합니다.
                    
                    minTemperature 기준으로 코디하되 만약 일교차가 10도 이상이라면 더워졌을 때 아우터를 벗을 수 있게 아우터가 포함된 옷차림을 생성하세요.
                    
                    다음과 같은 json 형식으로 응답하세요.
                    {{
                      outfit1: {{
                        "title": "(옷차림 제목)",
                        "description": "(카탈로그처럼 세련된 200자 내외의 옷차림 설명)",
                        "clothes": {{
                            "top1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (top1을 첨부하진 않은 경우 생략 가능, 첨부한 경우에는 생략 불가)
                            "top2": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (top2이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear as top1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear_as_top1이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear as top2": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear_as_top2이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "pants": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (pants이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "dress": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (dress이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "skirt": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (skirt이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear1이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear2": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear2이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }}
                        }}
                      }}
                      outfit2: {{
                        ...
                      }},
                      outfit3: {{
                        ...
                      }}
                    }}
                    """, maxTemperature, minTemperature, schedule, requirements, coordinationRule);
        }

        if(uniqueCoordinationType == UniqueCoordinationType.CROSSOVER_COORDINATION) {
            prompt = String.format("""
                    첨부한 옷을 활용하여 3가지 옷차림을 생성하세요.
                    
                    아래 내용은 참고만 하세요.
                    maxTemperature: %d
                    minTemperature: %d
                    schedule: %s
                    requirements: %s
                    coordinationRule: %s
                    
                    ** 첨부된 옷을 종류 별로 하나씩 사용하세요. 예를 들어 만약 top1, pants, outerwear1을 첨부했다면 top1에서 하나, pants에서 하나, outerwear에서 하나를 사용합니다.
                    
                    ** url이 변형되지 않게 주의해주세요. 
                    
                    ** 의류 타입을 변경하지 마세요. 예를 들어 skirt로 첨부한 이미지에 상의가 있다고 해서 top1으로 사용해서는 안됩니다.
                    
                    knit와 cardican을 조합하지 마세요.
                    안쪽에 입는 옷은 바깥쪽에 입는 옷보다 핏이 슬림해야 합니다.
                    만약 아우터의 핏이 안에 상의를 입지 못할 정도로 너무 slim하다면 top은 제외하세요.
                    색상 조화를 고려해주세요. 단, 세 가지 이상의 옷에서 색이 같으면 안됩니다.
                    
                    크로스오버 코디네이션(서로 어울리지 않을 것 같은 이질적인 요소들을 조화롭게 결합하여 새로운 아름다움을 창조하는 코디네이션 기법)을 구현해주세요.
                   
                    minTemperature 기준으로 코디하되 만약 일교차가 10도 이상이라면 더워졌을 때 아우터를 벗을 수 있게 아우터가 포함된 옷차림을 생성하세요.
                    
                    다음과 같은 json 형식으로 응답하세요.
                    {{
                      outfit1: {{
                        "title": "(옷차림 제목)",
                        "description": "(카탈로그처럼 세련된 200자 내외의 옷차림 설명)",
                        "clothes": {{
                            "top1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (top1을 첨부하진 않은 경우 생략 가능, 첨부한 경우에는 생략 불가)
                            "top2": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (top2이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear as top1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear_as_top1이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear as top2": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear_as_top2이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "pants": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (pants이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "dress": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (dress이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "skirt": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (skirt이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear1이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear2": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear2이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }}
                        }}
                      }}
                      outfit2: {{
                        ...
                      }},
                      outfit3: {{
                        ...
                      }}
                    }}
                    """, maxTemperature, minTemperature, schedule, requirements, coordinationRule);
        }

        if(uniqueCoordinationType == UniqueCoordinationType.LAYERED_COORDINATION) {
            prompt = String.format("""
                    첨부한 옷을 활용하여 3가지 옷차림을 생성하세요.
                    
                    아래 내용은 참고만 하세요.
                    maxTemperature: %d
                    minTemperature: %d
                    schedule: %s
                    requirements: %s
                    coordinationRule: %s
                    
                    ** 첨부된 옷을 종류 별로 하나씩 사용하세요. 예를 들어 만약 top1, pants, outerwear1을 첨부했다면 top1에서 하나, pants에서 하나, outerwear에서 하나를 사용합니다.
                    
                    ** url을 혼동하지 마세요. 예를 들어 top1의 url을 pants에 넣으면 안됩니다.
                    
                    ** 의류 타입을 변경하지 마세요. 예를 들어 skirt로 첨부한 이미지에 상의가 있다고 해서 top1으로 사용해서는 안됩니다.
                    
                    ** pants과 skirt가 함께 첨부되어 있으면 skirt는 단독으로 입을 수 없는 랩스커트이고 두 가지를 같이 입으라는 의미이므로 반드시 모든 옷차림에 pants과 skirt를 포함하세요. 중요합니다.
                    ** pants과 dress, top1이 함께 첨부되어 있으면 세 가지를 같이 입으라는 의미이므로 반드시 모든 옷차림에 pants과 dress, top1을 포함하세요.
                    ** top1과 top2가 함께 첨부되어 있다면 두 가지를 레이어드해서 입으라는 의미이므로 반드시 모든 옷차림에 top1과 top2을 포함하세요.
                    
                    knit와 cardican을 조합하지 마세요.
                    안쪽에 입는 옷은 바깥쪽에 입는 옷보다 핏이 슬림해야 합니다.
                    만약 아우터의 핏이 안에 상의를 입지 못할 정도로 너무 slim하다면 top은 제외하세요.
                    만약 랩스커트가 시스루가 아니라면 최대한 바지와 랩스커트의 색상을 맞춰주세요.
                    최대한 원피스의 길이는 바지의 길이보다 짧도록 해주세요.
                    최대한 바지의 길이는 랩스커트의 길이보다 길도록 해주세요.
                    최대한 원피스 길이는 치마바지의 치마 길이보다 짧도록 해주세요.
                    치마(랩스커트 포함)와 치마바지를 조합하지 마세요.
                    원피스와 치마바지를 조합하지 마세요.
                    
                    top1이 긴소매이면 top2는 반소매여야 합니다. 중요합니다.
                    
                    반드시 레이어드 코디네이션을 활용하세요.
                    
                    minTemperature 기준으로 코디하되 만약 일교차가 10도 이상이라면 더워졌을 때 아우터를 벗을 수 있게 아우터가 포함된 옷차림을 생성하세요.
                    
                    다음과 같은 json 형식으로 응답하세요.
                    {{
                      outfit1: {{
                        "title": "(옷차림 제목)",
                        "description": "(카탈로그처럼 세련된 200자 내외의 옷차림 설명)",
                        "clothes": {{
                            "top1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (top1을 첨부하진 않은 경우 생략 가능, 첨부한 경우에는 생략 불가)
                            "top2": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (top2이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear as top1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear_as_top1이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear as top2": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear_as_top2이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "pants": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (pants이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "dress": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (dress이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "skirt": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (skirt이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear1이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear2": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear2이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }}
                        }}
                      }}
                      outfit2: {{
                        ...
                      }},
                      outfit3: {{
                        ...
                      }}
                    }}
                    """, maxTemperature, minTemperature, schedule, requirements, coordinationRule);
        }

        if(uniqueCoordinationType == UniqueCoordinationType.NO_UNIQUE_COORDINATION) {
            prompt = String.format("""
                    첨부한 옷을 활용하여 3가지 옷차림을 생성하세요.
                    
                    아래 내용은 참고만 하세요.
                    maxTemperature: %d
                    minTemperature: %d
                    schedule: %s
                    requirements: %s
                    coordinationRule: %s
                    
                    ** 첨부된 옷을 종류 별로 하나씩 사용하세요. 예를 들어 만약 top1, pants, outerwear1을 첨부했다면 top1에서 하나, pants에서 하나, outerwear에서 하나를 사용합니다.
                    
                    ** url을 혼동하지 마세요. 예를 들어 top1의 url을 pants에 넣으면 안됩니다.
                    
                    ** 의류 타입을 변경하지 마세요. 예를 들어 skirt로 첨부한 이미지에 상의가 있다고 해서 top1으로 사용해서는 안됩니다.
                    
                    knit와 cardican을 조합하지 마세요.
                    안쪽에 입는 옷은 바깥쪽에 입는 옷보다 핏이 슬림해야 합니다.
                    만약 아우터의 핏이 안에 상의를 입지 못할 정도로 너무 slim하다면 top은 제외하세요.
                    
                    평범한 코디네이션으로 합니다.
                    
                    minTemperature 기준으로 코디하되 만약 일교차가 10도 이상이라면 더워졌을 때 아우터를 벗을 수 있게 아우터가 포함된 옷차림을 생성하세요.
                    
                    다음과 같은 json 형식으로 응답하세요.
                    {{
                      outfit1: {{
                        "title": "(옷차림 제목)",
                        "description": "(카탈로그처럼 세련된 200자 내외의 옷차림 설명)",
                        "clothes": {{
                            "top1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (top1을 첨부하진 않은 경우 생략 가능, 첨부한 경우에는 생략 불가)
                            "top2": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (top2이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear as top1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear_as_top1이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear as top2": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear_as_top2이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "pants": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (pants이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "dress": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (dress이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "skirt": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (skirt이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear1이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }},
                            "outerwear2": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl)}}, (outerwear2이 첨부되지 않은 경우 생략 가능, 첨부한 경우에는 생략 불가) }}
                        }}
                      }}
                      outfit2: {{
                        ...
                      }},
                      outfit3: {{
                        ...
                      }}
                    }}
                    """, maxTemperature, minTemperature, schedule, requirements, coordinationRule);
        }

        Map<OutfitKey, OutfitDto> outfits = coordinationService.getOutfits(necessaryClothesMap, clothesMap, prompt);
        System.out.println(outfits);

        return ResponseEntity.ok(outfits);
    }
    @PostMapping("/feedback")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> feedbackOutfit(@RequestBody feedbackRequest request) throws IOException {

        Map<ClothesKey, String> outfit = request.getOutfit();

        String feedback = request.getFeedback();

        String userId = getCurrentUserId();

        String coordinationRule = coordinationService.getCoordinationRule(userId);

        String newCoordinationRule = coordinationService.addFeedback(outfit, feedback, coordinationRule);

        System.out.println(newCoordinationRule);

        coordinationService.setCoordinationRule(userId, newCoordinationRule);

        return ResponseEntity.ok().build();
    }
}

