package dev.hendrix.MagicFridgeAI.controller;

import dev.hendrix.MagicFridgeAI.model.FoodItem;
import dev.hendrix.MagicFridgeAI.service.FoodItemService;
import dev.hendrix.MagicFridgeAI.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private FoodItemService foodItemService;
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService, FoodItemService foodItemService) {
        this.recipeService = recipeService;
        this.foodItemService = foodItemService;
    }

    @GetMapping("/generate")
    public Mono<ResponseEntity<String>> generateRecipe() {
        List<FoodItem> foodItem = foodItemService.getAll();
        return recipeService.generateRecipe(foodItem)
                .map(recipe -> ResponseEntity.ok(recipe))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
