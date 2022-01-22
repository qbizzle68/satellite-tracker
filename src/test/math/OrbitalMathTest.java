package test.math;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.qbizzle.Math.OrbitalMath.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrbitalMathTest {

    double ecc = 0.0123;
    final double CRND = 1e5;

    @Test
    @DisplayName("Mean motion to sma test")
    public void meanMotionToSMATest() {
        // mean motion = 15.73454234 rev / day
        assertEquals(6727170.439,
                Math.round(mMotion2SMA(15.73454234)*1000)/1000.0);
    }

    @Test
    @DisplayName("SMA to mean motion test")
    public void smaToMeanMotionTest() {
        assertEquals(0.00114,
                Math.round(SMA2MMotion(6727170.439)*100000)/100000.0);
    }

    @Nested
    @DisplayName("Mean anomaly to eccentric anomaly for each quadrant")
    public class Mean2EccQuad {
        @ParameterizedTest
        @DisplayName("Mean anomaly to eccentric anomaly by quadrants")
        @CsvFileSource(files = "src/test/resources/mean-to-ecc-arguments.csv", numLinesToSkip = 1)
        public void meanAnomalyToEccAnomalyByQuadrants(double q, double exp) {
            assertEquals(exp,
                    Math.round(Mean2Eccentric(q, ecc) * CRND) / CRND);
        }
    }

    @Nested
    @DisplayName("Eccentric anomaly to true anomaly for each quadrant")
    public class Ecc2TrueQuad {
        @ParameterizedTest
        @DisplayName("Eccentric anomaly to true anomaly by quadrants")
        @CsvFileSource(files = "src/test/resources/ecc-to-true-arguments.csv", numLinesToSkip = 1)
        public void eccAnomalyToTrueAnomalyByQuadrants(double q, double exp) {
            assertEquals(exp,
                    Math.round(Eccentric2True(q, ecc) * CRND) / CRND);
        }
    }

    @Nested
    @DisplayName("Mean anomaly to true anomaly for each quadrant")
    public class Mean2TrueQuad {
        @ParameterizedTest
        @DisplayName("Mean anomaly to true anomaly by quadrants")
        @CsvFileSource(files = "src/test/resources/mean-to-true-arguments.csv", numLinesToSkip = 1)
        public void meanAnomalyToTrueAnomalyByQuadrants(double q, double exp) {
            assertEquals(exp,
                    Math.round(Mean2True(q, ecc) * CRND) / CRND);
        }

    }

    @Nested
    @DisplayName("True anomaly to eccentric anomaly for each quadrant")
    public class True2EccQuad {
        @ParameterizedTest
        @DisplayName("True anomaly to eccentric anomaly by quadrants")
        @CsvFileSource(files = "src/test/resources/true-to-ecc-arguments.csv", numLinesToSkip = 1)
        public void trueAnomalyToEccentricAnomalyByQuadrants(double q, double exp) {
            assertEquals(exp,
                    Math.round(True2Eccentric(q, ecc) * CRND) / CRND);
        }
    }

    @Nested
    @DisplayName("Eccentric anomaly to mean anomaly for each quadrant")
    public class Ecc2MeanQuad {
        @ParameterizedTest
        @DisplayName("Eccentric anomaly to mean anomaly by quadrants")
        @CsvFileSource(files = "src/test/resources/ecc-to-mean-arguments.csv", numLinesToSkip = 1)
        public void eccAnomalyToMeanAnomalyByQuadrants(double q, double exp) {
            assertEquals(exp,
                    Math.round(Eccentric2Mean(q, ecc) * CRND) / CRND);
        }
    }

    @Nested
    @DisplayName("True anomaly to mean anomaly for each quadrant")
    public class True2MeanQuad {
        @ParameterizedTest
        @DisplayName("True anomaly to mean anomaly by quadrands")
        @CsvFileSource(files = "src/test/resources/true-to-mean-arguments.csv", numLinesToSkip = 1)
        public void trueAnomalyToMeanAnomalyByQuadrants(double q, double exp) {
            assertEquals(exp,
                    Math.round(True2Mean(q, ecc) * CRND) / CRND);
        }
    }

}