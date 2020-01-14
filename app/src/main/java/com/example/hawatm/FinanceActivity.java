package com.example.hawatm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hawatm.data.Expense;
import com.example.hawatm.data.ExpenseDatabase;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FinanceActivity extends AppCompatActivity {

    private static final String TAG = FinanceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        //database 1
//        final ExpenseDatabase database =  Room.databaseBuilder(
//                FinanceActivity.this, ExpenseDatabase.class, "expense.db").build();

        //database2
        final ExpenseDatabase database = ExpenseDatabase.getInstance(FinanceActivity.this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.expenseDao().insert(new Expense("2020-01-03", "breakfirst", 50));
                database.expenseDao().insert(new Expense("2020-01-07", "parking", 75));
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_expense_to_firebase){
            final String userid = getSharedPreferences("atm", MODE_PRIVATE)
                    .getString("USERID", null);
            if(userid != null){

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Expense> expenses =  ExpenseDatabase.getInstance(FinanceActivity.this)
                                .expenseDao()
                                .getAll();

                        FirebaseDatabase.getInstance()
                                .getReference("users")
                                .child(userid)
                                .child("expenses")
                                .setValue(expenses);

                    }
                });

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finance, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
