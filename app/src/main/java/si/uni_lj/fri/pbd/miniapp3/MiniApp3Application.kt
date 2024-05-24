package si.uni_lj.fri.pbd.miniapp3

import android.app.Application
import si.uni_lj.fri.pbd.miniapp3.database.Database

class MiniApp3Application : Application() {
    override fun onCreate() {
        super.onCreate()
        Database.init(applicationContext)
    }
}