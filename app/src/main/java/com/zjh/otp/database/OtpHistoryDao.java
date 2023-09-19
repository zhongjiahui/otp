package com.zjh.otp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OtpHistoryDao {

    @Insert
    void insertHistory(OtpHistoryEntity entity);

    @Delete
    void deleteHistory(OtpHistoryEntity entity);

    @Query("DELETE FROM otpHistory WHERE description = :description")
    void deleteHistoryByDescription(String description);

    @Query("DELETE FROM otpHistory")
    void deleteAllData();

    @Update
    void updateHistory(OtpHistoryEntity entity);

    @Query("SELECT * FROM otpHistory")
    List<OtpHistoryEntity> getHistoryList();
}
