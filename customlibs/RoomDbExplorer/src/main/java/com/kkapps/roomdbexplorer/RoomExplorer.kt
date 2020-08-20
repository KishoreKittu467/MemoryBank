package com.kkapps.roomdbexplorer

import android.content.Context
import android.content.Intent
import androidx.room.RoomDatabase

object RoomExplorer {
    /**
     * Launches [RoomExplorerActivity] from the context passed in the method.
     * @param context The context such as any activity or fragment or context reference
     * @param databaseClass The database class registered in Room with @Database annotation and extended with RoomDatabase
     * @param dbName The name of your Room Database
     */
    fun show(context: Context, databaseClass: Class<out RoomDatabase?>?, dbName: String?) {
        val intent = Intent(context, RoomExplorerActivity::class.java)
        intent.putExtra(RoomExplorerActivity.DATABASE_CLASS_KEY, databaseClass)
        intent.putExtra(RoomExplorerActivity.DATABASE_NAME_KEY, dbName)
        context.startActivity(intent)
    }
}
