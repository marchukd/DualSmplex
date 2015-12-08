package tests;

import com.example.dmytro.dualsmplex.Fraction;
import com.example.dmytro.dualsmplex.Homori;
import com.example.dmytro.dualsmplex.Simplex;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Dmytro on 02.12.2015.
 */
public class HomoriTest extends TestCase {
    @Test
    public void testHomori6() {
        ArrayList<ArrayList<Fraction>> limits = new ArrayList<>();
        ArrayList<Fraction> limit;
        ArrayList<Fraction> freevars = new ArrayList<>();
        ArrayList<Fraction> coefOfFunction = new ArrayList<>();

        limit = new ArrayList<>();
        limit.add(new Fraction(4));
        limit.add(new Fraction(1));
        limit.add(new Fraction(1));
        limit.add(new Fraction(0));
        freevars.add(new Fraction(6));
        limits.add(limit);

        limit = new ArrayList<>();
        limit.add(new Fraction(-2));
        limit.add(new Fraction(1));
        limit.add(new Fraction(0));
        limit.add(new Fraction(1));
        freevars.add(new Fraction(4));
        limits.add(limit);

        coefOfFunction.add(new Fraction(-1));
        coefOfFunction.add(new Fraction(1));
        coefOfFunction.add(new Fraction(0));
        coefOfFunction.add(new Fraction(0));

        Simplex simplex = new Simplex(limits, freevars, coefOfFunction);

        Homori homori = new Homori(simplex);
        assertTrue(homori.valueOfFunction.toString().equals("-1"));
    }

    public void testHomori7() {
        ArrayList<ArrayList<Fraction>> limits = new ArrayList<>();
        ArrayList<Fraction> limit;
        ArrayList<Fraction> freevars = new ArrayList<>();
        ArrayList<Fraction> coefOfFunction = new ArrayList<>();

        limit = new ArrayList<>();
        limit.add(new Fraction(1));
        limit.add(new Fraction(3));
        limit.add(new Fraction(1));
        limit.add(new Fraction(0));
        freevars.add(new Fraction(4));
        limits.add(limit);

        limit = new ArrayList<>();
        limit.add(new Fraction(2));
        limit.add(new Fraction(-5));
        limit.add(new Fraction(0));
        limit.add(new Fraction(1));
        freevars.add(new Fraction(7));
        limits.add(limit);

        coefOfFunction.add(new Fraction(-3));
        coefOfFunction.add(new Fraction(-4));
        coefOfFunction.add(new Fraction(0));
        coefOfFunction.add(new Fraction(0));

        Simplex simplex = new Simplex(limits, freevars, coefOfFunction);
        Homori homori = new Homori(simplex);
        assertTrue(homori.valueOfFunction.toString().equals("-9"));
    }

    public void testHomori8() {
        ArrayList<ArrayList<Fraction>> limits = new ArrayList<>();
        ArrayList<Fraction> limit;
        ArrayList<Fraction> freevars = new ArrayList<>();
        ArrayList<Fraction> coefOfFunction = new ArrayList<>();

        limit = new ArrayList<>();
        limit.add(new Fraction(-1));
        limit.add(new Fraction(3));
        limit.add(new Fraction(1));
        limit.add(new Fraction(0));
        freevars.add(new Fraction(6));
        limits.add(limit);

        limit = new ArrayList<>();
        limit.add(new Fraction(2));
        limit.add(new Fraction(-4));
        limit.add(new Fraction(0));
        limit.add(new Fraction(1));
        freevars.add(new Fraction(8));
        limits.add(limit);

        coefOfFunction.add(new Fraction(-4));
        coefOfFunction.add(new Fraction(-3));
        coefOfFunction.add(new Fraction(0));
        coefOfFunction.add(new Fraction(0));

        Simplex simplex = new Simplex(limits, freevars, coefOfFunction);
        Homori homori = new Homori(simplex);
        assertTrue(homori.valueOfFunction.toString().equals("-126"));
    }

    public void testHomori9() {
        ArrayList<ArrayList<Fraction>> limits = new ArrayList<>();
        ArrayList<Fraction> limit;
        ArrayList<Fraction> freevars = new ArrayList<>();
        ArrayList<Fraction> coefOfFunction = new ArrayList<>();

        limit = new ArrayList<>();
        limit.add(new Fraction(1));
        limit.add(new Fraction(3));
        limit.add(new Fraction(1));
        limit.add(new Fraction(0));
        freevars.add(new Fraction(5));
        limits.add(limit);

        limit = new ArrayList<>();
        limit.add(new Fraction(3));
        limit.add(new Fraction(-2));
        limit.add(new Fraction(0));
        limit.add(new Fraction(1));
        freevars.add(new Fraction(6));
        limits.add(limit);

        coefOfFunction.add(new Fraction(-3));
        coefOfFunction.add(new Fraction(-1));
        coefOfFunction.add(new Fraction(0));
        coefOfFunction.add(new Fraction(0));

        Simplex simplex = new Simplex(limits, freevars, coefOfFunction);
        Homori homori = new Homori(simplex);
        assertTrue(homori.valueOfFunction.toString().equals("-6"));
    }

