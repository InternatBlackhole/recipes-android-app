package si.timkr.mealdb.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import si.timkr.mealdb.models.dto.IngredientsDTO
import si.timkr.mealdb.models.dto.RecipesDTO

interface RestAPI {
    @get:GET("list.php?i=list")
    val allIngredients: Call<IngredientsDTO?>

    @GET("filter.php")
    fun getRecipesByIngredient(@Query("i") ingredient: String): Call<RecipesDTO?>

    @GET("lookup.php")
    fun getRecipe(@Query("i") mealId: String): Call<RecipesDTO?>
}