package com.example.dmytro.dualsmplex;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GenerationGridActivity extends AppCompatActivity implements Button.OnClickListener {
    LinearLayout lLayout;
    GridLayout layout;
    @Bind(R.id.enter_dest_func)
    GridLayout layout_dest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generation_grid);
        int VARCOUNT = getIntent().getIntExtra("VAR_COUNT", 3);
        int LIMITCOUNT = getIntent().getIntExtra("LIM_COUNT", 2);
        lLayout = (LinearLayout) findViewById(R.id.linearLayout);
        lLayout.addView(createGridEditView(VARCOUNT, LIMITCOUNT));
        lLayout.addView(createGridDestinationFunction(VARCOUNT));
        lLayout.addView(createProcessingButton());
        layout = (GridLayout) findViewById(R.id.enter_matrix);
        ButterKnife.bind(this);
    }

    private GridLayout createGridEditView(int _varcount, int _limcount) {
        GridLayout layout = new GridLayout(this);
        layout.setColumnCount(_varcount + 2);
        layout.setRowCount(_limcount + 1);
        for (int i = 0; i < layout.getColumnCount(); i++) {
            if (i == layout.getColumnCount() - 2) {
                layout.addView(new TextView(this));
                continue;
            }
            if (i == layout.getColumnCount() - 1) {
                TextView textView = new TextView(this);
                textView.setText("в.ч.");
                layout.addView(textView);
                continue;
            }
            TextView view = new TextView(this);
            view.setText("x" + (i + 1));
            layout.addView(view);
        }
        for (int i = 1; i < layout.getRowCount(); i++)
            for (int j = 0; j < layout.getColumnCount(); j++) {
                if (j == layout.getColumnCount() - 2) {
                    Spinner signSpinner = new Spinner(this);
                    String[] signs = new String[]{};
                    if (getIntent().getStringExtra("TYPE").equals(SelectMethodActivity.SIMPLEX_METHOD))
                        signs = new String[]{"="};
                    else if (getIntent().getStringExtra("TYPE").equals(SelectMethodActivity.DUAL_SIMPLEX_METHOD))
                        signs = new String[]{"<=", "=", ">="};
                    signSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, signs));
                    layout.addView(signSpinner);
                    continue;
                }
                layout.addView(new EditText(this));
            }
        layout.setId(R.id.enter_matrix);
        return layout;
    }

    private GridLayout createGridDestinationFunction(int _varcount) {
        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(_varcount + 1);
        grid.setRowCount(2);
        for (int i = 0; i < _varcount; i++) {
            TextView textView = new TextView(this);
            textView.setText("x" + (i + 1));
            grid.addView(textView);
        }
        grid.addView(new TextView(this));
        for (int i = 0; i < _varcount; i++) {
            grid.addView(new EditText(this));
        }
        TextView tw = new TextView(this);
        tw.setText("->min");
        grid.addView(tw);
        grid.setId(R.id.enter_dest_func);
        return grid;
    }

    private Button createProcessingButton() {
        Button button = new Button(this);
        button.setOnClickListener(this);
        button.setText("розв'язати");
        return button;
    }

    private ArrayList<ArrayList<Fraction>> getKoeficientsOfLimits() {
        ArrayList<ArrayList<Fraction>> data = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < layout.getRowCount(); i++) {
            ArrayList<Fraction> one_row = new ArrayList<>();
            for (int j = 0; j < layout.getColumnCount(); j++) {
                if (layout.getChildAt(index) instanceof EditText) {
                    one_row.add(Fraction.parse(((EditText) layout.getChildAt(index)).getText().toString()));
                }
                index++;
            }
            if (one_row.size() != 0) {
                one_row.remove(one_row.size() - 1);
                data.add(one_row);
            }
        }
        return data;
    }

    private ArrayList<Fraction> getFreeVars() {
        ArrayList<Fraction> free = new ArrayList<>();
        for (int i = layout.getColumnCount() * 2 - 1; i < layout.getColumnCount() * layout.getRowCount(); i += layout.getColumnCount()) {
            free.add(new Fraction(Fraction.parse(((EditText) layout.getChildAt(i)).getText().toString())));
        }
        return free;
    }

    private ArrayList<Fraction> getVarsOfFunc() {
        ArrayList<Fraction> data = new ArrayList<>();
        for (int i = 0; i < layout_dest.getChildCount(); i++)
            if (layout_dest.getChildAt(i) instanceof EditText)
                data.add(new Fraction(Fraction.parse(((EditText) layout_dest.getChildAt(i)).getText().toString())));
        return data;
    }

    private ArrayList<String> getSignsOfLimits() {
        ArrayList<String> signs = new ArrayList<>();
        if (getIntent().getStringExtra("TYPE").equals(SelectMethodActivity.HOMORI))
            return signs;
        for (int i = layout.getColumnCount() * 2 - 2; i < layout.getColumnCount() * layout.getRowCount(); i += layout.getColumnCount()) {
            Spinner spinner = (Spinner) layout.getChildAt(i);
            String signItem = spinner.getSelectedItem().toString();
            signs.add(signItem);
        }
        return signs;
    }

    private boolean isValidEnterData() {
        for (int i = 0; i < layout.getChildCount(); i++) {
            if (layout.getChildAt(i) instanceof EditText) {
                EditText field = (EditText) layout.getChildAt(i);
                if (!isParseString(field.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Некоректно введено дані!").show();
                    field.requestFocus();
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isParseString(String testString) {
        Pattern p = Pattern.compile("^[-]{0,1}[0-9]+(/[1-9]+[0-9]*){0,1}$");
        Matcher m = p.matcher(testString);
        return m.matches();
    }

    @Override
    public void onClick(View v) {
        if (isValidEnterData()) {
            ArrayList<ArrayList<Fraction>> koeficients = getKoeficientsOfLimits();
            ArrayList<Fraction> free = getFreeVars();
            ArrayList<Fraction> koef_of_func = getVarsOfFunc();
            ArrayList<String> signs = getSignsOfLimits();
            String method = getIntent().getStringExtra("TYPE");

            String resultMessage = "";
            if (method.equals(SelectMethodActivity.SIMPLEX_METHOD)) {
                Simplex simplex = new Simplex(koeficients, free, koef_of_func);
                if (simplex.getState() == Simplex.OK) {
                    resultMessage = getResultat(simplex);
                } else if (simplex.getState() == Simplex.EMPTY_MPR) {
                    resultMessage = "МПР = Ø";
                }
            }
            if (method.equals(SelectMethodActivity.DUAL_SIMPLEX_METHOD)) {
                DualSimplex dualSimplex = new DualSimplex(koeficients, free, koef_of_func, signs);
                if (dualSimplex.getState() == Simplex.OK) {
                    resultMessage = getResultat(dualSimplex);
                } else if (dualSimplex.getState() == Simplex.EMPTY_MPR) {
                    resultMessage = "МПР = Ø";
                }
            }
            if (method.equals(SelectMethodActivity.HOMORI)) {
                Simplex simplex = new Simplex(koeficients, free, koef_of_func);
                if (simplex.getState() == Simplex.OK) {
                    Homori homori = new Homori(simplex);
                    resultMessage = getResultat(homori);
                } else if (simplex.getState() == Simplex.EMPTY_MPR) {
                    resultMessage = "МПР = Ø";
                }
            }
            new AlertDialog.Builder(this).setMessage(resultMessage).show();
        }
    }

    String getResultat(BaseMethod baseMethod) {
        Fraction[] resPoint = baseMethod.resultationPoint;
        StringBuilder resultat = new StringBuilder("x*(");
        for (Fraction f : resPoint)
            resultat.append(f + ";");
        resultat.deleteCharAt(resultat.length() - 1);
        resultat.append("); f(x*) = " + baseMethod.valueOfFunction);
        return resultat.toString();
    }
}