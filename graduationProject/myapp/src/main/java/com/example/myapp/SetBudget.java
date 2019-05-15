package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SetBudget extends Activity{

    protected TextView remainBudget;
    protected EditText setBudgetEdit;
    protected Button setBudgetBtn;
    protected Button okBtn;
    protected Button cancleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setbudget);

        remainBudget = findViewById(R.id.remainBudget);


        setBudgetEdit = findViewById(R.id.setBudgetEt);
        setBudgetEdit.requestFocus();
        okBtn = findViewById(R.id.setBudgetOkBtn);
        cancleBtn = findViewById(R.id.setBudgetCancleBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budgetStr = String.valueOf(setBudgetEdit.getText());
                Intent intent = new Intent();
                intent.putExtra("Budget", budgetStr);
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
;
    }
}
