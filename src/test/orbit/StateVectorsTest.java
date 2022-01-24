package test.orbit;

import com.qbizzle.Math.Vector;
import com.qbizzle.Orbit.BadTLEFormatException;
import com.qbizzle.Orbit.COE;
import com.qbizzle.Orbit.StateVectors;
import com.qbizzle.Orbit.TLE;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StateVectorsTest {
    String strLEOTLE = "ISS (ZARYA)             \n" +
            "1 25544U 98067A   22022.91470718  .00005958  00000+0  11386-3 0  9993\n" +
            "2 25544  51.6445 336.0056 0006830  51.7508  17.5213 15.49594026322655";

    @Test
    @DisplayName("Current COE constructor test")
    public void currentCoeConstructorTest() throws BadTLEFormatException {
        COE coe = new COE(new TLE(strLEOTLE));
        StateVectors state = new StateVectors(coe);
        Assertions.assertAll(() -> assertEquals(new Vector(3796651.7981394175, 2625110.4264662312, 4981712.072374897), state.Position()),
                () -> assertEquals(new Vector(-5864.6649866517355, 4451.790185127849, 2125.8473021967284), state.Velocity()));
    }

    @Test
    @DisplayName("Future COE constructor test")
    public void futureCoeConstructorTest() throws BadTLEFormatException {
        COE coe = new COE(new TLE(strLEOTLE), 1.23456);
        StateVectors state = new StateVectors(coe);
        assertAll(() -> assertEquals(new Vector(-1225493.0931284525, 4676792.423899453, 4769592.7817697795), state.Position()),
                () -> assertEquals(new Vector(-7131.55337935173, 861.3728394739792, -2670.313696201894), state.Velocity()));
    }
    
    @Test
    @DisplayName("Current TLE constructor test")
    public void currentTleConstructorTest() throws BadTLEFormatException {
        StateVectors state = new StateVectors(new TLE(strLEOTLE));
        Assertions.assertAll(() -> assertEquals(new Vector(3796651.7981394175, 2625110.4264662312, 4981712.072374897), state.Position()),
                () -> assertEquals(new Vector(-5864.6649866517355, 4451.790185127849, 2125.8473021967284), state.Velocity()));
    }

    @Test
    @DisplayName("Future TLE constructor test")
    public void futureTleConstructorTest() throws BadTLEFormatException {
        StateVectors state = new StateVectors(new TLE(strLEOTLE), 1.23456);
        assertAll(() -> assertEquals(new Vector(-1225493.0931284525, 4676792.423899453, 4769592.7817697795), state.Position()),
                () -> assertEquals(new Vector(-7131.55337935173, 861.3728394739792, -2670.313696201894), state.Velocity()));
    }

}