package test.math;

import com.qbizzle.time.JD;
import com.qbizzle.exception.InvalidTLEException;
import com.qbizzle.orbit.TLE;
import com.sun.jdi.InternalException;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JDTest {

    private final JD jan1_2022 = new JD(1, 1, 2022, 12, 34, 56);
    private final JD dateJD = new JD(new Date(1641062096000L));
    private final double jdValue = 2459581.0242592595;
    private final double tleJDValue = 2454730.01783;

    @Test
    @DisplayName("Value method and long constructor test")
    public void valueMethodTest() {
        assertEquals(jdValue, jan1_2022.value());
    }

    @Test
    @DisplayName("Date constructor test")
    public void dateConstructorTest() {
        assertEquals(jdValue, dateJD.value());
    }

    @Test
    @DisplayName("tle constructor test")
    public void tleConstructorTest() {
        String tleString = """
                ISS (ZARYA)
                1 25544U 98067A   08264.51782528 -.00002182  00000-0 -11606-4 0  2927
                2 25544  51.6416 247.4627 0006703 130.5360 325.0288 15.72125391563537""";
        JD tleJD;
        try {
            tleJD = new JD(new TLE(tleString));
        } catch (InvalidTLEException e) {
            e.printStackTrace();
            throw new InternalException();
        }

        assertEquals(tleJDValue, Math.round(tleJD.value()*100000)/100000.0);
    }

    @Test
    @DisplayName("Number method test")
    public void numberMethodTest() {
        assertEquals(2459581, jan1_2022.number());
    }

    @Test
    @DisplayName("Fraction method test")
    public void fractionMethodTest() {
        assertEquals(0.02426, Math.round(jan1_2022.fraction()*100000)/100000.0);
    }

    @Test
    @DisplayName("Positive difference method test")
    public void positiveDifferenceMethodTest() {
        assertEquals(jdValue - tleJDValue, jan1_2022.difference(new JD(tleJDValue)));
    }

    @Test
    @DisplayName("Negative difference method test")
    public void negativeDifferenceMethodTest() {
        assertEquals(tleJDValue - jdValue, new JD(tleJDValue).difference(jan1_2022));
    }

    @Test
    @DisplayName("Positive future method test")
    public void positiveFutureMethodTest() {
        assertEquals(jdValue + 123.456, jan1_2022.future(123.456).value());
    }

    @Test
    @DisplayName("Negative future method test")
    public void negativeFutureMethodTest() {
        assertEquals(jdValue - 123.456, jan1_2022.future(-123.456).value());
    }

}