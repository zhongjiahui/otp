package com.zjh.otp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {OtpHistoryEntity.class}, version = 1)
public abstract class OtpDataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "authing_mobile";

    private static OtpDataBase databaseInstance;

    public static synchronized OtpDataBase getInstance(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room
                    .databaseBuilder(context.getApplicationContext(), OtpDataBase.class, DATABASE_NAME)
                    .build();
        }
        return databaseInstance;
    }


    public abstract OtpHistoryDao otpHistoryDao();

}
