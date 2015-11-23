package com.example.dmytro.dualsmplex;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnterVarLimCount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private boolean isParseString(String str) {
        Pattern p = Pattern.compile("^([1-9])+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public void onClick(View view) {
        EditText number1 = (EditText) findViewById(R.id.number1);
        EditText number2 = (EditText) findViewById(R.id.number2);
        if (isParseString(number1.getText().toString()) && isParseString(number2.getText().toString())) {
            int num1 = Integer.parseInt(number1.getText().toString());
            int num2 = Integer.parseInt(number2.getText().toString());
            Intent intent = new Intent();
            intent.putExtra("VAR_COUNT", num1);
            intent.putExtra("LIM_COUNT", num2);
            String type_of_method = getIntent().getStringExtra("type_method");
            intent.setClass(this, GenerationGridActivity.class);
            intent.putExtra("TYPE", type_of_method);
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Некоректно заповнені дані!").show();
        }
    }
}
