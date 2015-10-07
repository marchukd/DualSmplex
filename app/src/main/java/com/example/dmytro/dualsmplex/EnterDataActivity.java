package com.example.dmytro.dualsmplex;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
            TextView twEquals = new TextView(this);
            twEquals.setText("=");
            left.addView(twEquals);
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
        fX.addView(tw);
        TextView twEquals = new TextView(this);
        twEquals.setText("min");
        fX.addView(twEquals);
        customLayout.addView(fX);
        Button btProcess = new Button(this);
        btProcess.setText("Розв'язати");
        btProcess.setOnClickListener(this);
        customLayout.addView(btProcess);
        ((ScrollView) findViewById(R.id.scrollView)).addView(customLayout);
    }

    @Override
    public void onClick(View v) {
        for (int i = 1; i < customLayout.getChildCount() - 1; i++) {
            Log.d("ITEM", i + "");
            if (i == customLayout.getChildCount() - 3) continue;
            LinearLayout row = (LinearLayout) customLayout.getChildAt(i);
            List<Fraction> list_row = new ArrayList<>();
            for (int j = 0; j < row.getChildCount(); j++) {
                if (j == row.getChildCount() - 2 || (j == row.getChildCount() - 1 && i == customLayout.getChildCount() - 2))
                    continue;
                EditText closet = (EditText) row.getChildAt(j);
                if (i == customLayout.getChildCount() - 2) {
                    koef_of_func.add(new Fraction(Fraction.parse(closet.getText().toString())));
                    continue;
                }
                if (j == row.getChildCount() - 1) {
                    free_vars.add(new Fraction(Fraction.parse(closet.getText().toString())));
                    continue;
                }
                list_row.add(new Fraction(Fraction.parse(closet.getText().toString())));
            }
            if (list_row.size() != 0)
                koef_limits.add(list_row);
        }
        start_process();
    }

    private void start_process() {
        ArrayList<List<Fraction>> t_koef_limits = new ArrayList<>();
        List<Fraction> row1 = new ArrayList<>();
        row1.add(new Fraction(1, 1));
        row1.add(new Fraction(2, 1));
        row1.add(new Fraction(-1, 1));
        row1.add(new Fraction(0, 1));
        t_koef_limits.add(row1);
        List<Fraction> row2 = new ArrayList<>();
        row2.add(new Fraction(2, 1));
        row2.add(new Fraction(1, 1));
        row2.add(new Fraction(1, 1));
        row2.add(new Fraction(1, 1));
        t_koef_limits.add(row2);
        List<Fraction> t_free_vars = Arrays.asList(new Fraction(3, 1), new Fraction(2, 1));
        List<Fraction> t_koef_of_func = new ArrayList<>();
        t_koef_of_func.add(new Fraction(6, 1));
        t_koef_of_func.add(new Fraction(8, 1));
        t_koef_of_func.add(new Fraction(1, 1));
        t_koef_of_func.add(new Fraction(1, 1));
        List<String> t_signs = Arrays.asList(new String(">="), new String(">="));
        DualSimplex dualSimplex = new DualSimplex(t_koef_limits, t_free_vars, t_koef_of_func, t_signs);
        /*checkBazis();
        for (int i = 0; i < VAR_COUNT; i++)
            opinions[i] = koef_function[i].multiply(new Fraction(-1, 1));
        while (isNegativeFreeVar()) {
            if (isEmptyMPR()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("МПР - порожня");
                builder.create().show();
                return;
            }
            //мін з вільних членів
            Fraction min = free_vars[0];
            int exit = 0;
            for (int i = 0; i < free_vars.length; i++)
                if (free_vars[i].compare(min) == -1) {
                    min = free_vars[i];
                    exit = i;
                }
            Fraction min_of_relations = null;
            int enter = 0;
            for (int i = 0; i < VAR_COUNT; i++) {
                if (koef_limits[exit][i].compare(new Fraction(0, 1)) == -1)
                    if (min_of_relations == null) {
                        min_of_relations = opinions[i].divide(koef_limits[exit][i]);
                        enter = i + 1;
                    } else if (min_of_relations.compare(opinions[i].divide(koef_limits[exit][i])) == 1) {
                        min_of_relations = opinions[i].divide(koef_limits[exit][i]);
                        enter = i + 1;
                    }
            }
            bazis[exit] = String.valueOf(enter);
            Fraction element = koef_limits[exit][enter - 1];
            for (int i = 0; i < VAR_COUNT; i++)
                koef_limits[exit][i] = koef_limits[exit][i].divide(element);
            free_vars[exit] = free_vars[exit].divide(element);
            for (int i = 0; i < LIM_COUNT; i++) {
                if (i == exit) continue;
                if (koef_limits[i][enter - 1].compare(new Fraction(0, 1)) == 0) continue;
                Fraction divider = koef_limits[i][enter - 1].multiply(new Fraction(-1, 1));
                for (int j = 0; j < VAR_COUNT; j++)
                    koef_limits[i][j] = koef_limits[exit][j].multiply(divider).plus(koef_limits[i][j]);
                free_vars[i] = free_vars[exit].multiply(divider).plus(free_vars[i]);
            }
            Fraction divider = opinions[enter - 1].multiply(new Fraction(-1, 1));
            for (int i = 0; i < VAR_COUNT; i++)
                opinions[i] = koef_limits[exit][i].multiply(divider).plus(opinions[i]);
            value_of_function = free_vars[exit].multiply(divider).plus(value_of_function);
        }
        StringBuilder result = new StringBuilder("x*(");
        Fraction[] coords = new Fraction[VAR_COUNT];
        for (int i = 0; i < VAR_COUNT; i++)
            coords[i] = new Fraction(0, 1);
        for (int i = 0; i < LIM_COUNT; i++)
            coords[Integer.valueOf(bazis[i]) - 1] = free_vars[i];

        for (int i = 0; i < VAR_COUNT; i++)
            result.append(coords[i] + "; ");
        result = new StringBuilder(result.substring(0, result.length() - 2));
        result.append("); f(x*)=" + value_of_function);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(result);
        builder.create().show();*/
    }
}
