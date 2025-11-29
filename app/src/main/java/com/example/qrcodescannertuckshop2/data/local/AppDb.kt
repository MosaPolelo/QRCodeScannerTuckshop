package com.example.qrcodescannertuckshop2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration          // ✅ import Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [UserEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDb? = null

        // v1 -> v2: add uid column, backfill, add unique index
        private val MIGRATION_1_2 = object : Migration(1, 2) {   // ✅ use Migration(1,2)
            override fun migrate(db: SupportSQLiteDatabase) {
                // 1) add non-null column with temporary default
                db.execSQL(
                    """
                    ALTER TABLE users 
                    ADD COLUMN uid TEXT NOT NULL DEFAULT ''
                    """.trimIndent()
                )

                // 2) backfill uid for existing rows
                db.execSQL(
                    """
                    UPDATE users 
                    SET uid = 'Local:' || username 
                    WHERE uid = ''
                    """.trimIndent()
                )

                // 3) unique index on uid
                db.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS index_users_uid 
                    ON users(uid)
                    """.trimIndent()
                )
            }
        }

        fun getInstance(context: Context): AppDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "tuckshop_db"                // ✅ check spelling
                )
                    .addMigrations(MIGRATION_1_2) // ✅ register migration
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
