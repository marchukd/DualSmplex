package com.example.dmytro.dualsmplex;

import java.util.ArrayList;

public class Homori extends BaseMethod {
    Simplex simplex;
    int generatingLine;

    public Homori(Simplex simplex) {
        setFields(simplex);

        int state = simplex.getState();
        if (state == EMPTY_MPR)
            throw new RuntimeException("МПР вихідної задачі порожня");
        while (true) {
            if (isIntegerSolution()) {
                String message = "Процес припиняється. Отримано розв'язок вихідної задачі";
                return;
            } else {
                generatingLine = maxOfDoubleParts();
                prepareLimitsAfterAdd();
                coefOfLimits.add(getNewLimit());
                Fraction fraction = getNewFreeVar();
                freeVars.add(getNewFreeVar());
                ArrayList<Fraction> opinions = this.opinions;
                opinions.add(new Fraction(0));
                basis.add(simplex.VAR_COUNT + 1);
                VAR_COUNT++;

                DualSimplex dualSimplex = new DualSimplex(coefOfLimits, freeVars, opinions, valueOfFunction, basis);
                setFields(dualSimplex);
            }
        }
    }

    private void setFields(BaseMethod method) {
        this.coefOfLimits = method.coefOfLimits;
        this.coefOfFunction = method.coefOfFunction;
        this.resultationPoint = method.resultationPoint;
        this.freeVars = method.freeVars;
        this.valueOfFunction = method.valueOfFunction;
        this.opinions = method.opinions;
        this.basis = method.basis;
        VAR_COUNT = method.VAR_COUNT;
        LIMIT_COUNT = method.LIMIT_COUNT;
    }

    private boolean isIntegerSolution() {
        for (int i = 0; i < resultationPoint.length; i++)
            if (!resultationPoint[i].isInteger())
                return false;
        return true;
    }

    private int maxOfDoubleParts() {
        int maxIndex = 0;
        for (int i = 0; i < freeVars.size(); i++) {
            if (!freeVars.get(i).isInteger()) {
                maxIndex = i;
                break;
            }
        }
        for (int i = maxIndex + 1; i < freeVars.size(); i++) {
            if (!freeVars.get(i).isInteger())
                if (freeVars.get(i).getDoublePart().compare(freeVars.get(maxIndex).getDoublePart()) == 1)
                    maxIndex = i;
        }
        int countOfMax = 0;
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < freeVars.size(); i++)
            if (freeVars.get(i).getDoublePart().compare(freeVars.get(maxIndex).getDoublePart()) == 0) {
                countOfMax++;
                indexes.add(i);
            }
        if (countOfMax == 1)
            return maxIndex;
        Fraction summaOfDoubleParts = null;
        for (int i = 0; i < indexes.size(); i++) {
            Fraction currentSummaOfDoubleParts = new Fraction(0);
            int currentLimit = indexes.get(i);
            for (int j = 0; j < coefOfLimits.get(currentLimit).size(); j++)
                currentSummaOfDoubleParts = currentSummaOfDoubleParts.plus(coefOfLimits.get(currentLimit).get(j).getDoublePart());
            if (summaOfDoubleParts == null) {
                summaOfDoubleParts = freeVars.get(currentLimit).getDoublePart().divide(currentSummaOfDoubleParts);
                maxIndex = currentLimit;
            } else if (freeVars.get(currentLimit).getDoublePart().divide(currentSummaOfDoubleParts).compare(summaOfDoubleParts) == 1) {
                summaOfDoubleParts = freeVars.get(currentLimit).getDoublePart().divide(currentSummaOfDoubleParts);
                maxIndex = currentLimit;
            }
        }
        return maxIndex;
    }

    private ArrayList<Fraction> getNewLimit() {
        ArrayList<Fraction> newLimit = new ArrayList<>();
        for (int i = 0; i < coefOfLimits.get(generatingLine).size() - 1; i++) {
            if (coefOfLimits.get(generatingLine).get(i).getDoublePart().compare(Fraction.ZERO) != 0) {
                newLimit.add(coefOfLimits.get(generatingLine).get(i).getDoublePart().multiply(new Fraction(-1)));
            } else
                newLimit.add(new Fraction(0));
        }
        newLimit.add(new Fraction(1));
        return newLimit;
    }

    private void prepareLimitsAfterAdd() {
        for (int i = 0; i < coefOfLimits.size(); i++) {
            coefOfLimits.get(i).add(new Fraction(0));
        }
    }

    private Fraction getNewFreeVar() {
        return freeVars.get(generatingLine).getDoublePart().divide(new Fraction(-1));
    }
}
