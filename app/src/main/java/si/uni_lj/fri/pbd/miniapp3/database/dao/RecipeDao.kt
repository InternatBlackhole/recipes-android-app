package si.uni_lj.fri.pbd.miniapp3.database.dao

import androidx.room.Dao
import androidx.room.Query
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails

@Dao
interface RecipeDao {
    @Query("SELECT * FROM RecipeDetails WHERE idMeal = :idMeal")
    fun getRecipeById(idMeal: String?): RecipeDetails?

    // TODO: Add the missing methods
}