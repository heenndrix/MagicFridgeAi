package dev.hendrix.MagicFridgeAI.controller;

import dev.hendrix.MagicFridgeAI.model.FoodItem;
import dev.hendrix.MagicFridgeAI.service.FoodItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
public class FoodItemController {

    private FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    //POST
    @PostMapping("/add")
    public ResponseEntity<FoodItem> create(@RequestBody FoodItem foodItem) {
        FoodItem save = foodItemService.createFood(foodItem);
        return ResponseEntity.ok(save);
    }

    //GET
    @GetMapping("/all")
    public ResponseEntity<List<FoodItem>> getAll() {
        List<FoodItem> foodItems = foodItemService.getAll();
        return ResponseEntity.ok(foodItems);
    }

    //UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> update(@PathVariable Long id, @RequestBody FoodItem foodItem) {
        return foodItemService.getFoodById(id)
                .map(existingFood -> {
                    foodItem.setId(existingFood.getId());
                    FoodItem updatedFood = foodItemService.updateFood(id, foodItem);
                    return ResponseEntity.ok(updatedFood);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        foodItemService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }
}
