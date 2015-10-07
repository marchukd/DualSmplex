package com.example.dmytro.dualsmplex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmytro on 07.10.2015.
 */
public class DualSimplex {
    private int VAR_COUNT;
    private int LIMIT_COUNT;
    private ArrayList<List<Fraction>> koef_limits;
    private List<Fraction> free_vars;
    private ArrayList<Fraction> opinions;
    private Fraction value_of_function;
    private List<Fraction> koef_of_function;
    private ArrayList<Integer> bazis;
    private String resultat;

    public DualSimplex(ArrayList<List<Fraction>> _koef_limits, List<Fraction> _free_vars, List<Fraction> _koef_of_function, List<String> signs) {
        koef_limits = _koef_limits;
        LIMIT_COUNT = koef_limits.size();
        VAR_COUNT = koef_limits.get(0).size();
        free_vars = _free_vars;
        bazis = new ArrayList<>();
        koef_of_function = _koef_of_function;
        opinions = new ArrayList<>();
        resultat = new String();

        convert_enter_data(signs);
        make_opinions(_koef_of_function);
        processing();
    }

    private void convert_enter_data(List<String> signs) {
        int index_of_bazis_var = VAR_COUNT;
        for (int i = 0; i < LIMIT_COUNT; i++)
            if (signs.get(i).toString().equals(">=")) {
                for (int j = 0; j < VAR_COUNT; j++)
                    koef_limits.get(i).set(j, koef_limits.get(i).get(j).multiply(new Fraction(-1, 1)));
                free_vars.set(i, free_vars.get(i).multiply(new Fraction(-1, 1)));
            }
        for (int i = 0; i < LIMIT_COUNT; i++) {
            for (int j = 0; j < LIMIT_COUNT; j++) {
                if (j == i) {
                    koef_limits.get(j).add(new Fraction(1, 1));
                    bazis.add(new Integer(index_of_bazis_var++ + 1));
                } else
                    koef_limits.get(j).add(new Fraction(0, 1));
            }
            koef_of_function.add(new Fraction(0, 1));
            VAR_COUNT++;
        }
    }

    private void processing() {
        while (isNegativeFreeVar()) {
            if (isEmptyMPR()) {
                resultat = "МПР - порожня";
                return;
            }
            //мін з вільних членів
            Fraction min = free_vars.get(0);
            int exit = 0;
            for (int i = 0; i < LIMIT_COUNT; i++)
                if (free_vars.get(i).compare(min) == -1) {
                    min = free_vars.get(i);
                    exit = i;
                }
            Fraction min_of_relations = null;
            int enter = 0;
            for (int i = 0; i < VAR_COUNT; i++) {
                if (koef_limits.get(exit).get(i).compare(new Fraction(0, 1)) == -1)
                    if (min_of_relations == null) {
                        min_of_relations = opinions.get(i).divide(koef_limits.get(exit).get(i));
                        enter = i + 1;
                    } else if (min_of_relations.compare(opinions.get(i).divide(koef_limits.get(exit).get(i))) == 1) {
                        min_of_relations = opinions.get(i).divide(koef_limits.get(exit).get(i));
                        enter = i + 1;
                    }
            }
            bazis.set(exit, Integer.valueOf(String.valueOf(enter)));
            Fraction element = koef_limits.get(exit).get(enter - 1);
            for (int i = 0; i < VAR_COUNT; i++)
                koef_limits.get(exit).set(i, koef_limits.get(exit).get(i).divide(element));
            free_vars.set(exit, free_vars.get(exit).divide(element));
            for (int i = 0; i < LIMIT_COUNT; i++) {
                if (i == exit) continue;
                if (koef_limits.get(i).get(enter - 1).compare(new Fraction(0, 1)) == 0) continue;
                Fraction divider = koef_limits.get(i).get(enter - 1).multiply(new Fraction(-1, 1));
                for (int j = 0; j < VAR_COUNT; j++)
                    koef_limits.get(i).set(j, koef_limits.get(exit).get(j).multiply(divider).plus(koef_limits.get(i).get(j)));
                free_vars.set(i, free_vars.get(exit).multiply(divider).plus(free_vars.get(i)));
            }
            Fraction divider = opinions.get(enter - 1).multiply(new Fraction(-1, 1));
            for (int i = 0; i < VAR_COUNT; i++)
                opinions.set(i, koef_limits.get(exit).get(i).multiply(divider).plus(opinions.get(i)));
            value_of_function = free_vars.get(exit).multiply(divider).plus(value_of_function);
        }
        StringBuilder result = new StringBuilder("x*(");
        Fraction[] coords = new Fraction[VAR_COUNT];
        for (int i = 0; i < VAR_COUNT; i++)
            coords[i] = new Fraction(0, 1);
        for (int i = 0; i < LIMIT_COUNT; i++)
            coords[Integer.valueOf(bazis.get(i)) - 1] = free_vars.get(i);

        for (int i = 0; i < VAR_COUNT; i++)
            result.append(coords[i] + "; ");
        result = new StringBuilder(result.substring(0, result.length() - 2));
        result.append("); f(x*)=" + value_of_function);
        resultat = new String(result);
    }

    private boolean isNegativeFreeVar() {
        for (int i = 0; i < LIMIT_COUNT; i++)
            if (free_vars.get(i).compare(new Fraction(0, 1)) == -1)
                return true;
        return false;
    }

    private boolean isEmptyMPR() {
        ArrayList<Boolean> isNegativeEl = new ArrayList<>();
        for (int i = 0; i < LIMIT_COUNT; i++) {
            if (free_vars.get(i).compare(new Fraction(0, 1)) == -1) {
                int countNegative = 0;
                for (int j = 0; j < VAR_COUNT; j++)
                    if (koef_limits.get(i).get(j).compare(new Fraction(0, 1)) == -1)
                        countNegative++;
                isNegativeEl.add(countNegative == 0);
            }
        }
        for (Boolean b : isNegativeEl)
            if (b)
                return true;
        return false;
    }

    private void make_opinions(List<Fraction> koef_function) {
        Fraction _valueOfFunction = new Fraction(0, 1);
        for (int i = 0; i < VAR_COUNT; i++) {
            Fraction item_opinion = new Fraction(0, 1);
            for (int j = 0; j < LIMIT_COUNT; j++)
                item_opinion = item_opinion.multiply(koef_of_function.get(bazis.get(j) - 1));
            opinions.add(item_opinion.minus(koef_function.get(i)));
        }
        for (int i = 0; i < LIMIT_COUNT; i++)
            _valueOfFunction = _valueOfFunction.plus(free_vars.get(i).multiply(koef_of_function.get(bazis.get(i) - 1)));
        value_of_function = _valueOfFunction;
    }

    public String getResultat() {
        return resultat;
    }
}
