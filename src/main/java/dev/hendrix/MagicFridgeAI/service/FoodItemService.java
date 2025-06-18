package dev.hendrix.MagicFridgeAI.service;

import dev.hendrix.MagicFridgeAI.model.FoodItem;
import dev.hendrix.MagicFridgeAI.repository.FoodItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;

    public FoodItemService(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    // Create a new food item
    public FoodItem createFood(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }

    // List all food items
    public List<FoodItem> getAll(){
        return foodItemRepository.findAll();
    }

    //List food items by id
    public Optional<FoodItem> getFoodById(Long id) {
        return foodItemRepository.findById(id);
    }

    // Update an existing food item by id
    public FoodItem updateFood(Long id, FoodItem foodItem) {
        foodItem.setId(id);
        return foodItemRepository.save(foodItem);
    }

    // Update an existing food item by id
    public void deleteFood(Long id) {
        foodItemRepository.deleteById(id);
    }
}
