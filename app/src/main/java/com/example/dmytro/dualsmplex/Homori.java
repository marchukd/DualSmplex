package com.example.dmytro.dualsmplex;

import java.util.ArrayList;

/**
 * Created by Dmytro on 02.12.2015.
 */
public class Homori extends BaseMethod {
    Simplex simplex;

    public Homori(Simplex simplex) {
        this.coefOfLimits = simplex.coefOfLimits;
        this.coefOfFunction = simplex.coefOfFunction;
        this.resultationPoint = simplex.resultationPoint;
        this.freeVars = simplex.freeVars;

        int state = simplex.getState();
        if (state == EMPTY_MPR)
            throw new RuntimeException("МПР вихідної задачі порожня");
        if (isIntegerSolution()) {
            String message = "Процес припиняється. Отримано розв'язок вихідної задачі";
        } else {
            int generatingLine = maxOfDoubleParts();
            //ArrayList<Fraction> limit = new ArrayList<>()
        }
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
            maxIndex = 0;
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
}
