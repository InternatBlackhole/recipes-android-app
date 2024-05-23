package si.uni_lj.fri.pbd.miniapp3.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity
data class RecipeDetails(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean? = null,

    @ColumnInfo(name = "idMeal")
    @Expose
    var idMeal: String? = null,

    @ColumnInfo(name = "strMeal")
    @Expose
    var strMeal: String? = null,

    @ColumnInfo(name = "strCategory")
    @Expose
    var strCategory: String? = null,

    @ColumnInfo(name = "strArea")
    @Expose
    var strArea: String? = null,

    @ColumnInfo(name = "strInstructions")
    @Expose
    var strInstructions: String? = null,

    @ColumnInfo(name = "strMealThumb")
    @Expose
    var strMealThumb: String? = null,

    @ColumnInfo(name = "strYoutube")
    @Expose
    var strYoutube: String? = null,

    @ColumnInfo(name = "strIngredient1")
    @Expose
    var strIngredient1: String? = null,

    @ColumnInfo(name = "strIngredient2")
    @Expose
    var strIngredient2: String? = null,

    @ColumnInfo(name = "strIngredient3")
    @Expose
    var strIngredient3: String? = null,

    @ColumnInfo(name = "strIngredient4")
    @Expose
    var strIngredient4: String? = null,

    @ColumnInfo(name = "strIngredient5")
    @Expose
    var strIngredient5: String? = null,

    @ColumnInfo(name = "strIngredient6")
    @Expose
    var strIngredient6: String? = null,

    @ColumnInfo(name = "strIngredient7")
    @Expose
    var strIngredient7: String? = null,

    @ColumnInfo(name = "strIngredient8")
    @Expose
    var strIngredient8: String? = null,

    @ColumnInfo(name = "strIngredient9")
    @Expose
    var strIngredient9: String? = null,

    @ColumnInfo(name = "strIngredient10")
    @Expose
    var strIngredient10: String? = null,

    @ColumnInfo(name = "strIngredient11")
    @Expose
    var strIngredient11: String? = null,

    @ColumnInfo(name = "strIngredient12")
    @Expose
    var strIngredient12: String? = null,

    @ColumnInfo(name = "strIngredient13")
    @Expose
    var strIngredient13: String? = null,

    @ColumnInfo(name = "strIngredient14")
    @Expose
    var strIngredient14: String? = null,

    @ColumnInfo(name = "strIngredient15")
    @Expose
    var strIngredient15: String? = null,

    @ColumnInfo(name = "strIngredient16")
    @Expose
    var strIngredient16: String? = null,

    @ColumnInfo(name = "strIngredient17")
    @Expose
    var strIngredient17: String? = null,

    @ColumnInfo(name = "strIngredient18")
    @Expose
    var strIngredient18: String? = null,

    @ColumnInfo(name = "strIngredient19")
    @Expose
    var strIngredient19: String? = null,

    @ColumnInfo(name = "strIngredient20")
    @Expose
    var strIngredient20: String? = null,

    @ColumnInfo(name = "strMeasure1")
    @Expose
    var strMeasure1: String? = null,

    @ColumnInfo(name = "strMeasure2")
    @Expose
    var strMeasure2: String? = null,

    @ColumnInfo(name = "strMeasure3")
    @Expose
    var strMeasure3: String? = null,

    @ColumnInfo(name = "strMeasure4")
    @Expose
    var strMeasure4: String? = null,

    @ColumnInfo(name = "strMeasure5")
    @Expose
    var strMeasure5: String? = null,

    @ColumnInfo(name = "strMeasure6")
    @Expose
    var strMeasure6: String? = null,

    @ColumnInfo(name = "strMeasure7")
    @Expose
    var strMeasure7: String? = null,

    @ColumnInfo(name = "strMeasure8")
    @Expose
    var strMeasure8: String? = null,

    @ColumnInfo(name = "strMeasure9")
    @Expose
    var strMeasure9: String? = null,

    @ColumnInfo(name = "strMeasure10")
    @Expose
    var strMeasure10: String? = null,

    @ColumnInfo(name = "strMeasure11")
    @Expose
    var strMeasure11: String? = null,

    @ColumnInfo(name = "strMeasure12")
    @Expose
    var strMeasure12: String? = null,

    @ColumnInfo(name = "strMeasure13")
    @Expose
    var strMeasure13: String? = null,

    @ColumnInfo(name = "strMeasure14")
    @Expose
    var strMeasure14: String? = null,

    @ColumnInfo(name = "strMeasure15")
    @Expose
    var strMeasure15: String? = null,

    @ColumnInfo(name = "strMeasure16")
    @Expose
    var strMeasure16: String? = null,

    @ColumnInfo(name = "strMeasure17")
    @Expose
    var strMeasure17: String? = null,

    @ColumnInfo(name = "strMeasure18")
    @Expose
    var strMeasure18: String? = null,

    @ColumnInfo(name = "strMeasure19")
    @Expose
    var strMeasure19: String? = null,

    @ColumnInfo(name = "strMeasure20")
    @Expose
    var strMeasure20: String? = null,

    @ColumnInfo(name = "strSource")
    @Expose
    var strSource: String? = null
) {
    @Ignore
    constructor() : this(null)
}
