package si.uni_lj.fri.pbd.miniapp3.models.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecipeDTO {
    @SerializedName("strMeal")
    @Expose
    val recipeName: String? = null

    @SerializedName("strMealThumb")
    @Expose
    val recipeThumbnail: String? = null

    @SerializedName("idMeal")
    @Expose
    val recipeId: String? = null
}
