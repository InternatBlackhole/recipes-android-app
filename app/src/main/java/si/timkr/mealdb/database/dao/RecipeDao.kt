package si.timkr.mealdb.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import si.timkr.mealdb.models.dto.RecipeDTO

@Dao
interface RecipeDao {
    //since I look by id, there should be only one
    @Query("SELECT * FROM RecipeDetails WHERE idMeal = :idMeal")
    fun getRecipeById(idMeal: String): Flow<RecipeDTO?>

    @get:Query("SELECT * FROM RecipeDetails WHERE isFavorite = 1")
    val favoriteRecipes: Flow<List<RecipeDTO>>?

    @Insert(entity = RecipeDTO::class)
    fun insertRecipe(recipe: RecipeDTO)

    @Update(entity = RecipeDTO::class)
    fun updateRecipe(newRecipe: RecipeDTO)

    @Delete(entity = RecipeDTO::class)
    fun deleteRecipe(recipe: RecipeDTO)
}