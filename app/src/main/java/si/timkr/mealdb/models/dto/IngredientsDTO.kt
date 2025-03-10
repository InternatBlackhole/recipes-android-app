package si.timkr.mealdb.models.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class IngredientsDTO(
    @SerializedName("meals")
    @Expose
    val ingredients: List<IngredientDTO>? = null
) {
}