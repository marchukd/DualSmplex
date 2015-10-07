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

public class EnterDataActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout customLayout;
    private int VAR_COUNT;
    private int LIM_COUNT;
    private Fraction[][] koef_limits;
    private String[] bazis;
    private Fraction[] free_vars;
    private Fraction[] opinions;
    private Fraction[] koef_function;
    private int[] signs_of_inequality;
    private Fraction value_of_function;
    public static int GREATER_OR_EQUAL = 0;
    public static int LESS_THAN_OR_EQUAL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);
        VAR_COUNT = getIntent().getIntExtra("VAR_COUNT", 3);
        LIM_COUNT = getIntent().getIntExtra("LIM_COUNT", 3);
        koef_limits = new Fraction[LIM_COUNT][VAR_COUNT];
        signs_of_inequality = new int[LIM_COUNT];
        free_vars = new Fraction[LIM_COUNT];
        koef_function = new Fraction[VAR_COUNT];
        bazis = new String[LIM_COUNT];
        opinions = new Fraction[VAR_COUNT];
        value_of_function = new Fraction(0, 1);


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
        btProcess.setText("Вирішити");
        btProcess.setOnClickListener(this);
        customLayout.addView(btProcess);
        ((ScrollView) findViewById(R.id.scrollView)).addView(customLayout);
    }

    @Override
    public void onClick(View v) {
        LinearLayout col;
        int index = 0;
        int index_in_opinions = 0;
        for (int i = 1; i < customLayout.getChildCount() - 1; i++) {
            Log.d("ITEM", i + "");
            if (i == customLayout.getChildCount() - 3) continue;
            LinearLayout row = (LinearLayout) customLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                if (j == row.getChildCount() - 2 || (j == row.getChildCount() - 1 && i == customLayout.getChildCount() - 2))
                    continue;
                EditText closet = (EditText) row.getChildAt(j);
                if (i == customLayout.getChildCount() - 2) {
                    koef_function[index_in_opinions++] = new Fraction(Fraction.parse(closet.getText().toString()));
                    continue;
                }
                if (j == row.getChildCount() - 1) {
                    free_vars[index] = new Fraction(Fraction.parse(closet.getText().toString()));
                    continue;
                }
                koef_limits[index][j] = new Fraction(Fraction.parse(closet.getText().toString()));
            }
            index++;
        }
        start_process();
    }

    private void start_process() {
        checkBazis();
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
        builder.create().show();
    }

    private boolean checkBazis() {
        int count_bazis_vars = 0;
        int index = 0;
        for (int i = 0; i < koef_limits[0].length; i++) {
            int count_1 = 0;
            int count_0 = 0;
            for (int j = 0; j < koef_limits.length; j++) {
                if (koef_limits[j][i].compare(new Fraction(0, 1)) == 0)
                    count_0++;
                if (koef_limits[j][i].compare(new Fraction(1, 1)) == 0)
                    count_1++;
            }
            if ((count_0 + count_1) == LIM_COUNT && (count_1 == 1)) {
                bazis[index++] = String.valueOf(i + 1);
                count_bazis_vars++;
            }
        }
        return count_bazis_vars == LIM_COUNT;
    }

    private boolean isNegativeFreeVar() {
        for (int i = 0; i < free_vars.length; i++)
            if (free_vars[i].compare(new Fraction(0, 1)) == -1)
                return true;
        return false;
    }

    private boolean isEmptyMPR() {
        ArrayList<Boolean> isNegativeEl = new ArrayList<Boolean>();
        for (int i = 0; i < LIM_COUNT; i++) {
            if (free_vars[i].compare(new Fraction(0, 1)) == -1) {
                int countNegative = 0;
                for (int j = 0; j < VAR_COUNT; j++)
                    if (koef_limits[i][j].compare(new Fraction(0, 1)) == -1)
                        countNegative++;
                isNegativeEl.add(countNegative == 0);
            }
        }
        for (Boolean b : isNegativeEl)
            if (b)
                return true;
        return false;
    }

    void privedennya() {
        for (int i = 0; i < koef_limits.length; i++)
            if (signs_of_inequality[i] == GREATER_OR_EQUAL)
                for (int j = 0; j < koef_limits[i].length; j++)
                    koef_limits[i][j] = koef_limits[i][j].multiply(new Fraction(-1, 1));
        for (int count = 0; count < koef_limits.length; count++) {
            for (int i = 0; i < koef_limits.length; i++) {
                Fraction[] row = new Fraction[koef_limits[i].length + 1];
                for (int k = 0; k < koef_limits[i].length; k++)
                    row[k] = koef_limits[i][k];
                row[koef_limits[i].length] = i == count ? new Fraction(1, 1) : new Fraction(0, 1);
                koef_limits[i] = row;
            }
        }
    }
}
