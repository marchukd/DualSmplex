package com.example.dmytro.dualsmplex;

import java.util.ArrayList;
import java.util.List;

public class DualSimplex extends BaseMethod {

    public DualSimplex(ArrayList<ArrayList<Fraction>> _koef_limits, ArrayList<Fraction> _free_vars, ArrayList<Fraction> _koef_of_function, ArrayList<String> signs) {
        setFields(_koef_limits, _free_vars, _koef_of_function);

        bringingEnterData(signs);
        initOpinions(_koef_of_function);
        TASK_STATE = start();
    }

    public DualSimplex(ArrayList<ArrayList<Fraction>> coefOfLimits, ArrayList<Fraction> freeVars, ArrayList<Fraction> opinions, Fraction valueOfFunction) {
        this.coefOfLimits = coefOfLimits;
        LIMIT_COUNT = coefOfLimits.size();
        VAR_COUNT = coefOfLimits.get(0).size();
        this.freeVars = freeVars;
        basis = new ArrayList<>();
        this.opinions = opinions;
        this.valueOfFunction = valueOfFunction;
        bringingEnterData(getSigns());
        TASK_STATE = start();
    }

    private void setFields(ArrayList<ArrayList<Fraction>> _koef_limits, ArrayList<Fraction> _free_vars, ArrayList<Fraction> _koef_of_function) {
        coefOfLimits = _koef_limits;
        LIMIT_COUNT = coefOfLimits.size();
        VAR_COUNT = coefOfLimits.get(0).size();
        freeVars = _free_vars;
        basis = new ArrayList<>();
        coefOfFunction = _koef_of_function;
        opinions = new ArrayList<>();
    }

    private void bringingEnterData(List<String> signs) {
        int index_of_bazis_var = VAR_COUNT;
        if (!signs.contains(">=") && !signs.contains("<=")) {
            initialBasis();
            return;
        }
        for (int i = 0; i < LIMIT_COUNT; i++)
            if (signs.get(i).equals(">=")) {
                for (int j = 0; j < VAR_COUNT; j++)
                    coefOfLimits.get(i).set(j, coefOfLimits.get(i).get(j).multiply(new Fraction(-1, 1)));
                freeVars.set(i, freeVars.get(i).multiply(new Fraction(-1, 1)));
            }
        for (int i = 0; i < LIMIT_COUNT; i++) {
            for (int j = 0; j < LIMIT_COUNT; j++) {
                if (j == i) {
                    coefOfLimits.get(j).add(new Fraction(1, 1));
                    basis.add(new Integer(index_of_bazis_var++ + 1));
                } else
                    coefOfLimits.get(j).add(new Fraction(0, 1));
            }
            coefOfFunction.add(new Fraction(0, 1));
            VAR_COUNT++;
        }
    }

    private void initialBasis() {
        for (int i = 0; i < VAR_COUNT; i++) {
            int zero = 0, one = 0;
            for (int j = 0; j < LIMIT_COUNT; j++)
                if (coefOfLimits.get(j).get(i).compare(new Fraction(0, 1)) == 0)
                    zero++;
                else if (coefOfLimits.get(j).get(i).compare(new Fraction(1, 1)) == 0)
                    one++;
            if (zero + one == LIMIT_COUNT)
                basis.add(i + 1);
        }
    }

