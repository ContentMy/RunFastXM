package com.existmg.library_data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * @Author ContentMy
 * @Date 2024/5/13 1:04 AM
 * @Description
 */
object DatabaseMigrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // 数据库版本1到版本2的迁移逻辑
            //第一种方案 创建新结构的临时表，获取旧表中的数据+默认的新列数据填充到临时表中，删除旧表重命名临时表。适合有较大改动、需要处理迁移逻辑的情况
            // 1. 创建一个新的临时表
            db.execSQL("CREATE TABLE IF NOT EXISTS remind_table_temp (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "remindImg TEXT DEFAULT '', " +
                    "remindTitle TEXT DEFAULT '', " +
                    "remindTime INTEGER  NOT NULL DEFAULT 0, " +
                    "remindStartTime INTEGER  NOT NULL DEFAULT 0, " +
                    "remindEndTime INTEGER  NOT NULL DEFAULT 0, " +
                    "remindContent TEXT DEFAULT '', " +
                    "remindCompleteStatus INTEGER  NOT NULL DEFAULT 0)")

            // 2. 将旧表中的数据复制到临时表中
            db.execSQL("INSERT INTO remind_table_temp " +
                    "(id, remindImg, remindTitle, remindTime, remindStartTime, remindEndTime, remindContent, remindCompleteStatus) " +
                    "SELECT id, remindImg, remindTitle, remindTime, 0, 0, '', 0 " +
                    "FROM remind_table")

            // 3. 删除旧表
            db.execSQL("DROP TABLE IF EXISTS remind_table")

            // 4. 重命名临时表为原表名
            db.execSQL("ALTER TABLE remind_table_temp RENAME TO remind_table")

            //第二种方案 适合简单的迁移
//            db.execSQL("ALTER TABLE remind_table " + " ADD COLUMN remindImg TEXT NOT NULL DEFAULT ''")
//            db.execSQL("ALTER TABLE remind_table " + " ADD COLUMN remindTitle TEXT NOT NULL DEFAULT ''")
//            db.execSQL("ALTER TABLE remind_table " + " ADD COLUMN remindContent TEXT NOT NULL DEFAULT ''")
//            db.execSQL("ALTER TABLE remind_table " + " ADD COLUMN remindTime TEXT NOT NULL DEFAULT ''")
//            db.execSQL("ALTER TABLE remind_table " + " ADD COLUMN remindStartTime INTEGER NOT NULL DEFAULT 0")
//            db.execSQL("ALTER TABLE remind_table " + " ADD COLUMN remindEndTime INTEGER NOT NULL DEFAULT 0")
//            db.execSQL("ALTER TABLE remind_table " + " ADD COLUMN remindCompleteStatus INTEGER NOT NULL DEFAULT 1")

            //TODO:方案三：使用自动迁移，不需要这个迁移类，直接在database类的注解中使用autoMigrations，但目前使用autoMigrations需要androidx.room的插件支持，
            // 目前接入该插件会显示找不到的错误，但是room相关依赖是导入模块的，怀疑是与gradle版本不兼容，后续统一处理兼容问题时，进一步查看

        }
    }
}