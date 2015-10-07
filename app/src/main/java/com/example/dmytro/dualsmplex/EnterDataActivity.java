package com.example.dmytro.dualsmplex;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnterDataActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout customLayout;
    private int VAR_COUNT;
    private int LIM_COUNT;
    private ArrayList<List<Fraction>> koef_limits;
    private List<Fraction> free_vars;
    private List<Fraction> koef_of_func;
    private List<String> signs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);
        VAR_COUNT = getIntent().getIntExtra("VAR_COUNT", 3);
        LIM_COUNT = getIntent().getIntExtra("LIM_COUNT", 3);
        koef_limits = new ArrayList<>();
        free_vars = new ArrayList<>();
        koef_of_func = new ArrayList<>();

        customLayout = new LinearLayout(this);
        customLayout.setOrientation(LinearLayout.VERTICAL);
        TextView header1 = new TextView(this);
        header1.setText("Обмеження");
        customLayout.addView(header1);

        for (int i = 0; i < LIM_COUNT; i++) {
            LinearLayout left = new LinearLayout(this);
            left.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < VAR_COUNT; j++) {
                EditText ew1 = new EditText(this);
                ew1.setHint("x" + (j + 1));
                left.addView(ew1);
            }
            //spinner
            Spinner spinner = new Spinner(this);
            String[] items = {">=", "<="};
            spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
            spinner.setLayoutParams(new ActionBar.LayoutParams(110, 60));
            left.addView(spinner);
            //end spinner
            EditText freeVar = new EditText(this);
            freeVar.setHint("в.ч.");
            left.addView(freeVar);
            customLayout.addView(left);
        }

        TextView header2 = new TextView(this);
        header2.setText("Функція цілі");
        customLayout.addView(header2);

        LinearLayout fX = new LinearLayout(this);
        fX.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < VAR_COUNT; i++) {
            EditText et = new EditText(this);
            et.setHint("x" + (i + 1));
            fX.addView(et);
        }
        TextView tw = new TextView(this);
        tw.setText("->");
        tw.setLayoutParams(new ActionBar.LayoutParams(110, 60));
        fX.addView(tw);
        TextView twEquals = new TextView(this);
        twEquals.setText("min");
        fX.addView(twEquals);
        Spinner spinner = new Spinner(this);

        //end spinner min or max
        customLayout.addView(fX);
        Button btProcess = new Button(this);
        btProcess.setText("Розв'язати");
        btProcess.setOnClickListener(this);
        customLayout.addView(btProcess);
        ((ScrollView) findViewById(R.id.scrollView)).addView(customLayout);
    }

    @Override
    public void onClick(View v) {
        koef_limits.clear();
        free_vars.clear();
        koef_of_func.clear();
        List<String> signs = new ArrayList<>();

        for (int i = 1; i < customLayout.getChildCount() - 1; i++) {
            if (i == customLayout.getChildCount() - 3) continue;
            if (i == customLayout.getChildCount() - 2) {
                LinearLayout func_values = (LinearLayout) customLayout.getChildAt(i);
                for (int j = 0; j < func_values.getChildCount() - 2; j++) {
                    EditText editText = (EditText) func_values.getChildAt(j);
                    koef_of_func.add(new Fraction(Fraction.parse(editText.getText().toString())));
                }
                continue;
            }
            LinearLayout linearLayout = (LinearLayout) customLayout.getChildAt(i);
            List<Fraction> row = new ArrayList<>();
            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                System.out.println(linearLayout.getChildAt(j));
                if (j == linearLayout.getChildCount() - 1) {
                    EditText editText = (EditText) linearLayout.getChildAt(j);
                    free_vars.add(new Fraction(Fraction.parse(editText.getText().toString())));
                    continue;
                }
                if (j == linearLayout.getChildCount() - 2) {
                    Spinner sp = (Spinner) linearLayout.getChildAt(j);
                    signs.add(sp.getSelectedItem().toString());
                    continue;
                }
                EditText editText = (EditText) linearLayout.getChildAt(j);
                row.add(new Fraction(Fraction.parse(editText.getText().toString())));
            }
            koef_limits.add(row);
        }
        this.signs = signs;
        start_process();
    }

    private void start_process() {
        DualSimplex dualSimplex = new DualSimplex(koef_limits, free_vars, koef_of_func, signs);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(dualSimplex.getResultat()).create().show();
    }
}
