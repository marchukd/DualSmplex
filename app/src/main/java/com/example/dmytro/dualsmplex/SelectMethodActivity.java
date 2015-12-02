package com.example.dmytro.dualsmplex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
        switch (view.getId()) {
            case R.id.btSimplex:
                intent.putExtra("type_method", SIMPLEX_METHOD);
                break;
            case R.id.btDualSimplex:
                intent.putExtra("type_method", DUAL_SIMPLEX_METHOD);
                break;
            case R.id.btHomori:
                intent.putExtra("type_method", HOMORI);
                break;
        }
        intent.setClass(this, intentClass);
        startActivity(intent);
    }
}
