package test.orbit;

import com.qbizzle.exception.InvalidTLEException;
import com.qbizzle.math.Vector;
import com.qbizzle.orbit.COE;
import com.qbizzle.orbit.TLE;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class COETest {
    private final String strLEOTLE = "ISS (ZARYA)             \n" +
            "1 25544U 98067A   22022.91470718  .00005958  00000+0  11386-3 0  9993\n" +
            "2 25544  51.6445 336.0056 0006830  51.7508  17.5213 15.49594026322655";
    private final TLE tle = new TLE(strLEOTLE);

    COETest() throws InvalidTLEException {
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
                () -> assertEquals(64.665196717, Math.round(coe.ta*10e8)/10e8, "True anomaly"));
    }

    @Test
    @DisplayName("State vectors constructor test")
    public void stateVectorsConstructorTest() {
        Vector r = new Vector(8750000, 5100000, 0);
        Vector v = new Vector(-3000, 5200, 5900);
        COE coe = new COE(r, v);
        assertAll(() -> assertEquals(50686357.45, Math.round(coe.sma*100)/100.0, "SMA"),
                () -> assertEquals(0.8002, Math.round(coe.ecc*10000)/10000.0, "ecc"),
                () -> assertEquals(30.2361, Math.round(coe.lan*10000)/10000.0, "lan"),
                () -> assertEquals(359.59, Math.round(coe.aop*100)/100.0, "aop"),
                () -> assertEquals(44.5029, Math.round(coe.inc*10000)/10000.0, "inc"),
                () -> assertEquals(0.41, Math.round(coe.ta*100)/100.0, "ta"));
    }

}