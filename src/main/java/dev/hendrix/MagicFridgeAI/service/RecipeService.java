package dev.hendrix.MagicFridgeAI.service;

import dev.hendrix.MagicFridgeAI.model.FoodItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final WebClient webClient;

    @Value("${API_KEY}")
    private String apiKey;

    public RecipeService(WebClient webClient) {
        this.webClient = webClient;
    }




    public Mono<String> generateRecipe(List<FoodItem> foodItems) {

        String food = foodItems.stream()
                .map(item -> String.format("%s (%s) - Quantidade %d, Validade: %s",
                                item.getName(),
                                item.getId(),
                                item.getQuantity(),
                                item.getExpirationDate()))
                .collect(Collectors.joining("\n "))
                ;
        String prompt = "Você é um chef de cozinha especializado em criar receitas deliciosas e saudáveis, " +
                "com base nos seguintes ingredientes disponíveis no meu banco de dados:\n" + food +
                "\n\nPor favor, crie uma receita que utilize esses ingredientes, " +
                "incluindo o modo de preparo e o tempo de cozimento. ";
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4.1",
                "messages", List.of(
                        Map.of("role", "system",  "content", "Você é um chef de cozinha especializado em criar receitas deliciosas e saudáveis. "),
                        Map.of("role", "user", "content", prompt)
                )
        );
        return webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    var choices = (List<Map<String, Object>>) response.get("choices");
                    if(choices != null && !choices.isEmpty()) {
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                        return (String) message.get("content").toString();
                    }
                    return "Nenhuma receita encontrada.";
                });
    }
}
