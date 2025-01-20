package si.timkr.mealdb.models

data class RecipeSummaryIM(
    val strMeal: String,
    val strMealThumb: String,
    val idMeal: String
) {
    override fun toString(): String {
        return "RecipeSummaryIM(strMeal='$strMeal', strMealThumb='$strMealThumb', idMeal='$idMeal')"
    }
}

