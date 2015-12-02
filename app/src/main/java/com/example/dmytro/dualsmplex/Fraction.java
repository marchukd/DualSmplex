package com.example.dmytro.dualsmplex;

/**
 * Created by Dmytro on 01.10.2015.
 */
public class Fraction {
    public static Fraction ZERO = new Fraction(0, 1);
    public static Fraction ONE = new Fraction(1, 1);
    private int numerator;
    private int denominator;

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.shorten();
    }

    public Fraction(int numerator) {
        this(numerator, 1);
    }

    public Fraction(Fraction _fr) {
        this.numerator = _fr.numerator;
        this.denominator = _fr.denominator;
    }

    public Fraction plus(Fraction param) {
        int _denom = this.denominator * param.denominator;
        int _numer = this.numerator * (_denom / this.denominator) + param.numerator * (_denom / param.denominator);
        return new Fraction(_numer, _denom);
    }

    public Fraction minus(Fraction param) {
        int _denom = this.denominator * param.denominator;
        int _numer = this.numerator * (_denom / this.denominator) - param.numerator * (_denom / param.denominator);
        return new Fraction(_numer, _denom);
    }

    public Fraction multiply(Fraction param) {
        int _denom = this.denominator * param.denominator;
        int _numer = this.numerator * param.numerator;
        return new Fraction(_numer, _denom);
    }

    public Fraction divide(Fraction param) {
        int _denom = this.denominator * param.numerator;
        int _numer = this.numerator * param.denominator;
        return new Fraction(_numer, _denom);
    }

    public String toString() {
        if (this.denominator == 1)
            return Integer.toString(this.numerator);
        return this.numerator + "/" + this.denominator;
    }

    private int mcd(int a, int b) {
        int r;
        while (b != 0) {
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }


    public void shorten() {
        int aux = mcd(this.numerator, this.denominator);
        this.numerator /= aux;
        this.denominator /= aux;
        this.checkMinus();
    }

    private void checkMinus() {
        if (this.denominator < 0) {
            this.denominator *= -1;
            this.numerator *= -1;
        }
    }

    public int compare(Fraction param) {
        if (((double) this.numerator / (double) this.denominator) > ((double) param.numerator / (double) param.denominator))
            return 1;
        else if ((double) this.numerator == (double) param.numerator && (double) this.denominator == (double) param.denominator)
            return 0;
        return -1;
    }

    public static Fraction parse(String dest) {
        String[] temp = dest.split("/");
        if (temp.length == 2)
            return new Fraction(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
        else if (temp.length == 1)
            return new Fraction(Integer.parseInt(temp[0]), 1);
        else
            return new Fraction(0, 1);

    }

    public boolean isInteger() {
        return (((double) numerator / denominator) - (numerator / denominator)) == 0;
    }

    public Fraction getDoublePart() {
        if (this.compare(ZERO) == -1)
            return new Fraction(new Fraction(denominator, denominator).plus(this));
        return new Fraction(numerator % denominator, denominator);
    }
}
