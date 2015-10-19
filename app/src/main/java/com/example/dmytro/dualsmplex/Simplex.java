package com.example.dmytro.dualsmplex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmytro on 18.10.2015.
 */
public class Simplex {
    public static int EMPTY_MPR = -1;
    public static int OK = 0;
    public static int TASK_STATE = OK;

    private Fraction ZERO = new Fraction(0, 1);
    private Fraction ONE = new Fraction(1, 1);

    private int VAR_COUNT;
    private int LIMIT_COUNT;

    private ArrayList<List<Fraction>> koef_limits;
    private List<Fraction> free_vars;
    private ArrayList<Fraction> opinions;
    private Fraction value_of_function;
    private List<Fraction> koef_of_function;
    private ArrayList<Integer> bazis;
    private Fraction[] result_point;

    public Simplex(ArrayList<List<Fraction>> _koef_limits, List<Fraction> _free_vars, List<Fraction> _koef_of_function) {
        koef_limits = _koef_limits;
        free_vars = _free_vars;
        koef_of_function = _koef_of_function;
        LIMIT_COUNT = koef_limits.size();
        VAR_COUNT = koef_limits.get(0).size();
        bazis = new ArrayList<>();
        opinions = new ArrayList<>();
        result_point = new Fraction[VAR_COUNT];
        setBazis();
        make_opinions(_koef_of_function);
        if (processing() == EMPTY_MPR)
            TASK_STATE = EMPTY_MPR;
    }

    private void make_opinions(List<Fraction> koef_function) {
        Fraction _valueOfFunction = new Fraction(ZERO);
        for (int i = 0; i < VAR_COUNT; i++) {
            Fraction item_opinion = new Fraction(ZERO);
            for (int j = 0; j < LIMIT_COUNT; j++)
                item_opinion = item_opinion.plus(koef_limits.get(j).get(i).multiply(koef_of_function.get(bazis.get(j) - 1)));
            opinions.add(item_opinion.minus(koef_function.get(i)));
        }
        for (int i = 0; i < LIMIT_COUNT; i++)
            _valueOfFunction = _valueOfFunction.plus(free_vars.get(i).multiply(koef_of_function.get(bazis.get(i) - 1)));
        value_of_function = _valueOfFunction;
    }

    private void setBazis() {
        for (int i = 0; i < VAR_COUNT; i++) {
            int zero = 0, one = 0;
            for (int j = 0; j < LIMIT_COUNT; j++)
                if (koef_limits.get(j).get(i).compare(ZERO) == 0)
                    zero++;
                else if (koef_limits.get(j).get(i).compare(ONE) == 0)
                    one++;
            if (zero + one == LIMIT_COUNT)
                bazis.add(i + 1);
        }
    }

    private int checkOpinions() {
        int posOpinion = 0;
        for (int i = 0; i < VAR_COUNT; i++) {
            if (opinions.get(i).compare(ZERO) == 1) {
                int positive = 0;
                for (int j = 0; j < LIMIT_COUNT; j++)
                    if (koef_limits.get(j).get(i).compare(ZERO) == 1)
                        positive++;
                if (positive != 0)
                    posOpinion++;
                else if (positive == 0)
                    return -1;
            }
        }
        return posOpinion;
    }

    private int processing() {
        while (this.checkOpinions() > 0) {
            int max = maxOpinion();
            int min = minOpinion(max);
            bazis.set(min, max + 1);

            Fraction element = koef_limits.get(min).get(max);
            Fraction mnoz;
            for (int i = 0; i < VAR_COUNT; i++)
                koef_limits.get(min).set(i, koef_limits.get(min).get(i).divide(element));
            free_vars.set(min, free_vars.get(min).divide(element));
            for (int i = 0; i < LIMIT_COUNT; i++) {
                if (i == min || koef_limits.get(i).get(max).compare(ZERO) == 0) continue;
                mnoz = koef_limits.get(i).get(max).divide(new Fraction(-1, 1));
                for (int j = 0; j < VAR_COUNT; j++)
                    koef_limits.get(i).set(j, koef_limits.get(min).get(j).multiply(mnoz).plus(koef_limits.get(i).get(j)));
                free_vars.set(i, free_vars.get(min).multiply(mnoz).plus(free_vars.get(i)));
            }
            mnoz = opinions.get(max).divide(new Fraction(-1, 1));
            for (int i = 0; i < VAR_COUNT; i++)
                opinions.set(i, koef_limits.get(min).get(i).multiply(mnoz).plus(opinions.get(i)));
            value_of_function = free_vars.get(min).multiply(mnoz).plus(value_of_function);
            if (checkOpinions() == -1)
                return EMPTY_MPR;
        }
        for (int i = 0; i < VAR_COUNT; i++)
            result_point[i] = new Fraction(0, 1);
        for (int i = 0; i < LIMIT_COUNT; i++)
            result_point[bazis.get(i) - 1] = free_vars.get(i);
        return OK;
    }

    private int minOpinion(int maxOpinion) {
        ArrayList<Integer> indexPos = new ArrayList<>();
        for (int i = 0; i < LIMIT_COUNT; i++)
            if (koef_limits.get(i).get(maxOpinion).compare(ZERO) == 1)
                indexPos.add(i);
        int startPos = indexPos.get(0);
        Fraction minRelation = free_vars.get(startPos).divide(koef_limits.get(startPos).get(maxOpinion));
        int index = startPos;
        for (int i = 0; i < indexPos.size(); i++)
            if (free_vars.get(indexPos.get(i)).divide(koef_limits.get(indexPos.get(i)).get(maxOpinion)).compare(minRelation) == -1) {
                minRelation = free_vars.get(indexPos.get(i)).divide(koef_limits.get(indexPos.get(i)).get(maxOpinion));
                index = indexPos.get(i);
            }
        return index;
    }

    private int maxOpinion() {
        int index = 0;
        Fraction f = opinions.get(index);
        for (int i = 0; i < opinions.size(); i++)
            if (opinions.get(i).compare(f) == 1) {
                f = opinions.get(i);
                index = i;
            }
        return index;
    }
}
