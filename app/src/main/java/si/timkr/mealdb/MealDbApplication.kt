package si.timkr.mealdb

import android.app.Application
import si.timkr.mealdb.database.Database

class MealDbApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Database.init(applicationContext)
    }
}