    private int start() {
        while (isNegativeFreeVar()) {
            if (isEmptyMPR()) {
                return EMPTY_MPR;
            }
            //мін з вільних членів
            Fraction min = freeVars.get(0);
            int exit = 0;
            for (int i = 0; i < LIMIT_COUNT; i++)
                if (freeVars.get(i).compare(min) == -1) {
                    min = freeVars.get(i);
                    exit = i;
                }
            Fraction min_of_relations = null;
            int enter = 0;
            for (int i = 0; i < VAR_COUNT; i++) {
                if (coefOfLimits.get(exit).get(i).compare(new Fraction(0, 1)) == -1)
                    if (min_of_relations == null) {
                        min_of_relations = opinions.get(i).divide(coefOfLimits.get(exit).get(i));
                        enter = i + 1;
                    } else if (min_of_relations.compare(opinions.get(i).divide(coefOfLimits.get(exit).get(i))) == 1) {
                        min_of_relations = opinions.get(i).divide(coefOfLimits.get(exit).get(i));
                        enter = i + 1;
                    }
            }
            basis.set(exit, Integer.valueOf(String.valueOf(enter)));
            Fraction element = coefOfLimits.get(exit).get(enter - 1);
            for (int i = 0; i < VAR_COUNT; i++)
                coefOfLimits.get(exit).set(i, coefOfLimits.get(exit).get(i).divide(element));
            freeVars.set(exit, freeVars.get(exit).divide(element));
            for (int i = 0; i < LIMIT_COUNT; i++) {
                if (i == exit) continue;
                if (coefOfLimits.get(i).get(enter - 1).compare(new Fraction(0, 1)) == 0) continue;
                Fraction divider = coefOfLimits.get(i).get(enter - 1).multiply(new Fraction(-1, 1));
                for (int j = 0; j < VAR_COUNT; j++)
                    coefOfLimits.get(i).set(j, coefOfLimits.get(exit).get(j).multiply(divider).plus(coefOfLimits.get(i).get(j)));
                freeVars.set(i, freeVars.get(exit).multiply(divider).plus(freeVars.get(i)));
            }
            Fraction divider = opinions.get(enter - 1).multiply(new Fraction(-1, 1));
            for (int i = 0; i < VAR_COUNT; i++)
                opinions.set(i, coefOfLimits.get(exit).get(i).multiply(divider).plus(opinions.get(i)));
            valueOfFunction = freeVars.get(exit).multiply(divider).plus(valueOfFunction);
        }
        resultationPoint = new Fraction[VAR_COUNT];
        for (int i = 0; i < VAR_COUNT; i++)
            resultationPoint[i] = new Fraction(0, 1);
        for (int i = 0; i < LIMIT_COUNT; i++)
            resultationPoint[basis.get(i) - 1] = freeVars.get(i);
        return OK;
    }

    private boolean isNegativeFreeVar() {
        for (int i = 0; i < LIMIT_COUNT; i++)
            if (freeVars.get(i).compare(new Fraction(0, 1)) == -1)
                return true;
        return false;
    }

    private boolean isEmptyMPR() {
        ArrayList<Boolean> isNegativeEl = new ArrayList<>();
        for (int i = 0; i < LIMIT_COUNT; i++) {
            if (freeVars.get(i).compare(new Fraction(0, 1)) == -1) {
                int countNegative = 0;
                for (int j = 0; j < VAR_COUNT; j++)
                    if (coefOfLimits.get(i).get(j).compare(new Fraction(0, 1)) == -1)
                        countNegative++;
                isNegativeEl.add(countNegative == 0);
            }
        }
        for (Boolean b : isNegativeEl)
            if (b)
                return true;
        return false;
    }

    private void initOpinions(List<Fraction> koef_function) {
        Fraction _valueOfFunction = new Fraction(0, 1);
        for (int i = 0; i < VAR_COUNT; i++) {
            Fraction item_opinion = new Fraction(0, 1);
            for (int j = 0; j < LIMIT_COUNT; j++)
                item_opinion = item_opinion.multiply(coefOfFunction.get(basis.get(j) - 1));
            opinions.add(item_opinion.minus(koef_function.get(i)));
        }
        for (int i = 0; i < LIMIT_COUNT; i++)
            _valueOfFunction = _valueOfFunction.plus(freeVars.get(i).multiply(coefOfFunction.get(basis.get(i) - 1)));
        valueOfFunction = _valueOfFunction;
    }

    public Fraction[] getResultPoint() {
        return resultationPoint;
    }

    public ArrayList<ArrayList<Fraction>> getLimits() {
        return coefOfLimits;
    }

    public Fraction getValueOfFunction() {
        return valueOfFunction;
    }

    public ArrayList<Fraction> getFreeVars() {
        return freeVars;
    }

    public int getState() {
        return TASK_STATE;
    }

    private ArrayList<String> getSigns() {
        ArrayList<String> signs = new ArrayList<>();
        for (int i = 0; i < freeVars.size(); i++)
            signs.add(new String("="));
        return signs;
    }
}
