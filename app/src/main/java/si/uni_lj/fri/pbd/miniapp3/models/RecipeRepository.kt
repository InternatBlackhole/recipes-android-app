package si.uni_lj.fri.pbd.miniapp3.models

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesDTO
import si.uni_lj.fri.pbd.miniapp3.rest.RestAPI
import si.uni_lj.fri.pbd.miniapp3.rest.ServiceGenerator

interface RecipeRepository {
    companion object {
        val instance: RecipeRepository = RecipeRepositoryImpl
    }

    val allIngredients: Flow<IngredientsDTO?>
    fun getRecipesByIngredient(ingredient: String): Flow<RecipesDTO?>
}

private object RecipeRepositoryImpl : RecipeRepository {
    private val mealApi = ServiceGenerator.createService(RestAPI::class.java)

    //private val mainScope = MainScope() //in this scope the state will be shared
    //private val ioDispatcher = Dispatchers.Default //where the cold flow will be executed
    override fun getRecipesByIngredient(ingredient: String): Flow<RecipesDTO?> =
        apiFlowCall(mealApi.getRecipesByIngredient(ingredient)) {
            trySend(it)
        }//.shareIn(mainScope, SharingStarted.WhileSubscribed(), 1)

    /*callbackFlow {
    mealApi.getMealsByIngredient(ingredient)?.enqueue(object : Callback<MealsDTO?> {
        override fun onResponse(call: Call<MealsDTO?>, response: Response<MealsDTO?>) {
            if (!response.isSuccessful) {
                close(Error("Call failed with code: ${response.code()} ${response.message()}"))
                return
            }
            response.body()?.meals?.forEach {
                val res = trySend(it)
                if (!res.isSuccess) {
                    close(res.exceptionOrNull())
                    return
                }
            }
            close()
        }

        override fun onFailure(call: Call<MealsDTO?>, t: Throwable) {
            close(t)
        }
    })
}*/

    override val allIngredients: Flow<IngredientsDTO?> = apiFlowCall(mealApi.allIngredients) {
        trySend(it)
    }//.shareIn(mainScope, SharingStarted.WhileSubscribed(), 1)


    /*callbackFlow {
    mealApi.allIngredients?.enqueue(object : Callback<IngredientsDTO?> {
        override fun onResponse(
            call: Call<IngredientsDTO?>,
            response: Response<IngredientsDTO?>
        ) {
            if (!response.isSuccessful) {
                close(Error("Call failed with code: ${response.code()} ${response.message()}"))
                return
            }
            response.body()?.meals?.forEach {
                val res = trySend(it)
                if (!res.isSuccess) {
                    close(res.exceptionOrNull())
                    return
                }
            }
            close()
        }

        override fun onFailure(call: Call<IngredientsDTO?>, t: Throwable) {
            close(t)
        }

    })
}*/

    /**
     * @param handleItem Callback that handles the retrieved response, run in some coroutine
     */
    private fun <T> apiFlowCall(
        call: Call<T>,
        handleItem: ProducerScope<T>.(response: T?) -> Unit
    ): Flow<T?> =
        callbackFlow {
            //is this callback executed on some internal okhttp thread?
            call.clone().enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (!response.isSuccessful) {
                        close(Error("Call failed with code: ${response.code()} ${response.message()}"))
                        return
                    }
                    handleItem(this@callbackFlow, response.body())
                    close()
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    close(t)
                }
            })
            awaitClose { }
        }//.flowOn(ioDispatcher)
}