    public void testHomori10() {
        ArrayList<ArrayList<Fraction>> limits = new ArrayList<>();
        ArrayList<Fraction> limit;
        ArrayList<Fraction> freevars = new ArrayList<>();
        ArrayList<Fraction> coefOfFunction = new ArrayList<>();

        limit = new ArrayList<>();
        limit.add(new Fraction(2));
        limit.add(new Fraction(-4));
        limit.add(new Fraction(1));
        limit.add(new Fraction(0));
        freevars.add(new Fraction(6));
        limits.add(limit);

        limit = new ArrayList<>();
        limit.add(new Fraction(1));
        limit.add(new Fraction(4));
        limit.add(new Fraction(0));
        limit.add(new Fraction(1));
        freevars.add(new Fraction(9));
        limits.add(limit);

        coefOfFunction.add(new Fraction(-4));
        coefOfFunction.add(new Fraction(-3));
        coefOfFunction.add(new Fraction(0));
        coefOfFunction.add(new Fraction(0));

        Simplex simplex = new Simplex(limits, freevars, coefOfFunction);
        Homori homori = new Homori(simplex);
        assertTrue(homori.valueOfFunction.toString().equals("-23"));
    }

    public void testHomori11() {
        ArrayList<ArrayList<Fraction>> limits = new ArrayList<>();
        ArrayList<Fraction> limit;
        ArrayList<Fraction> freevars = new ArrayList<>();
        ArrayList<Fraction> coefOfFunction = new ArrayList<>();

        limit = new ArrayList<>();
        limit.add(new Fraction(3));
        limit.add(new Fraction(-2));
        limit.add(new Fraction(1));
        limit.add(new Fraction(0));
        freevars.add(new Fraction(7));
        limits.add(limit);

        limit = new ArrayList<>();
        limit.add(new Fraction(-1));
        limit.add(new Fraction(5));
        limit.add(new Fraction(0));
        limit.add(new Fraction(1));
        freevars.add(new Fraction(10));
        limits.add(limit);

        coefOfFunction.add(new Fraction(-5));
        coefOfFunction.add(new Fraction(-3));
        coefOfFunction.add(new Fraction(0));
        coefOfFunction.add(new Fraction(0));

        Simplex simplex = new Simplex(limits, freevars, coefOfFunction);
        Homori homori = new Homori(simplex);
        assertTrue(homori.valueOfFunction.toString().equals("-21"));
    }

    public void testHomori12() {
        ArrayList<ArrayList<Fraction>> limits = new ArrayList<>();
        ArrayList<Fraction> limit;
        ArrayList<Fraction> freevars = new ArrayList<>();
        ArrayList<Fraction> coefOfFunction = new ArrayList<>();

        limit = new ArrayList<>();
        limit.add(new Fraction(4));
        limit.add(new Fraction(-1));
        limit.add(new Fraction(1));
        limit.add(new Fraction(0));
        limit.add(new Fraction(0));
        freevars.add(new Fraction(5));
        limits.add(limit);

        limit = new ArrayList<>();
        limit.add(new Fraction(-2));
        limit.add(new Fraction(3));
        limit.add(new Fraction(0));
        limit.add(new Fraction(1));
        limit.add(new Fraction(0));
        freevars.add(new Fraction(7));
        limits.add(limit);

        limit = new ArrayList<>();
        limit.add(new Fraction(7));
        limit.add(new Fraction(2));
        limit.add(new Fraction(0));
        limit.add(new Fraction(0));
        limit.add(new Fraction(1));
        freevars.add(new Fraction(11));
        limits.add(limit);

        coefOfFunction.add(new Fraction(-7));
        coefOfFunction.add(new Fraction(-9));
        coefOfFunction.add(new Fraction(0));
        coefOfFunction.add(new Fraction(0));
        coefOfFunction.add(new Fraction(0));

        Simplex simplex = new Simplex(limits, freevars, coefOfFunction);
        Homori homori = new Homori(simplex);
        assertTrue(homori.valueOfFunction.toString().equals("-25"));
    }
}
