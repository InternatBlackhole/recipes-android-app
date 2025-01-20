package si.timkr.mealdb.models

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import si.timkr.mealdb.database.Database
import si.timkr.mealdb.models.dto.IngredientsDTO
import si.timkr.mealdb.models.dto.RecipeDTO
import si.timkr.mealdb.models.dto.RecipesDTO
import si.timkr.mealdb.rest.RestAPI
import si.timkr.mealdb.rest.ServiceGenerator
import si.timkr.mealdb.viewmodels.DetailsViewModel
import java.nio.file.Files
import java.nio.file.Paths

interface RecipeRepository {
    companion object {
        val instance: RecipeRepository = RecipeRepositoryImpl
    }

    val allIngredients: Flow<IngredientsDTO?>
    fun getRecipesByIngredient(ingredient: String): Flow<RecipesDTO?>

    fun getFullRecipe(recipeId: String): Flow<RecipeDTO?>
    fun getFullRecipe(recipeDTO: RecipeDTO): Flow<RecipeDTO?>

    fun markRecipeFavorite(recipeDTO: RecipeDTO, isFavorite: Boolean): Flow<Throwable?>

    val favoriteRecipes: Flow<RecipesDTO?>
}

private object RecipeRepositoryImpl : RecipeRepository {
    private val mealApi = ServiceGenerator.createService(RestAPI::class.java)
    private val db = Database.instance.recipeDao()

    //private val mainScope = MainScope() //in this scope the state will be shared
    private val ioDispatcher = Dispatchers.IO //where the cold flow will be executed
    private val repositoryScope = CoroutineScope(ioDispatcher)
    override fun getRecipesByIngredient(ingredient: String): Flow<RecipesDTO?> =
        apiFlowCall(mealApi.getRecipesByIngredient(ingredient))

    override fun getFullRecipe(recipeId: String): Flow<RecipeDTO?> = flow<RecipeDTO?> {
        db.getRecipeById(recipeId)
            .collect { recipe ->
                if (recipe == null) {
                    //not in database
                    apiFlowCall(mealApi.getRecipe(recipeId))
                        .map { value -> value?.recipes?.get(0) }
                        .collect(this)
                } else emit(recipe)
            }
    }.flowOn(ioDispatcher)

    override fun getFullRecipe(recipeDTO: RecipeDTO): Flow<RecipeDTO?> =
        getFullRecipe(recipeDTO.recipeId)

    override fun markRecipeFavorite(recipeDTO: RecipeDTO, isFavorite: Boolean) = callbackFlow {
        repositoryScope.launch(context = ioDispatcher) {
            if (isFavorite) {
                //save it
                //save thumbnail to local cache
                if (recipeDTO.recipeThumbnail != null) {
                    try {
                        Files.newOutputStream(
                            Paths.get(recipeDTO.recipeThumbnailLocal)
                        ).use { output ->
                            //thumbnail should not be null, is checked
                            ServiceGenerator.createImageUrlCall(recipeDTO.recipeThumbnail)
                                .execute()
                                .use {
                                    if (!it.isSuccessful) throw Exception("Http request failed")
                                    Log.i(
                                        DetailsViewModel::class.simpleName,
                                        "Content-Type: ${it.header("Content-Type")}"
                                    )
                                    it.body?.byteStream()?.transferTo(output)
                                }
                        }
                    } catch (e: Throwable) {
                        send(e)
                    }
                }
                db.insertRecipe(recipeDTO)
            } else {
                //delete it
                db.deleteRecipe(recipeDTO)
            }
            //db.updateRecipe(recipeDTO.copy(isFavorite = isFavorite))
            send(null)
            close()
        }
        awaitClose { }
    }


    override val favoriteRecipes: Flow<RecipesDTO?> = flow {
        db.favoriteRecipes?.collect {
            emit(RecipesDTO(it))
        }
    }.flowOn(ioDispatcher)

    override val allIngredients: Flow<IngredientsDTO?> = apiFlowCall(mealApi.allIngredients)

    private fun <T> apiFlowCall(
        call: Call<T>,
    ): Flow<T?> =
        callbackFlow {
            //is this callback executed on some internal okhttp thread?
            call.clone().enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (!response.isSuccessful) {
                        close(Error("Call failed with code: ${response.code()} ${response.message()}"))
                        return
                    }
                    trySend(response.body())
                    close()
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    close(t)
                }
            })
            awaitClose { }
        }
}