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
                    coordinationRule: %s
                    baseJsons:
                    %s
                    
                    꼭 포함해야 하는 옷(만약 있다면)과 schedule, requirements, coordinationRule에 가장 부합하는 baseJson을 선택하세요.
                    그리고 schedule과 requirements, coordinationRule을 반영하여 선택한 baseJson에 속성이나 속성의 값을 추가/삭제하세요.
                    만약 schedule이 있다면 style1이나 tpo을 사용하세요.
                    requirements에서 무엇을 요구하는지 차근차근 생각해보세요.
                    꼭 필요한 속성만 사용하세요.
                    
                    사용 가능한 속성:
                    <공통>
                    - "type" (필수, 'top1', 'top2', 'pants', 'skirt', 'dress', 'outerwear1', 'outerwear2', 'outerwear as top1', 'outerwear as top2' 중 택 1)
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
                    - “category” (“sweatshirt”, “hooded sweatshirt”, “shirt/blouse”(둘이 하나임), “t-shirt”, “knit”, 중 복수 선택, 일반 검색)
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
                    
                    만약 requirements에서 의류 구성을 명시했고, 해당하는 의류 구성이 baseJsons에 존재하지 않는다면 꼭 포함해야 하는 옷(만약 있다면)과 minTemperature, schedule, requirements, coordinationRule을 참고하여 baseJson을 직접 생성하세요.
                    
                    만약 일교차가 10도 이상이라면 더워졌을 때 아우터를 벗을 수 있게 아우터가 포함된 baseJson을 선택/생성하세요.
                    
                    'top1', 'top2', 'pants', 'skirt', 'dress', 'outerwear1', 'outerwear2', 'outerwear as top1', 'outerwear as top2' 중 하나를 이름으로, 속성의 json을 값으로 하여 (만약 꼭 포함해야 하는 옷이라면 _id의 json을 값으로 하여) json 형식으로 응답하세요.
                    만약 복수 선택 가능한 속성이라면 값을 배열로 설정하세요.
                    
                    응답 예시:
                    {
                      "top1": {
                        "type": "top",
                        "category": ["t-shirt"],
                        "sleeveLength": ["long sleeves"],
                        "style1": "romantic",
                        "tpo": "date"
                      },
                      "pants": {
                        "type": "pants",
                        "season": ["spring"],
                        "bottomLength": ["ankle pants", "long pants"]
                      }
                    }
                    
                    모든 의류 우형에 대하여 "pattern"을 "plain"이 아닌 모든 값의 배열로 설정하세요.
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
                    
                    꼭 포함해야 하는 옷(만약 있다면)과 schedule, requirements, coordinationRule, crossoverCoordinationType에 가장 부합하는 baseJson을 선택하세요.
                    그리고 schedule과 requirements, coordinationRule, crossoverCoordinationType을 반영하여 선택한 baseJson에 속성이나 속성의 값을 추가/삭제하세요.
                    만약 schedule이 있다면 style1이나 tpo을 사용하세요.
                    requirements에서 무엇을 요구하는지 차근차근 생각해보세요.
                    꼭 필요한 속성만 사용하세요.
                    
                    사용 가능한 속성:
                    <공통>
                    - "type" (필수, 'top1', 'top2', 'pants', 'skirt', 'dress', 'outerwear1', 'outerwear2', 'outerwear as top1', 'outerwear as top2' 중 택 1)
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
                    - “category” (“sweatshirt”, “hooded sweatshirt”, “shirt/blouse”(둘이 하나임), “t-shirt”, “knit”, 중 복수 선택, 일반 검색)
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
                    
                    만약 requirements에서 의류 구성을 명시했고, 해당하는 의류 구성이 baseJsons에 존재하지 않는다면 꼭 포함해야 하는 옷(만약 있다면)과 minTemperature, schedule, requirements, coordinationRule, crossoverCoordinationType을 참고하여 baseJson을 직접 생성하세요.
                    
                    만약 일교차가 10도 이상이라면 더워졌을 때 아우터를 벗을 수 있게 아우터가 포함된 baseJson을 선택/생성하세요.
                    
                    'top1', 'top2', 'pants', 'skirt', 'dress', 'outerwear1', 'outerwear2', 'outerwear as top1', 'outerwear as top2' 중 하나를 이름으로, 속성의 json을 값으로 하여 (만약 꼭 포함해야 하는 옷이라면 _id의 json을 값으로 하여) json 형식으로 응답하세요.
                    만약 복수 선택 가능한 속성이라면 값을 배열로 설정하세요.
                    
                    응답 예시:
                    {
                      "top1": {
                        "type": "top",
                        "category": ["t-shirt"],
                        "sleeveLength": ["long sleeves"],
                        "style1": "romantic",
                        "tpo": "date"
                      },
                      "pants": {
                        "type": "pants",
                        "season": ["spring"],
                        "bottomLength": ["ankle pants", "long pants"]
                      }
                    }
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
                    
                    꼭 포함해야 하는 옷(만약 있다면)과 schedule, requirements, coordinationRule에 가장 부합하는 baseJson을 선택하세요.
                    그리고 schedule과 requirements, coordinationRule을 반영하여 선택한 baseJson에 속성이나 속성의 값을 추가/삭제하세요.
                    만약 schedule이 있다면 style1이나 tpo을 사용하세요.
                    requirements에서 무엇을 요구하는지 차근차근 생각해보세요.
                    반드시 레이어드 코디네이션을 활용하세요.
                    꼭 필요한 속성만 사용하세요.
                    
                    사용 가능한 속성:
                    <공통>
                    - "type" (필수, 'top1', 'top2', 'pants', 'skirt', 'dress', 'outerwear1', 'outerwear2', 'outerwear as top1', 'outerwear as top2' 중 택 1)
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
                    - “category” (“sweatshirt”, “hooded sweatshirt”, “shirt/blouse”(둘이 하나임), “t-shirt”, “knit”, 중 복수 선택, 일반 검색)
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
                    
                    만약 requirements에서 의류 구성을 명시했고, 해당하는 의류 구성이 baseJsons에 존재하지 않는다면 꼭 포함해야 하는 옷(만약 있다면)과 minTemperature, schedule, requirements, coordinationRule을 참고하여 baseJson을 직접 생성하세요.
                    
                    만약 일교차가 10도 이상이라면 더워졌을 때 아우터를 벗을 수 있게 아우터가 포함된 baseJson을 선택/생성하세요.
                    
                    'top1', 'top2', 'pants', 'skirt', 'dress', 'outerwear1', 'outerwear2', 'outerwear as top1', 'outerwear as top2' 중 하나를 이름으로, 속성의 json을 값으로 하여 (만약 꼭 포함해야 하는 옷이라면 _id의 json을 값으로 하여) json 형식으로 응답하세요.
                    만약 복수 선택 가능한 속성이라면 값을 배열로 설정하세요.
                    
                    응답 예시:
                    {
                      "top1": {
                        "type": "top",
                        "category": ["t-shirt"],
                        "sleeveLength": ["long sleeves"],
                        "style1": "romantic",
                        "tpo": "date"
                      },
                      "pants": {
                        "type": "pants",
                        "season": ["spring"],
                        "bottomLength": ["ankle pants", "long pants"]
                      }
                    }
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
                    
                    꼭 포함해야 하는 옷(만약 있다면)과 schedule, requirements, coordinationRule에 가장 부합하는 baseJson을 선택하세요.
                    그리고 schedule과 requirements, coordinationRule을 반영하여 선택한 baseJson에 속성이나 속성의 값을 추가/삭제하세요.
                    만약 schedule이 있다면 style1이나 tpo을 사용하세요.
                    requirements에서 무엇을 요구하는지 차근차근 생각해보세요.
                    평범한 코디네이션을 적용하세요.
                    꼭 필요한 속성만 사용하세요.
                    
                    사용 가능한 속성:
                    <공통>
                    - "type" (필수, 'top1', 'top2', 'pants', 'skirt', 'dress', 'outerwear1', 'outerwear2', 'outerwear as top1', 'outerwear as top2' 중 택 1)
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
                    - “category” (“sweatshirt”, “hooded sweatshirt”, “shirt/blouse”(둘이 하나임), “t-shirt”, “knit”, 중 복수 선택, 일반 검색)
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
                    
                    만약 requirements에서 의류 구성을 명시했고, 해당하는 의류 구성이 baseJsons에 존재하지 않는다면 꼭 포함해야 하는 옷(만약 있다면)과 minTemperature, schedule, requirements, coordinationRule을 참고하여 baseJson을 직접 생성하세요.
                    
                    만약 일교차가 10도 이상이라면 더워졌을 때 아우터를 벗을 수 있게 아우터가 포함된 baseJson을 선택/생성하세요.
                    
                    'top1', 'top2', 'pants', 'skirt', 'dress', 'outerwear1', 'outerwear2', 'outerwear as top1', 'outerwear as top2' 중 하나를 이름으로, 속성의 json을 값으로 하여 (만약 꼭 포함해야 하는 옷이라면 _id의 json을 값으로 하여) json 형식으로 응답하세요.
                    만약 복수 선택 가능한 속성이라면 값을 배열로 설정하세요.
                    
                    응답 예시:
                    {
                      "top1": {
                        "type": "top",
                        "category": ["t-shirt"],
                        "sleeveLength": ["long sleeves"],
                        "style1": "romantic",
                        "tpo": "date"
                      },
                      "pants": {
                        "type": "pants",
                        "season": ["spring"],
                        "bottomLength": ["ankle pants", "long pants"]
                      }
                    }
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
                    
                    ** 첨부된 옷을 유형별로 하나씩 사용하세요. 예를 들어 만약 top1, pants, outerwear1을 첨부했다면 top1에서 하나, pants에서 하나, outerwear에서 하나를 사용합니다.
                    ** 꼭 포함해야 하는 옷이 아닌 경우, 같은 옷은 세 가지 옷차림을 통틀어 한 번만 사용할 수 있습니다.
                    
                    knit와 cardican을 조합하지 마세요.
                    반드시 패턴 온 패턴 기법을 활용하세요.
                    셔츠/블라우스를 니트와 레이어드 할 때는 셔츠/블라우스 중 셔츠를 사용하고 더 이상 사용할 셔츠가 없는 경우에만 블라우스를 사용하세요.
                   
                    다음과 같은 json 형식으로 응답하세요.
                    {{
                      outfit1: {{
                        "title": "(옷차림 제목)",
                        "description": "(카탈로그처럼 세련된 200자 내외의 옷차림 설명)",
                        "clothes": {{
                            "top1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl), text: (옷의 설명)}},
                            ...
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
                    
                    ** 첨부된 옷을 유형별로 하나씩 사용하세요. 예를 들어 만약 top1, pants, outerwear1을 첨부했다면 top1에서 하나, pants에서 하나, outerwear에서 하나를 사용합니다.
                    ** 꼭 포함해야 하는 옷이 아닌 경우, 같은 옷은 세 가지 옷차림을 통틀어 한 번만 사용할 수 있습니다.
                    
                    knit와 cardican을 조합하지 마세요.
                    반드시 크로스오버 코디네이션을 활용하세요.
                    셔츠/블라우스를 니트와 레이어드 할 때는 셔츠/블라우스 중 셔츠를 사용하고 더 이상 사용할 셔츠가 없는 경우에만 블라우스를 사용하세요.
                   
                    다음과 같은 json 형식으로 응답하세요.
                    {{
                      outfit1: {{
                        "title": "(옷차림 제목)",
                        "description": "(카탈로그처럼 세련된 200자 내외의 옷차림 설명)",
                        "clothes": {{
                            "top1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl), text: (옷의 설명)}},
                            ...
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
                    
                    ** 하나의 옷차림을 생성할 때 모든 의류 유형을 한 번씩은 꼭 사용해야 합니다. 예를 들어 만약 top1, top2, pants를 첨부했다면 top1에서 하나, top2에서 하나, pants에서 하나를 사용합니다. top1만 사용하고 top2는 생략하는 것은 안됩니다.
                    ** 꼭 포함해야 하는 옷이 아닌 경우, 같은 옷은 세 가지 옷차림을 통틀어 한 번만 사용할 수 있습니다.
                    ** 만약 top1과 top2를 함께 첨부했다면 두 가지를 같이 입으라는 의미입니다. 절대로 둘 중 하나를 생략하지 마세요. 만약 skirt과 pants를 함께 첨부했다면 두 가지를 같이 입으라는 의미입니다. 절대로 둘 중 하나를 생략하지 마세요. 만약 dress과 skirt를 함께 첨부했다면 두 가지를 같이 입으라는 의미입니다. 절대로 둘 중 하나를 생략하지 마세요.
                    
                    knit와 cardican을 조합하지 마세요.
                    반드시 레이어드 코디네이션을 활용하세요.
                    셔츠/블라우스를 터틀넥/하프넥 니트과 조합하지 마세요.
                    원피스, 스커트와 치마바지를 조합하지 마세요.
                    만약 랩스커트가 시스루가 아니라면 최대한 바지와 랩스커트의 색상을 일치시켜 주세요.
                    최대한 원피스, 스커트의 길이가 바지의 길이보다 짧게 해주세요.
                    셔츠/블라우스를 니트와 레이어드 할 때는 셔츠/블라우스 중 셔츠를 사용하고 더 이상 사용할 셔츠가 없는 경우에만 블라우스를 사용하세요.
                   
                    다음과 같은 json 형식으로 응답하세요.
                    {{
                      outfit1: {{
                        "title": "(옷차림 제목)",
                        "description": "(카탈로그처럼 세련된 200자 내외의 옷차림 설명)",
                        "clothes": {{
                            "top1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl), text: (옷의 설명)}}(top1을 첨부하지 않은 경우 생략 가능),
                            ...
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
                    
                    ** 첨부된 옷을 유형별로 하나씩 사용하세요. 예를 들어 만약 top1, pants, outerwear1을 첨부했다면 top1에서 하나, pants에서 하나, outerwear에서 하나를 사용합니다.
                    ** 꼭 포함해야 하는 옷이 아닌 경우, 같은 옷은 세 가지 옷차림을 통틀어 한 번만 사용할 수 있습니다.
                    
                    knit와 cardican을 조합하지 마세요.
                    반드시 평범한 코디네이션을 적용하세요.
                    셔츠/블라우스를 니트와 레이어드 할 때는 셔츠/블라우스 중 셔츠를 사용하고 더 이상 사용할 셔츠가 없는 경우에만 블라우스를 사용하세요.
                   
                    다음과 같은 json 형식으로 응답하세요.
                    {{
                      outfit1: {{
                        "title": "(옷차림 제목)",
                        "description": "(카탈로그처럼 세련된 200자 내외의 옷차림 설명)",
                        "clothes": {{
                            "top1": {{(imageUrl: (옷의 imageUrl), productUrl: (옷의 productUrl), text: (옷의 설명)}},
                            ...
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

        for (OutfitDto od : outfits.values()) {
            Map<ClothesKey, Map<String, String>> clothes = od.getClothes();

            for (Map<String, String> oneClothes : clothes.values()) {
                if (oneClothes.get("productUrl") != null) {
                    oneClothes.put("imageUrl", coordinationService.getClothes(oneClothes.get("productUrl")).getImageUrl());
                }
            }
        }

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

