package com.example.dmytro.dualsmplex;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class SelectMethodActivity extends Activity {
    public static String SIMPLEX_METHOD = "SIMPLEX_METHOD";
    public static String DUAL_SIMPLEX_METHOD = "DUAL_SIMPLEX_METHOD";
    public static String HOMORI = "HOMORI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_method);
    }

    public void onSelectMethod(View view) {
        Class<EnterVarLimCount> intentClass = EnterVarLimCount.class;
        Intent intent = new Intent();
        /*testcase
        ArrayList<List<Fraction>> _koef_limits = new ArrayList<>();
        List<Fraction> row1 = new ArrayList<>();
        row1.add(new Fraction(1, 1));
        row1.add(new Fraction(-1, 1));
        row1.add(new Fraction(1, 1));
        row1.add(new Fraction(0, 1));
        row1.add(new Fraction(0, 1));
        _koef_limits.add(row1);
        List<Fraction> row2 = new ArrayList<>();
        row2.add(new Fraction(2, 1));
        row2.add(new Fraction(1, 1));
        row2.add(new Fraction(0, 1));
        row2.add(new Fraction(1, 1));
        row2.add(new Fraction(0, 1));
        _koef_limits.add(row2);
        List<Fraction> row3 = new ArrayList<>();
        row3.add(new Fraction(-1, 1));
        row3.add(new Fraction(1, 1));
        row3.add(new Fraction(0, 1));
        row3.add(new Fraction(0, 1));
        row3.add(new Fraction(1, 1));
        _koef_limits.add(row3);
        ArrayList<Fraction> _free_vars = new ArrayList<>();
        _free_vars.add(new Fraction(3, 1));
        _free_vars.add(new Fraction(2, 1));
        _free_vars.add(new Fraction(1, 1));
        ArrayList<Fraction> _koef_of_function = new ArrayList<>();
        _koef_of_function.add(new Fraction(-1, 1));
        _koef_of_function.add(new Fraction(-4, 1));
        _koef_of_function.add(new Fraction(0, 1));
        _koef_of_function.add(new Fraction(1, 1));
        _koef_of_function.add(new Fraction(0, 1));
        Simplex simplex = new Simplex(_koef_limits, _free_vars, _koef_of_function);
        */
        switch (view.getId()) {
            case R.id.btSimplex:
                intent.putExtra("type_method", SIMPLEX_METHOD);
                break;
            case R.id.btDualSimplex:
                intent.putExtra("type_method", DUAL_SIMPLEX_METHOD);
                break;
        }
        intent.setClass(this, intentClass);
        startActivity(intent);
    }
}
