package com.example.hawatm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.os.Bundle;
import android.util.Log;

import com.example.hawatm.data.Expense;
import com.example.hawatm.data.ExpenseDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FinanceActivity extends AppCompatActivity {

    private static final String TAG = FinanceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        final ExpenseDatabase database =  Room.databaseBuilder(
                FinanceActivity.this, ExpenseDatabase.class, "expense.db").build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.expenseDao().insert(new Expense("2020-01-03", "breakfirst", 50));
//                database.expenseDao().insert(new Expense("2020-01-07", "parking", 75));
            }
        }).start();


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Expense> expenses =  database.expenseDao().getAll();
                for (Expense expense : expenses) {
                    Log.d(TAG, "Expense:" + expense.getDate() + "/" + expense.getInfo() + "/" + expense.getAmount());
                }
            }
        });


    }
}
