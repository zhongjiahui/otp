package com.zjh.otp.database;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.DeleteTable;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.AutoMigrationSpec;

@Database(entities = {OtpHistoryEntity.class}, version = 1)
public abstract class AuthingMobileDataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "authing_mobile";

    private static AuthingMobileDataBase databaseInstance;

    public static synchronized AuthingMobileDataBase getInstance(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room
                    .databaseBuilder(context.getApplicationContext(), AuthingMobileDataBase.class, DATABASE_NAME)
                    .build();
        }
        return databaseInstance;
    }


    public abstract OtpHistoryDao otpHistoryDao();

}
