package team.suajung.ad.ress.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import team.suajung.ad.ress.digital_wardrobe.repository2.ItemRepository;
import team.suajung.ad.ress.dto.*;
import team.suajung.ad.ress.enums.*;
import team.suajung.ad.ress.repository1.*;
import team.suajung.ad.ress.util.OpenAIClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoordinationService {

    @Autowired
    private OpenAIClient openAIClient;

    @Autowired
    @Qualifier("mongoTemplate1")
    private MongoTemplate mongoTemplate1;

    @Autowired
    @Qualifier("mongoTemplate2")
    private MongoTemplate mongoTemplate2;

    @Autowired
    private TopRepository topRepository;

    @Autowired
    private PantsRepository pantsRepository;

    @Autowired
    private DressRepository dressRepository;

    @Autowired
    private SkirtRepository skirtRepository;

    @Autowired
    private OuterwearRepository outerwearRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CoordinationFavoriteRepository coordinationFavoriteRepository;

    @Autowired
    private CoordinationRuleRepository coordinationRuleRepository;

    public String getBaseJsons(Integer temperature, UniqueCoordinationType uniqueCoordinationType) throws IOException {

        if (temperature == null){
            return null;
        }

        int count;

        String degree;

        if (uniqueCoordinationType == UniqueCoordinationType.LAYERED_COORDINATION) {
            if (temperature >= 28) {
                count = 5;
                degree = "28";
            } else if (temperature >= 23) {
                count = 7;
                degree = "23";
            } else if (temperature >= 20) {
                count = 15;
                degree = "20";
            } else if (temperature >= 17) {
                count = 14;
                degree = "17";
            } else if (temperature >= 12) {
                count = 14;
                degree = "12";
            } else if (temperature >= 9) {
                count = 13;
                degree = "9";
            }  else {
                count = 12;
                degree = "Else";
            }

            StringBuilder baseJsons = new StringBuilder();

            for (int i = 0; i < count; i++) {
                if (i != 0) baseJsons.append("\nor\n");

                String path = "src/main/resources/layeredBaseJson/layeredBaseJson" + degree + "Degrees/layeredBaseJson" + degree + "Degrees" + (i + 1) + ".json";

                String baseJson = Files.readString(Paths.get(path));

                baseJsons.append(baseJson);
            }

            return baseJsons.toString();
        } else {
            if (temperature >= 28) {
                count = 4;
                degree = "28";
            } else if (temperature >= 23) {
                count = 7;
                degree = "23";
            } else if (temperature >= 20) {
                count = 11;
                degree = "20";
            }
            else if (temperature >= 17) {
                count = 10;
                degree = "17";
            } else if (temperature >= 12) {
                count = 12;
                degree = "12";
            } else if (temperature >= 9) {
                count = 12;
                degree = "9";
            } else {
                count = 11;
                degree = "Else";
            }

            StringBuilder baseJsons = new StringBuilder();

            for (int i = 0; i < count; i++) {
                if (i != 0) baseJsons.append("\nor\n");

                String path = "src/main/resources/baseJson/baseJson" + degree + "Degrees/baseJson" + degree + "Degrees" + (i + 1) + ".json";

                String baseJson = Files.readString(Paths.get(path));

                baseJsons.append(baseJson);
            }

            return baseJsons.toString();
        }
    }

    public ClothesDto getNecessaryClothes(String necessaryClothes_id) {

        TopDto top = topRepository.findById(necessaryClothes_id).orElse(null);

        if (top != null) return top;

        PantsDto pants = pantsRepository.findById(necessaryClothes_id).orElse(null);

        if (pants != null) return pants;

        DressDto dress = dressRepository.findById(necessaryClothes_id).orElse(null);

        if (dress != null) return dress;

        SkirtDto skirt = skirtRepository.findById(necessaryClothes_id).orElse(null);

        if (skirt != null) return skirt;

        OuterwearDto outerwear = outerwearRepository.findById(necessaryClothes_id).orElse(null);

        if (outerwear != null) return outerwear;

        return itemRepository.findById(necessaryClothes_id).orElse(null);
    }

    public ClothesDto getClothes(String productUrl) {

        TopDto top = topRepository.findByProductUrl(productUrl).orElse(null);

        if (top != null) return top;

        PantsDto pants = pantsRepository.findByProductUrl(productUrl).orElse(null);

        if (pants != null) return pants;

        DressDto dress = dressRepository.findByProductUrl(productUrl).orElse(null);

        if (dress != null) return dress;

        SkirtDto skirt = skirtRepository.findByProductUrl(productUrl).orElse(null);

        if (skirt != null) return skirt;

        OuterwearDto outerwear = outerwearRepository.findByProductUrl(productUrl).orElse(null);

        if (outerwear != null) return outerwear;

        return itemRepository.findByProductUrl(productUrl).orElse(null);
    }

    public List<ClothesDto> getNecessaryClothesList(List<String> necessaryClothes_ids) {

        List<ClothesDto> necassaryClothesList = new ArrayList<>();

        for (String _id : necessaryClothes_ids) {
            ClothesDto necessaryClothes = getNecessaryClothes(_id);

            necassaryClothesList.add(necessaryClothes);
        }

        return necassaryClothesList;
    }

    public List<String> getCrossoverCoordinationType(List<ClothesDto> necessaryClothesList, String prompt) throws IOException {

        List<Map<String, Object>> content = new ArrayList<>();

        for (ClothesDto clothes : necessaryClothesList) {

            String clothesType = clothes.getClass().getName();

            content.add(Map.of("type", "text", "text", "다음은 꼭 포함해야 하는 " + clothesType + "입니다."));

            content.add(Map.of("type", "image_url", "image_url", Map.of("url", clothes.getImageUrl())));
        }

        content.add(Map.of("type", "text", "text", prompt));

        int max_attempt = 10;

        int attempt = 0;

        while (attempt < max_attempt) {
            try {
                JsonNode result = openAIClient.chatCompletions(content);

                ObjectMapper mapper = new ObjectMapper();

                Map<String, List<String>> crossoverCoordinationType = mapper.readValue(result.toString(), new TypeReference<>() {});

                if (crossoverCoordinationType.get("crossoverCoordinationMethod") == null) {
                    throw new Exception();
                }

                return crossoverCoordinationType.get("crossoverCoordinationMethod");
            } catch (Exception e) {
                attempt++;

                System.out.println(e.toString());
            }
        }

        return List.of();
    }

    public Map<ClothesKey, ClothesInfo> getCompleteJson(List<ClothesDto> necessaryClothesList, String prompt) throws IOException {

        List<Map<String, Object>> content = new ArrayList<>();

        for (ClothesDto clothes : necessaryClothesList) {

            String clothesType = clothes.getClass().getName();

            content.add(Map.of("type", "text", "text", "다음은 꼭 포함해야 하는 " + clothesType + "입니다."));

            content.add(Map.of("type", "image_url", "image_url", Map.of("url", clothes.getImageUrl())));

            content.add(Map.of("type", "text", "text", "이 옷의 _id는 " + clothes.get_id() + "입니다."));
        }

        content.add(Map.of("type", "text", "text", prompt));

        int max_attempt = 30;

        int attempt = 0;

        while (attempt < max_attempt) {
            try {
                JsonNode result = openAIClient.chatCompletions(content);

                ObjectMapper mapper = new ObjectMapper();

                Map<String, ClothesInfo> clothesInfosTemp = mapper.readValue(result.toString(), new TypeReference<>() {});

                Map<ClothesKey, ClothesInfo> clothesInfos = new HashMap<>();

                for (Map.Entry<String, ClothesInfo> entry : clothesInfosTemp.entrySet()) {
                    ClothesKey clotheskey = ClothesKey.from(entry.getKey());

                    clothesInfos.put(clotheskey, entry.getValue());
                }

                return clothesInfos;
            } catch (Exception e) {
                attempt++;

                System.out.println(e.toString());
            }
        }

        return Map.of();
    }

    public List<Document> getNotNullCommonFieldsPipeline (List<Document> pipeline, Map<String, Style> notNullStyleFields, Map<String, List<Attribute>> notNullListFields, Map<String, Boolean> notNullBooleanFields) {

        if (!notNullStyleFields.isEmpty()) {
            for (String key : notNullStyleFields.keySet()) {
                String jsonValue = notNullStyleFields.get(key).getJsonValue();

                List<Document> styleConditions = List.of(
                        new Document("style1", jsonValue),
                        new Document("style2", jsonValue),
                        new Document("style3", jsonValue)
                );

                pipeline.add(new Document("$match", new Document("$or", styleConditions)));
            }
        }

        for (String key : notNullListFields.keySet()) {
            List<String> jsonValues = notNullListFields.get(key).stream()
                    .map(Attribute::getJsonValue)
                    .toList();

            pipeline.add(new Document("$match", new Document(key, new Document("$in", jsonValues))));
        }

        for (String key : notNullBooleanFields.keySet()) {
            pipeline.add(new Document("$match", new Document(key,notNullBooleanFields.get(key))));
        }

        return pipeline;
    }

    public List<Document> getVectorSearchResult(ClothesKey key, String path, List<Double> queryVector, Map<String, Style> notNullStyleFields, Map<String, List<Attribute>> notNullListFields, Map<String, Boolean> notNullBooleanFields, String userId, List<String> wardrobeNames, Boolean useBasicWardrobe) {

        List<Document> pipeline = new ArrayList<>();

        List<Document> result = new ArrayList<>();

        if (useBasicWardrobe) {
            Document vectorSearch = new Document("$vectorSearch", new Document("index", getCollection(key) + "_vector_index")
                    .append("path", path)
                    .append("queryVector", queryVector)
                    .append("numCandidates", 1000)
                    .append("limit", 1000));

            pipeline.add(vectorSearch);

            pipeline = getNotNullCommonFieldsPipeline(pipeline, notNullStyleFields, notNullListFields, notNullBooleanFields);

            pipeline.add(new Document("$match", new Document("userId", null)));

            Document project = new Document("score", new Document("$meta", "vectorSearchScore"));

            pipeline.add(new Document("$project", project));

            try (MongoCursor<Document> cursor = mongoTemplate1.getCollection(getCollection(key)).aggregate(pipeline).iterator()) {
                while (cursor.hasNext()) {
                    result.add(cursor.next());
                }
            }
        }

        if (!wardrobeNames.isEmpty()) {
            Document vectorSearch = new Document("$vectorSearch", new Document("index", "item_vector_index")
                    .append("path", path)
                    .append("queryVector", queryVector)
                    .append("numCandidates", 1000)
                    .append("limit", 1000));

            pipeline.add(vectorSearch);

            pipeline = getNotNullCommonFieldsPipeline(pipeline, notNullStyleFields, notNullListFields, notNullBooleanFields);

            pipeline.add(new Document("$match", new Document("userId", userId)));

            pipeline.add(new Document("$match", new Document("wardrobeId", new Document("$in", wardrobeNames))));

            Document project = new Document("_id", 1)
                    .append("imageUrl", 1)
                    .append("score", new Document("$meta", "vectorSearchScore"));

            pipeline.add(new Document("$project", project));

            try (MongoCursor<Document> cursor = mongoTemplate2.getCollection("item").aggregate(pipeline).iterator()) {
                while (cursor.hasNext()) {
                    result.add(cursor.next());
                }
            }
        }

        return result;
    }

    public List<ClothesFound> searchClothesList(ClothesKey clothesKey, ClothesInfo clothesInfo, Map<String, Style> notNullStyleFields, Map<String, List<Attribute>> notNullListFields, Map<String, Boolean> notNullBooleanFields, String userId, List<String> wardrobeNames, Boolean useBasicWardrobe) throws IOException {

        clothesInfo.setEmbeddingFields(openAIClient);

        Map<String, List<Double>> notNullDetailEmbeddingFields = clothesInfo.getNotNullDetailEmbeddingFields();

        Map<String, List<Double>> notNullOtherEmbeddingFields = clothesInfo.getNotNullOtherEmbeddingFields();

        if (notNullDetailEmbeddingFields.isEmpty() && notNullOtherEmbeddingFields.isEmpty()) {
            List<Document> pipeline = getNotNullCommonFieldsPipeline(new ArrayList<>(), notNullStyleFields, notNullListFields, notNullBooleanFields);

            List<Document> result = new ArrayList<>();

            if (useBasicWardrobe) {
                pipeline.add(new Document("$match", new Document("userId", null)));

                Document project = new Document("imageUrl", 1).append("productUrl", 1);

                pipeline.add(new Document("$project", project));

                try (MongoCursor<Document> cursor = mongoTemplate1.getCollection(getCollection(clothesKey)).aggregate(pipeline).iterator()) {
                    while (cursor.hasNext()) {
                        result.add(cursor.next());
                    }
                }
            }

            if (!wardrobeNames.isEmpty()) {
                pipeline.add(new Document("$match", new Document("userId", userId)));

                pipeline.add(new Document("$match", new Document("wardrobeId", new Document("$in", wardrobeNames))));

                Document project = new Document("imageUrl", 1);

                pipeline.add(new Document("$project", project));
                try (MongoCursor<Document> cursor = mongoTemplate2.getCollection("item").aggregate(pipeline).iterator()) {
                    while (cursor.hasNext()) {
                        result.add(cursor.next());
                    }
                }
            }

            Collections.shuffle(result);

            List<Document> top10 = result.stream().limit(7).toList();

            List<ClothesFound> clothesList = new ArrayList<>();

            for (Document doc : top10){
                ClothesFound newClothes = new ClothesFound(null, doc.getString("imageUrl"), doc.getString("productUrl"), null, null);

                clothesList.add(newClothes);
            }

            return clothesList;
        } else {
            List<ClothesFound> existingClothesList = new ArrayList<>();

            if (!notNullDetailEmbeddingFields.isEmpty()) {
                for (String key : notNullDetailEmbeddingFields.keySet()) {
                    List<Double> value = notNullDetailEmbeddingFields.get(key);

                    for (int i = 1; i <= 3; i++) {
                        List<Document> result = getVectorSearchResult(clothesKey, key, value, notNullStyleFields, notNullListFields, notNullBooleanFields, userId, wardrobeNames, useBasicWardrobe);

                        for (Document doc : result) {
                            String _id = doc.getObjectId("_id").toHexString();

                            if (!existingClothesList.isEmpty()) {
                                ClothesFound existingClothes = existingClothesList.stream()
                                        .filter(c -> _id.equals(c.get_id()))
                                        .findFirst()
                                        .get();

                                Map<String, Double> scores = existingClothes.getScores();

                                double newScore = doc.getDouble("score");

                                if (i == 1) {
                                    scores.put(key, newScore);
                                }
                                else {
                                    Double previousScore = scores.get(key);

                                    scores.put(key, Math.max(previousScore, newScore));
                                }
                            }
                            else {
                                Map<String, Double> scores = new HashMap<>();

                                scores.put(key, doc.getDouble("score"));

                                ClothesFound newClothes = new ClothesFound(_id, doc.getString("imageUrl"), doc.getString("productUrl"), scores, null);

                                existingClothesList.add(newClothes);
                            }
                        }
                    }
                }
            }

            for (String key : notNullOtherEmbeddingFields.keySet()) {
                List<Double> value = notNullOtherEmbeddingFields.get(key);

                List<Document> result = getVectorSearchResult(clothesKey, key, value, notNullStyleFields, notNullListFields, notNullBooleanFields, userId, wardrobeNames, useBasicWardrobe);

                for (Document doc : result) {
                    String _id = doc.getObjectId("_id").toHexString();

                    if (!existingClothesList.isEmpty()) {
                        ClothesFound existingClothes = existingClothesList.stream()
                                .filter(c -> _id.equals(c.get_id()))
                                .findFirst()
                                .get();

                        Map<String, Double> scores = existingClothes.getScores();

                        double score = doc.getDouble("score");

                        scores.put(key, score);
                    }
                    else {
                        Map<String, Double> scores = new HashMap<>();

                        scores.put(key, doc.getDouble("score"));

                        ClothesFound newClothes = new ClothesFound(_id, doc.getString("imageUrl"), doc.getString("product_url"), scores, null);

                        existingClothesList.add(newClothes);
                    }
                }

            }

            for (ClothesFound clothes : existingClothesList) {
                Map<String, Double> scores = clothes.getScores();

                double similarity = scores.values().stream().mapToDouble(Double::doubleValue).sum();

                clothes.setSimilarity(similarity);
            }

            existingClothesList.sort((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()));

            return existingClothesList.stream().limit(7).collect(Collectors.toList());
        }
    }

    public String getCollection(ClothesKey clothesKey) {

        if (clothesKey == ClothesKey.TOP1 || clothesKey == ClothesKey.TOP2) {
            return "top";
        }

        if (clothesKey == ClothesKey.PANTS) {
            return "pants";
        }

        if (clothesKey == ClothesKey.DRESS) {
            return "dress";
        }

        if (clothesKey == ClothesKey.SKIRT) {
            return "skirt";
        }

        return "outerwear";
    }

    public List<String> getPriorityList(String collection, UniqueCoordinationType uniqueCoordinationType) {
        if (uniqueCoordinationType == UniqueCoordinationType.LAYERED_COORDINATION) {
            return switch (collection) {
                case "top" ->
                        List.of("isUnique", "topLength", "style3", "style2", "style1", "season", "pattern", "brightness", "saturation", "color", "isSeeThrough", "fit", "category", "sleeveLength", "isSimple");
                case "pants" ->
                        List.of("isUnique", "style3", "style2", "style1", "season", "pattern", "brightness", "saturation", "color", "category", "pantsFit", "bottomLength");
                case "dress" ->
                        List.of("isUnique", "style3", "style2", "style1", "season", "isTopRequired", "pattern", "brightness", "saturation", "color", "isSeeThrough", "skirtType", "skirtFit", "fit", "skirtLength", "sleeveLength");
                case "skirt" ->
                        List.of("isUnique", "style3", "style2", "style1", "season", "pattern", "brightness", "saturation", "color", "skirtFit", "skirtLength", "skirtType");
                default ->
                        List.of("isUnique", "topLength", "style3", "style2", "style1", "pattern", "brightness", "saturation", "color", "isSeeThrough", "fit", "sleeveLength", "category", "season");
            };
        } else{
            return switch (collection) {
                case "top" ->
                        List.of("isUnique", "topLength", "style3", "style2", "season", "fit", "category", "sleeveLength", "isSimple", "brightness", "saturation", "color", "isSeeThrough", "style1", "pattern");
                case "pants" ->
                        List.of("isUnique", "style3", "style2", "season", "brightness", "saturation", "category", "pantsFit", "bottomLength", "color", "style1", "pattern");
                case "dress" ->
                        List.of("isUnique", "style3", "style2", "season", "isTopRequired", "brightness", "saturation", "skirtType", "skirtFit", "fit", "skirtLength", "sleeveLength", "color", "isSeeThrough", "style1", "pattern");
                case "skirt" ->
                        List.of("isUnique", "style3", "style2", "season", "brightness", "saturation", "skirtType", "skirtFit", "skirtLength", "color", "style1", "pattern");
                default ->
                        List.of("isUnique", "topLength", "style3", "style2", "brightness", "saturation", "fit", "sleeveLength", "color", "isSeeThrough", "style1", "pattern", "season", "category");
            };
        }

    }

    public NotNullFields removeField(String collection, Map<String, Style> notNullStyleFields, Map<String, List<Attribute>> notNullListFields, Map<String, Boolean> notNullBooleanFields, UniqueCoordinationType uniqueCoordinationType) {

        Map<String, Style> notNullStyleFieldsCopy = new HashMap<>(notNullStyleFields);

        Map<String, List<Attribute>> notNullListFieldsCopy = new HashMap<>(notNullListFields);

        Map<String, Boolean> notNullBooleanFieldsCopy = new HashMap<>(notNullBooleanFields);

        List<String> priorityList = getPriorityList(collection, uniqueCoordinationType);

        for (String p : priorityList) {
            if (notNullStyleFieldsCopy.containsKey(p)) {
                notNullStyleFieldsCopy.remove(p);

                return new NotNullFields(notNullStyleFieldsCopy, notNullListFieldsCopy, notNullBooleanFieldsCopy);
            }
            if (notNullListFieldsCopy.containsKey(p)) {
                notNullListFieldsCopy.remove(p);

                return new NotNullFields(notNullStyleFieldsCopy, notNullListFieldsCopy, notNullBooleanFieldsCopy);
            }
            if (notNullBooleanFieldsCopy.containsKey(p)) {
                notNullBooleanFieldsCopy.remove(p);

                return new NotNullFields(notNullStyleFieldsCopy, notNullListFieldsCopy, notNullBooleanFieldsCopy);
            }
        }

        return new NotNullFields(notNullStyleFieldsCopy, notNullListFieldsCopy, notNullBooleanFieldsCopy);
    }

    public Map<OutfitKey, OutfitDto> getOutfits(Map<ClothesKey, ClothesDto> necessaryClothesMap, Map<ClothesKey, List<ClothesFound>> clothesMap, String prompt) throws IOException {

        List<Map<String, Object>> content = new ArrayList<>();

        for (ClothesKey key : necessaryClothesMap.keySet()) {
            ClothesDto value = necessaryClothesMap.get(key);

            content.add(Map.of("type", "text", "text", "다음은 꼭 포함해야 하는 " + key + "입니다."));

            content.add(Map.of("type", "image_url", "image_url", Map.of("url", value.getImageUrl())));

            content.add(Map.of("type", "text", "text", "이 옷의 imageUrl은 " + value.getImageUrl()+ "입니다."));

            content.add(Map.of("type", "text", "text", "이 옷의 productUrl은 " + value.getProductUrl()+ "입니다."));
        }

        for (ClothesKey key : clothesMap.keySet()) {
            List<ClothesFound> value = clothesMap.get(key);

            for (ClothesFound clothes : value) {
                content.add(Map.of("type", "text", "text", "다음은 사용 가능한" + key + "입니다."));

                content.add(Map.of("type", "image_url", "image_url", Map.of("url", clothes.getImageUrl())));

                content.add(Map.of("type", "text", "text", "이 옷의 imageUrl은 " + clothes.getImageUrl() + "입니다."));

                content.add(Map.of("type", "text", "text", "이 옷의 productUrl은 " + clothes.getProductUrl() + "입니다."));
            }
        }

        content.add(Map.of("type", "text", "text", prompt));

        int maxAttempt = 30;

        int attempt = 0;

        while (attempt < maxAttempt) {
            try {
                JsonNode result = openAIClient.chatCompletions(content);

                ObjectMapper mapper = new ObjectMapper();

                Map<String, OutfitDto> OutfitsTemp = mapper.readValue(result.toString(), new TypeReference<>() {});

                Map<OutfitKey, OutfitDto> outfits = new HashMap<>();

                for (Map.Entry<String, OutfitDto> entry : OutfitsTemp.entrySet()) {
                    OutfitKey outfitkey = OutfitKey.from(entry.getKey());

                    outfits.put(outfitkey, entry.getValue());
                }

                return outfits;
            } catch (Exception e) {
                attempt++;
            }
        }
        return Map.of();
    }

    public String getCoordinationRule(String userId) {
        if (coordinationRuleRepository.findByUserId(userId).isPresent()) {
            return coordinationRuleRepository.findByUserId(userId).get().getCoordinationRule();
        } else {
            return "";
        }
    }

    public String addFeedback(Map<ClothesKey, String> outfit, String feedback, String coordinationRule) throws IOException {

        List<Map<String, Object>> content = new ArrayList<>();

        for (ClothesKey key : outfit.keySet()) {
            String value = outfit.get(key);

            content.add(Map.of("type", "text", "text", "다음은 " + key + "입니다."));

            String image_url =  getNecessaryClothes(value).getImageUrl();

            content.add(Map.of("type", "image_url", "image_url", Map.of("url", image_url)));
        }

        String prompt = String.format("""
                    - feedback: %s
                    - coordinationRule: %s
                    
                    첨부한 옷차림에 대한 feedback을 coordinationRule에 통합해 주세요.
                    coordinationRule이란 개인의 취향을 반영한 코디 규칙을 의미합니다.
                    통합된 coordiantion은 300자 이내여야 합니다.
                    feedback의 내용을 벗어난 추측은 안됩니다. 길게 쓸 필요 없습니다.
                    예를 들어 feedback이 '~이 너무 ~하다'는 내용이라면 해당 요소는 기피하라는 내용을 coordinationRule에 통합하면 됩니다.
                    """, feedback, coordinationRule);

        content.add(Map.of("type", "text", "text", prompt));

        return openAIClient.chatCompletions2(content);
    }

    public void setCoordinationRule(String userId, String newCoordinationRule) {

        if (coordinationRuleRepository.findByUserId(userId).isPresent()) {
            CoordinationRule coordinationRule = coordinationRuleRepository.findByUserId(userId).get();

            coordinationRule.setCoordinationRule(newCoordinationRule);

            coordinationRuleRepository.save(coordinationRule);
        } else {
            coordinationRuleRepository.save(new CoordinationRule(userId, newCoordinationRule));
        }
    }

    public void addFavoriteCoordination(String userId, Map<ClothesKey, String> clothesMap) {
        CoordinationFavorite favorite = CoordinationFavorite.builder()
                .userId(userId)
                .clothesMap(
                        clothesMap.entrySet().stream()
                                .collect(Collectors.toMap(
                                        entry -> entry.getKey().name(),
                                        Map.Entry::getValue
                                ))
                )
                .build();

        coordinationFavoriteRepository.save(favorite);
    }

    public List<Map<ClothesKey, Map<String, String>>> getFavoriteCoordinations(String userId) {
        List<CoordinationFavorite> favorites = coordinationFavoriteRepository.findByUserId(userId);

        List<Map<ClothesKey, Map<String, String>>> coordinationList = new ArrayList<>();
        for (CoordinationFavorite favorite : favorites) {
            Map<ClothesKey, Map<String, String>> coordination = new HashMap<>();
            for (Map.Entry<String, String> entry : favorite.getClothesMap().entrySet()) {
                ClothesKey key = ClothesKey.valueOf(entry.getKey());
                ClothesDto clothes = getNecessaryClothes(entry.getValue());
                Map<String, String> map = new HashMap<String, String>();
                map.put("imageUrl", clothes.getImageUrl());
                map.put("productUrl", clothes.getProductUrl());
                coordination.put(key, map);
            }
            coordinationList.add(coordination);
        }
        return coordinationList;
    }

    public void removeFavoriteCoordination(String favoriteId) {
        coordinationFavoriteRepository.deleteById(favoriteId);
    }
}
