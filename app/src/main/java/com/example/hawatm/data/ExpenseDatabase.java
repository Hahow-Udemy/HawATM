package com.example.hawatm.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database (entities = {Expense.class}, version = 1)
public abstract class ExpenseDatabase extends RoomDatabase {
    public abstract ExpenseDao expenseDao();
    private static ExpenseDatabase instance = null;

    public static ExpenseDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, ExpenseDatabase.class, "expense.db")
                    .build();
        }
        return instance;
    }
}
