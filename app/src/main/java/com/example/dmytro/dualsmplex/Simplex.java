package com.example.dmytro.dualsmplex;

import java.util.ArrayList;
import java.util.List;

public class Simplex extends BaseMethod {

    public Simplex(ArrayList<ArrayList<Fraction>> coefOfLimits, ArrayList<Fraction> freeVars, ArrayList<Fraction> coefOfFunction) {
        this.coefOfLimits = coefOfLimits;
        this.freeVars = freeVars;
        this.coefOfFunction = coefOfFunction;
        LIMIT_COUNT = this.coefOfLimits.size();
        VAR_COUNT = this.coefOfLimits.get(0).size();
        basis = new ArrayList<>();
        opinions = new ArrayList<>();
        resultationPoint = new Fraction[VAR_COUNT];

        initialBasis();
        initialOpinions(coefOfFunction);
        TASK_STATE = start();
    }

    private void initialOpinions(List<Fraction> koef_function) {
        Fraction _valueOfFunction = new Fraction(Fraction.ZERO);
        for (int i = 0; i < VAR_COUNT; i++) {
            Fraction itemOpinion = new Fraction(Fraction.ZERO);
            for (int j = 0; j < LIMIT_COUNT; j++)
                itemOpinion = itemOpinion.plus(coefOfLimits.get(j).get(i).multiply(coefOfFunction.get(basis.get(j) - 1)));
            opinions.add(itemOpinion.minus(koef_function.get(i)));
        }
        for (int i = 0; i < LIMIT_COUNT; i++)
            _valueOfFunction = _valueOfFunction.plus(freeVars.get(i).multiply(coefOfFunction.get(basis.get(i) - 1)));
        valueOfFunction = _valueOfFunction;
    }

    private void initialBasis() {
        for (int i = 0; i < VAR_COUNT; i++) {
            int zero = 0, one = 0;
            for (int j = 0; j < LIMIT_COUNT; j++)
                if (coefOfLimits.get(j).get(i).compare(Fraction.ZERO) == 0)
                    zero++;
                else if (coefOfLimits.get(j).get(i).compare(Fraction.ONE) == 0)
                    one++;
            if (zero + one == LIMIT_COUNT)
                basis.add(i + 1);
        }
    }

    private int checkOpinions() {
        int posOpinion = 0;
        for (int i = 0; i < VAR_COUNT; i++) {
            if (opinions.get(i).compare(Fraction.ZERO) == 1) {
                int positive = 0;
                for (int j = 0; j < LIMIT_COUNT; j++)
                    if (coefOfLimits.get(j).get(i).compare(Fraction.ZERO) == 1)
                        positive++;
                if (positive != 0)
                    posOpinion++;
                else if (positive == 0)
                    return -1;
            }
        }
        return posOpinion;
    }

    private int start() {
        while (checkOpinions() > 0) {
            int varOfMaxOpinion = getVarOfMaxOpinion();
            int varOfMinRelation = getVarOfMinReation(varOfMaxOpinion);
            basis.set(varOfMinRelation, varOfMaxOpinion + 1);

            Fraction solutionElement = coefOfLimits.get(varOfMinRelation).get(varOfMaxOpinion);
            Fraction oppositeElement;
            for (int i = 0; i < VAR_COUNT; i++)
                coefOfLimits.get(varOfMinRelation).set(i, coefOfLimits.get(varOfMinRelation).get(i).divide(solutionElement));
            freeVars.set(varOfMinRelation, freeVars.get(varOfMinRelation).divide(solutionElement));
            for (int i = 0; i < LIMIT_COUNT; i++) {
                if (i == varOfMinRelation || coefOfLimits.get(i).get(varOfMaxOpinion).compare(Fraction.ZERO) == 0)
                    continue;
                oppositeElement = coefOfLimits.get(i).get(varOfMaxOpinion).divide(new Fraction(-1, 1));
                for (int j = 0; j < VAR_COUNT; j++)
                    coefOfLimits.get(i).set(j, coefOfLimits.get(varOfMinRelation).get(j).multiply(oppositeElement).plus(coefOfLimits.get(i).get(j)));
                freeVars.set(i, freeVars.get(varOfMinRelation).multiply(oppositeElement).plus(freeVars.get(i)));
            }
            oppositeElement = opinions.get(varOfMaxOpinion).divide(new Fraction(-1, 1));
            for (int i = 0; i < VAR_COUNT; i++)
                opinions.set(i, coefOfLimits.get(varOfMinRelation).get(i).multiply(oppositeElement).plus(opinions.get(i)));
            valueOfFunction = freeVars.get(varOfMinRelation).multiply(oppositeElement).plus(valueOfFunction);
            if (checkOpinions() == -1)
                return EMPTY_MPR;
        }
        for (int i = 0; i < VAR_COUNT; i++)
            resultationPoint[i] = new Fraction(0, 1);
        for (int i = 0; i < LIMIT_COUNT; i++)
            resultationPoint[basis.get(i) - 1] = freeVars.get(i);
        return OK;
    }

    private int getVarOfMinReation(int maxOpinion) {
        ArrayList<Integer> indexPos = new ArrayList<>();
        for (int i = 0; i < LIMIT_COUNT; i++)
            if (coefOfLimits.get(i).get(maxOpinion).compare(Fraction.ZERO) == 1)
                indexPos.add(i);
        int startPos = indexPos.get(0);
        Fraction minRelation = freeVars.get(startPos).divide(coefOfLimits.get(startPos).get(maxOpinion));
        int index = startPos;
        for (int i = 0; i < indexPos.size(); i++)
            if (freeVars.get(indexPos.get(i)).divide(coefOfLimits.get(indexPos.get(i)).get(maxOpinion)).compare(minRelation) == -1) {
                minRelation = freeVars.get(indexPos.get(i)).divide(coefOfLimits.get(indexPos.get(i)).get(maxOpinion));
                index = indexPos.get(i);
            }
        return index;
    }

    private int getVarOfMaxOpinion() {
        int index = 0;
        Fraction f = opinions.get(index);
        for (int i = 0; i < opinions.size(); i++)
            if (opinions.get(i).compare(f) == 1) {
                f = opinions.get(i);
                index = i;
            }
        return index;
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
}
