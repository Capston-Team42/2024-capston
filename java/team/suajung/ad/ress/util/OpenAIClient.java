package team.suajung.ad.ress.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIClient {

    @Value("${openai.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode chatCompletions(List<Map<String, Object>> content) throws IOException {

        String url = "https://api.openai.com/v1/chat/completions";

        HttpPost post = new HttpPost(url);

        ObjectNode requestBody = objectMapper.createObjectNode();

        requestBody.put("model", "gpt-4.1");

        ArrayNode messages = objectMapper.createArrayNode();

        ObjectNode systemMessage = objectMapper.createObjectNode();

        systemMessage.put("role", "system");

        systemMessage.put("content", "당신은 패션스타일리스트입니다.");

        messages.add(systemMessage);

        ObjectNode userMessages = objectMapper.createObjectNode();

        userMessages.put("role", "user");

        userMessages.set("content", objectMapper.valueToTree(content));

        messages.add(userMessages);

        requestBody.set("messages", messages);

        ObjectNode responseFormat = objectMapper.createObjectNode();

        responseFormat.put("type", "json_object");

        requestBody.set("response_format", responseFormat);

        post.setHeader("Authorization", "Bearer " + apiKey);

        post.setHeader("Content-Type", "application/json");

        post.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpClientResponseHandler<JsonNode> handler = response -> {
                int status = response.getCode();

                if (status >= 200 && status < 300) {
                    String result = EntityUtils.toString(response.getEntity());

                    JsonNode root = objectMapper.readTree(result);

                    String contentString = root.path("choices").get(0).path("message").path("content").asText();

                    System.out.println(contentString);

                    JsonNode clothesInfos = objectMapper.readTree(contentString);

                    return clothesInfos;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };

            return httpClient.execute(post, handler);
        }
    }

    public List<Double> embeddings(String text) throws IOException {

        String url = "https://api.openai.com/v1/embeddings";

        HttpPost post = new HttpPost(url);

        ObjectNode requestBody = objectMapper.createObjectNode();

        requestBody.put("model", "text-embedding-3-large");

        requestBody.put("input", text);

        post.setHeader("Authorization", "Bearer " + apiKey);

        post.setHeader("Content-Type", "application/json");

        post.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpClientResponseHandler<List<Double>> handler = response -> {

                int status = response.getCode();

                if (status >= 200 && status < 300) {
                    String result = EntityUtils.toString(response.getEntity());

                    JsonNode rootNode = objectMapper.readTree(result);

                    JsonNode embeddingNode = rootNode.path("data").get(0).path("embedding");

                    List<Double> embedding = new ArrayList<>();

                    for (JsonNode node : embeddingNode) {
                        embedding.add(node.asDouble());
                    }

                    return embedding;
                } else {
                    throw new HttpException("Unexpected response status: " + status);
                }
            };

            return null;
        }
    }

    public String chatCompletions2(List<Map<String, Object>> content) throws IOException {

        String url = "https://api.openai.com/v1/chat/completions";

        HttpPost post = new HttpPost(url);

        ObjectNode requestBody = objectMapper.createObjectNode();

        requestBody.put("model", "gpt-4.1");

        ArrayNode messages = objectMapper.createArrayNode();

        ObjectNode userMessages = objectMapper.createObjectNode();

        userMessages.put("role", "user");

        userMessages.set("content", objectMapper.valueToTree(content));

        messages.add(userMessages);

        requestBody.set("messages", messages);

        post.setHeader("Authorization", "Bearer " + apiKey);

        post.setHeader("Content-Type", "application/json");

        post.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpClientResponseHandler<String> handler = response -> {
                int status = response.getCode();

                if (status >= 200 && status < 300) {
                    String result = EntityUtils.toString(response.getEntity());

                    JsonNode root = objectMapper.readTree(result);

                    return root.path("choices").get(0).path("message").path("content").asText();
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            return httpClient.execute(post, handler);
        }
    }
}

