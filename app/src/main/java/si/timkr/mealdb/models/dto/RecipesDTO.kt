package si.timkr.mealdb.models.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RecipesDTO(
    @SerializedName("meals")
    @Expose
    val recipes: List<RecipeDTO>? = null
) {
}