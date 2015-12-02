package tests;

import com.example.dmytro.dualsmplex.Fraction;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by Dmytro on 02.12.2015.
 */
public class FractionTest extends TestCase {
    @Test
    public void testIsInteger() {
        Fraction fraction = new Fraction(7, 5);
        assertTrue(!fraction.isInteger());

        fraction = new Fraction(1, 1);
        assertTrue(fraction.isInteger());

        fraction = new Fraction(5, 1);
        assertTrue(fraction.isInteger());

        fraction = new Fraction(1, 4);
        assertTrue(!fraction.isInteger());

        fraction = new Fraction(27, 9);
        assertTrue(fraction.isInteger());

        fraction = new Fraction(15, 17);
        assertTrue(!fraction.isInteger());

        fraction = new Fraction(0, 4);
        assertTrue(fraction.isInteger());
    }

    @Test
    public void testGetDoublePart() {
        Fraction fraction = new Fraction(3, 4);
        assertTrue(fraction.getDoublePart().compare(new Fraction(3, 4)) == 0);

        fraction = new Fraction(4, 3);
        assertTrue(fraction.getDoublePart().compare(new Fraction(1, 3)) == 0);

        fraction = new Fraction(7, 3);
        assertTrue(fraction.getDoublePart().compare(new Fraction(1, 3)) == 0);

        fraction = new Fraction(27, 3);
        assertTrue(fraction.getDoublePart().compare(new Fraction(0)) == 0);

        fraction = new Fraction(7, 2);
        assertTrue(fraction.getDoublePart().compare(new Fraction(1, 2)) == 0);

        fraction = new Fraction(-1, 22);
        assertTrue(fraction.getDoublePart().compare(new Fraction(21, 22)) == 0);
    }
}
