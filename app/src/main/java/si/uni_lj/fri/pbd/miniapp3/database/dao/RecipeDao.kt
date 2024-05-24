package si.uni_lj.fri.pbd.miniapp3.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeDTO

@Dao
interface RecipeDao {
    //since I look by id, there should be only one
    @Query("SELECT * FROM RecipeDetails WHERE idMeal = :idMeal")
    fun getRecipeById(idMeal: String): RecipeDTO?

    @get:Query("SELECT * FROM RecipeDetails WHERE isFavorite = 1")
    val favoriteRecipes: List<RecipeDTO>?

    @Insert(entity = RecipeDTO::class)
    fun insertRecipe(recipe: RecipeDTO)

    @Update(entity = RecipeDTO::class)
    fun updateRecipe(newRecipe: RecipeDTO)

    @Delete(entity = RecipeDTO::class)
    fun deleteRecipe(recipe: RecipeDTO)
}