package com.example.dmytro.dualsmplex;

import java.util.ArrayList;

/**
 * Created by Dmytro on 02.12.2015.
 */
public class BaseMethod {
    public int VAR_COUNT;
    public int LIMIT_COUNT;

    public static int EMPTY_MPR = -1;
    public static int OK = 0;
    public static int TASK_STATE = OK;

    public ArrayList<ArrayList<Fraction>> coefOfLimits;
    public ArrayList<Fraction> freeVars;
    public ArrayList<Fraction> opinions;
    public Fraction valueOfFunction;
    public ArrayList<Fraction> coefOfFunction;
    public ArrayList<Integer> basis;
    public Fraction[] resultationPoint;
}
