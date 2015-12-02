package tests;

import com.example.dmytro.dualsmplex.DualSimplex;
import com.example.dmytro.dualsmplex.Fraction;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;


/**
 * Created by Dmytro on 25.11.2015.
 */
public class DualSimplexTest extends TestCase {
    private ArrayList<ArrayList<Fraction>> coefOfLimits = new ArrayList<>();
    private ArrayList<Fraction> row;
    private ArrayList<Fraction> freevars = new ArrayList<>();
    private ArrayList<String> signs = new ArrayList<>();
    private ArrayList<Fraction> koefOfFunc = new ArrayList<>();
    private Fraction[] resultPoint = new Fraction[]{};
    private Fraction valueOfFunction = new Fraction(0, 1);

    public DualSimplexTest(String name) {
        super(name);
    }

    @Test
    public void test() throws Exception {
        clearFields();
        task1();
        DualSimplex simplex = new DualSimplex(coefOfLimits, freevars, koefOfFunc, signs);
        for (int i = 0; i < simplex.getResultPoint().length; i++)
            assertTrue(simplex.getResultPoint()[i].compare(resultPoint[i]) == 0);
        assertTrue(simplex.getValueOfFunction().compare(valueOfFunction) == 0);
    }

    public void task1() {
        row = new ArrayList<>();
        row.add(new Fraction(1));
        row.add(new Fraction(-1));
        signs.add("<=");
        freevars.add(new Fraction(2));
        coefOfLimits.add(row);

        row = new ArrayList<>();
        row.add(new Fraction(2));
        row.add(new Fraction(3));
        signs.add(">=");
        freevars.add(new Fraction(5));
        coefOfLimits.add(row);

        row = new ArrayList<>();
        row.add(new Fraction(-4));
        row.add(new Fraction(2));
        signs.add("<=");
        freevars.add(new Fraction(-3));
        coefOfLimits.add(row);

        koefOfFunc.add(new Fraction(2));
        koefOfFunc.add(new Fraction(3));

        resultPoint = new Fraction[]{
                new Fraction(11, 5),
                new Fraction(1, 5),
                new Fraction(0),
                new Fraction(0),
                new Fraction(27, 5)
        };

        valueOfFunction = new Fraction(5);
    }

    private void clearFields() {
        signs.clear();
        coefOfLimits.clear();
        freevars.clear();
        koefOfFunc.clear();
    }
}
