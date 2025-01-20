package si.timkr.mealdb.models.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class IngredientDTO(
    @SerializedName("idIngredient")
    @Expose
    val idIngredient: String? = null,

    @SerializedName("strIngredient")
    @Expose
    val strIngredient: String? = null,

    @SerializedName("strDescription")
    @Expose
    val strDescription: String? = null
)