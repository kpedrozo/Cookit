package com.example.cookit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cookit.models.RecipeDao
import com.example.cookit.models.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
abstract class RoomDataBase : RoomDatabase() {

    abstract fun recipeDao() : RecipeDao

    companion object {
        @Volatile
        private var instance : RoomDataBase? = null

        fun getInstance (context: Context) : RoomDataBase = instance ?: synchronized(this) {
            instance?: buildDataBase(context)
        }

        private fun buildDataBase(context: Context) : RoomDataBase =
            Room.databaseBuilder(context, RoomDataBase::class.java, "room_database")
                .fallbackToDestructiveMigration() // ante cualquier cambio de version (tablas, columnas, etc) -> deberiamos proveer un metodo de migracion con .addMigrations()
                .build()
    }
}