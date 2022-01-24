package test.orbit;

import com.qbizzle.Orbit.BadTLEFormatException;
import com.qbizzle.Orbit.COE;
import com.qbizzle.Orbit.TLE;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class COETest {
    private final String strLEOTLE = "ISS (ZARYA)             \n" +
            "1 25544U 98067A   22022.91470718  .00005958  00000+0  11386-3 0  9993\n" +
            "2 25544  51.6445 336.0056 0006830  51.7508  17.5213 15.49594026322655";
    private final TLE tle = new TLE(strLEOTLE);

    COETest() throws BadTLEFormatException {
    }

    @Test
    @DisplayName("TLE constructor test")
    public void tleConstructorTest() {
        COE coe = new COE(tle);
        assertAll(() -> assertEquals(6796049.7960874, coe.sma, "SMA"),
                () -> assertEquals(0.0006830, coe.ecc, "Eccentricity"),
                () -> assertEquals(336.0056, coe.lan, "LAN"),
                () -> assertEquals(51.7508, coe.aop, "AOP"),
                () -> assertEquals(51.6445, coe.inc, "Inclination"),
                () -> assertEquals(17.54488, Math.round(coe.ta*100000)/100000.0, "True anomaly"));
    }

    @Test
    @DisplayName("TLE future constructor test")
    public void tleFutureConstructorTest() {
        COE coe = new COE(tle, 1.23456);
        assertAll(() -> assertEquals(6796006.784, Math.round(coe.sma*1000)/1000.0, "SMA"),
                () -> assertEquals(0.000676675, Math.round(coe.ecc*10e8)/10e8, "Eccentricity"),
                () -> assertEquals(336.0056, Math.round(coe.lan*10e4)/10e4, "LAN"),
                () -> assertEquals(51.7508, Math.round(coe.aop*10e4)/10e4, "AOP"),
                () -> assertEquals(51.6445, coe.inc, "Inclination"),
                () -> assertEquals(56.77810642, Math.round(coe.ta*10e8)/10e8, "True anomaly"));
    }

}