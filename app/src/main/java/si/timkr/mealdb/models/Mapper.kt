package si.timkr.mealdb.models

import si.timkr.mealdb.models.dto.RecipeDTO

object Mapper {

    fun mapRecipeDetailsToRecipeSummaryIm(rec: RecipeDTO): RecipeSummaryIM {
        return RecipeSummaryIM(rec.recipeName!!, rec.recipeThumbnail!!, rec.recipeId!!)
    }

    fun mapRecipeDetailsDtoToRecipeDetailsIm(
        isFavorite: Boolean?,
        dto: RecipeDTO
    ): RecipeDetailsIM {
        return RecipeDetailsIM(
            isFavorite = isFavorite,
            idMeal = dto.recipeId,
            strMeal = dto.recipeName,
            strCategory = dto.strCategory,
            strArea = dto.strArea,
            strInstructions = dto.strInstructions,
            strMealThumb = dto.recipeThumbnail,
            strYoutube = dto.strYoutube,
            strIngredient1 = dto.strIngredient1,
            strIngredient2 = dto.strIngredient2,
            strIngredient3 = dto.strIngredient3,
            strIngredient4 = dto.strIngredient4,
            strIngredient5 = dto.strIngredient5,
            strIngredient6 = dto.strIngredient6,
            strIngredient7 = dto.strIngredient7,
            strIngredient8 = dto.strIngredient8,
            strIngredient9 = dto.strIngredient9,
            strIngredient10 = dto.strIngredient10,
            strIngredient11 = dto.strIngredient11,
            strIngredient12 = dto.strIngredient12,
            strIngredient13 = dto.strIngredient13,
            strIngredient14 = dto.strIngredient14,
            strIngredient15 = dto.strIngredient15,
            strIngredient16 = dto.strIngredient16,
            strIngredient17 = dto.strIngredient17,
            strIngredient18 = dto.strIngredient18,
            strIngredient19 = dto.strIngredient19,
            strIngredient20 = dto.strIngredient20,
            strMeasure1 = dto.strMeasure1,
            strMeasure2 = dto.strMeasure2,
            strMeasure3 = dto.strMeasure3,
            strMeasure4 = dto.strMeasure4,
            strMeasure5 = dto.strMeasure5,
            strMeasure6 = dto.strMeasure6,
            strMeasure7 = dto.strMeasure7,
            strMeasure8 = dto.strMeasure8,
            strMeasure9 = dto.strMeasure9,
            strMeasure10 = dto.strMeasure10,
            strMeasure11 = dto.strMeasure11,
            strMeasure12 = dto.strMeasure12,
            strMeasure13 = dto.strMeasure13,
            strMeasure14 = dto.strMeasure14,
            strMeasure15 = dto.strMeasure15,
            strMeasure16 = dto.strMeasure16,
            strMeasure17 = dto.strMeasure17,
            strMeasure18 = dto.strMeasure18,
            strMeasure19 = dto.strMeasure19,
            strMeasure20 = dto.strMeasure20,
            strSource = dto.strSource
        )
    }

    fun mapRecipeDetailsToRecipeDetailsIm(
        isFavorite: Boolean?,
        dto: RecipeDTO
    ): RecipeDetailsIM {
        return RecipeDetailsIM(
            isFavorite = isFavorite,
            idMeal = dto.recipeId,
            strMeal = dto.recipeName,
            strCategory = dto.strCategory,
            strArea = dto.strArea,
            strInstructions = dto.strInstructions,
            strMealThumb = dto.recipeThumbnail,
            strYoutube = dto.strYoutube,
            strIngredient1 = dto.strIngredient1,
            strIngredient2 = dto.strIngredient2,
            strIngredient3 = dto.strIngredient3,
            strIngredient4 = dto.strIngredient4,
            strIngredient5 = dto.strIngredient5,
            strIngredient6 = dto.strIngredient6,
            strIngredient7 = dto.strIngredient7,
            strIngredient8 = dto.strIngredient8,
            strIngredient9 = dto.strIngredient9,
            strIngredient10 = dto.strIngredient10,
            strIngredient11 = dto.strIngredient11,
            strIngredient12 = dto.strIngredient12,
            strIngredient13 = dto.strIngredient13,
            strIngredient14 = dto.strIngredient14,
            strIngredient15 = dto.strIngredient15,
            strIngredient16 = dto.strIngredient16,
            strIngredient17 = dto.strIngredient17,
            strIngredient18 = dto.strIngredient18,
            strIngredient19 = dto.strIngredient19,
            strIngredient20 = dto.strIngredient20,
            strMeasure1 = dto.strMeasure1,
            strMeasure2 = dto.strMeasure2,
            strMeasure3 = dto.strMeasure3,
            strMeasure4 = dto.strMeasure4,
            strMeasure5 = dto.strMeasure5,
            strMeasure6 = dto.strMeasure6,
            strMeasure7 = dto.strMeasure7,
            strMeasure8 = dto.strMeasure8,
            strMeasure9 = dto.strMeasure9,
            strMeasure10 = dto.strMeasure10,
            strMeasure11 = dto.strMeasure11,
            strMeasure12 = dto.strMeasure12,
            strMeasure13 = dto.strMeasure13,
            strMeasure14 = dto.strMeasure14,
            strMeasure15 = dto.strMeasure15,
            strMeasure16 = dto.strMeasure16,
            strMeasure17 = dto.strMeasure17,
            strMeasure18 = dto.strMeasure18,
            strMeasure19 = dto.strMeasure19,
            strMeasure20 = dto.strMeasure20,
            strSource = dto.strSource
        )
    }
}
