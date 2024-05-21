package com.existmg.library_data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.existmg.library_data.db.dao.MemorandumDao
import com.existmg.library_data.db.dao.MemorandumImgDao
import com.existmg.library_data.db.dao.RemindDao
import com.existmg.library_data.db.dao.TargetCheckInDao
import com.existmg.library_data.db.dao.TargetDao
import com.existmg.library_data.db.entity.MemorandumEntity
import com.existmg.library_data.db.entity.MemorandumImgEntity
import com.existmg.library_data.db.entity.RemindEntity
import com.existmg.library_data.db.entity.TargetCheckInEntity
import com.existmg.library_data.db.entity.TargetEntity
import com.existmg.library_data.db.migrations.DatabaseMigrations

/**
 * @Author ContentMy
 * @Date 2024/4/24 4:50 PM
 * @Description 这里是数据库统一的database类，封装了初始化和对应dao的声明
 */
@Database(
    entities = [
        RemindEntity::class,
        TargetEntity::class,TargetCheckInEntity::class,
        MemorandumEntity::class,MemorandumImgEntity::class
               ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {// Room 需要使用工厂模式来创建数据库实例，所以必须声明一个抽象类，并且这个类不能被直接实例化
abstract fun remindDao(): RemindDao
abstract fun targetDao():TargetDao
abstract fun targetCheckInDao():TargetCheckInDao
abstract fun memorandumDao():MemorandumDao
abstract fun memorandumImgDao(): MemorandumImgDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
        ): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "run_fast_xm_database")
                    .addMigrations(DatabaseMigrations.MIGRATION_1_2,DatabaseMigrations.MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

    }
}