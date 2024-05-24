package si.uni_lj.fri.pbd.miniapp3.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import si.uni_lj.fri.pbd.miniapp3.Constants
import si.uni_lj.fri.pbd.miniapp3.database.dao.RecipeDao
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeDTO

@androidx.room.Database(entities = [RecipeDTO::class], version = 2, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: Database

        val instance: Database
            get() {
                if (!::INSTANCE.isInitialized) {
                    throw Error("Ah you see, this thing has to be initialized in Application")
                }
                return INSTANCE
            }

        fun init(context: Context) {
            if (!::INSTANCE.isInitialized) {
                synchronized(Database::class.java) {
                    if (!::INSTANCE.isInitialized) {
                        INSTANCE = Room.databaseBuilder(
                            context,
                            Database::class.java, Constants.DB_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
        }
    }
}