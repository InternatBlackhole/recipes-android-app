package si.uni_lj.fri.pbd.miniapp3.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import si.uni_lj.fri.pbd.miniapp3.models.dto.*

interface RestAPI {
    @get:GET("list.php?i=list")
    val allIngredients: Call<IngredientsDTO?>

    @GET("filter.php")
    fun getRecipesByIngredient(@Query("i") ingredient: String): Call<RecipesDTO?>

    @GET("lookup.php")
    fun getRecipe(@Query("i") mealId: String): Call<RecipesDTO?>
}