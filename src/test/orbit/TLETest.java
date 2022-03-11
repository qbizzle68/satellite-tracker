package test.orbit;

import com.qbizzle.exception.InvalidTLEException;
import com.qbizzle.orbit.TLE;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TLETest {

    @Nested
    @DisplayName("Testing constructor of various types of orbits")
    public class ConstructorTest {
        private final String strLEOTLE = "ISS (ZARYA)             \n" +
                "1 25544U 98067A   22022.91470718  .00005958  00000+0  11386-3 0  9993\n" +
                "2 25544  51.6445 336.0056 0006830  51.7508  17.5213 15.49594026322655";
        private final String strGEOTLE = "TDRS 3                  \n" +
                "1 19548U 88091B   22022.74750692 -.00000288  00000+0  00000+0 0  9998\n" +
                "2 19548  13.8173 352.4733 0040871 328.9668  20.9178  1.00275354109252";
        private final String strMEOTLE = "GPS BIIR-2  (PRN 13)    \n" +
                "1 24876U 97035A   22022.27835439  .00000064  00000+0  00000+0 0  9998\n" +
                "2 24876  55.4939 161.3762 0056142  54.1976 306.2926  2.00563186179725";
        private final String strMolniyaTLE = "MOLNIYA 3-50            \n" +
                "1 25847U 99036A   22021.95943339 -.00000413  00000+0 -32734-3 0  9996\n" +
                "2 25847  63.0212 357.5613 7429748 281.0640  10.6910  2.00612477165170";
        private final String strPolarTLE = "POLAR SCOUT KODIAK      \n" +
                "1 43764U 18099G   22022.73232871  .00001624  00000+0  14434-3 0  9993\n" +
                "2 43764  97.6452  95.6829 0016259  38.8651 321.3737 14.97401529171391";
        private final TLE leotle = new TLE(strLEOTLE);
        private final TLE geotle = new TLE(strGEOTLE);
        private final TLE meotle = new TLE(strMEOTLE);
        private final TLE molniyatle = new TLE(strMolniyaTLE);
        private final TLE polartle = new TLE(strPolarTLE);

        public ConstructorTest() throws InvalidTLEException {
        }

        @Test
        @DisplayName("LEO constructor test")
        public void leoConstructorTest() {
            Assertions.assertAll(() -> assertEquals("ISS (ZARYA)", leotle.name()),
                    () -> assertEquals(25544, leotle.catalogNumber()),
                    () -> assertEquals('U', leotle.classification()),
                    () -> assertEquals("98067A", leotle.cosparID()),
                    () -> assertEquals(2022, leotle.epochYear()),
                    () -> assertEquals(22.91470718, leotle.epochDay()),
                    () -> assertEquals(0.00005958, leotle.meanMotionDot()),
                    () -> assertEquals(0.0, leotle.meanMotionDDot()),
                    () -> assertEquals(0.00011386, leotle.bStar()),
                    () -> assertEquals(0, leotle.ephemeris()),
                    () -> assertEquals(999, leotle.tleSetNumber()),
                    () -> assertEquals(51.6445, leotle.inclination()),
                    () -> assertEquals(336.0056, leotle.lan()),
                    () -> assertEquals(0.0006830, leotle.eccentricity()),
                    () -> assertEquals(51.7508, leotle.aop()),
                    () -> assertEquals(17.5213, leotle.meanAnomaly()),
                    () -> assertEquals(15.49594026, leotle.meanMotion()),
                    () -> assertEquals(32265, leotle.revolutionNumber()));
        }

        @Test
        @DisplayName("GEO constructor test")
        public void geoConstructorTest() {
            Assertions.assertAll(() -> assertEquals("TDRS 3", geotle.name(), "Name"),
                    () -> assertEquals(19548, geotle.catalogNumber(), "Catalog number"),
                    () -> assertEquals('U', geotle.classification(), "Classification"),
                    () -> assertEquals("88091B", geotle.cosparID(), "Cospar ID"),
                    () -> assertEquals(2022, geotle.epochYear(), "Epoch year"),
                    () -> assertEquals(22.74750692, geotle.epochDay(), "Epoch day"),
                    () -> assertEquals(-0.00000288, geotle.meanMotionDot(), "Mean motion dot"),
                    () -> assertEquals(0.0, geotle.meanMotionDDot(), "Mean motion ddot"),
                    () -> assertEquals(0.0, geotle.bStar(), "BStar"),
                    () -> assertEquals(0, geotle.ephemeris(), "Ephemeris"),
                    () -> assertEquals(999, geotle.tleSetNumber(), "Set number"),
                    () -> assertEquals(13.8173, geotle.inclination(), "Inclination"),
                    () -> assertEquals(352.4733, geotle.lan(), "LAN"),
                    () -> assertEquals(0.0040871, geotle.eccentricity(), "Eccentricity"),
                    () -> assertEquals(328.9668, geotle.aop(), "AOP"),
                    () -> assertEquals(20.9178, geotle.meanAnomaly(), "Mean anomaly"),
                    () -> assertEquals(1.00275354, geotle.meanMotion(), "Mean motion"),
                    () -> assertEquals(10925, geotle.revolutionNumber(), "Revolution number"));
        }

        @Test
        @DisplayName("MEO constructor test")
        public void meoConstructorTest() {
            Assertions.assertAll(() -> assertEquals("GPS BIIR-2  (PRN 13)", meotle.name(), "Name"),
                    () -> assertEquals(24876, meotle.catalogNumber(), "Catalog number"),
                    () -> assertEquals('U', meotle.classification(), "Classification"),
                    () -> assertEquals("97035A", meotle.cosparID(), "Cospar ID"),
                    () -> assertEquals(2022, meotle.epochYear(), "Epoch year"),
                    () -> assertEquals(22.27835439, meotle.epochDay(), "Epoch day"),
                    () -> assertEquals(0.00000064, meotle.meanMotionDot(), "Mean motion dot"),
                    () -> assertEquals(0.0, meotle.meanMotionDDot(), "Mean motion ddot"),
                    () -> assertEquals(0.0, meotle.bStar(), "BStar"),
                    () -> assertEquals(0, meotle.ephemeris(), "Ephemeris"),
                    () -> assertEquals(999, meotle.tleSetNumber(), "Set number"),
                    () -> assertEquals(55.4939, meotle.inclination(), "Inclination"),
                    () -> assertEquals(161.3762, meotle.lan(), "LAN"),
                    () -> assertEquals(0.0056142, meotle.eccentricity(), "Eccentricity"),
                    () -> assertEquals(54.1976, meotle.aop(), "AOP"),
                    () -> assertEquals(306.2926, meotle.meanAnomaly(), "Mean anomaly"),
                    () -> assertEquals(2.00563186, meotle.meanMotion(), "Mean motion"),
                    () -> assertEquals(17972, meotle.revolutionNumber(), "Revolution number"));
        }

        @Test
        @DisplayName("Molniya constructor test")
        public void molniyaConstructorTest() {
            Assertions.assertAll(() -> assertEquals("MOLNIYA 3-50", molniyatle.name(), "Name"),
                    () -> assertEquals(25847, molniyatle.catalogNumber(), "Catalog number"),
                    () -> assertEquals('U', molniyatle.classification(), "Classification"),
                    () -> assertEquals("99036A", molniyatle.cosparID(), "Cospar ID"),
                    () -> assertEquals(2022, molniyatle.epochYear(), "Epoch year"),
                    () -> assertEquals(21.95943339, molniyatle.epochDay(), "Epoch day"),
                    () -> assertEquals(-0.00000413, molniyatle.meanMotionDot(), "Mean motion dot"),
                    () -> assertEquals(0.0, molniyatle.meanMotionDDot(), "Mean motion ddot"),
                    () -> assertEquals(-0.00032734, molniyatle.bStar(), "BStar"),
                    () -> assertEquals(0, molniyatle.ephemeris(), "Ephemeris"),
                    () -> assertEquals(999, molniyatle.tleSetNumber(), "Set number"),
                    () -> assertEquals(63.0212, molniyatle.inclination(), "Inclination"),
                    () -> assertEquals(357.5613, molniyatle.lan(), "LAN"),
                    () -> assertEquals(0.7429748, molniyatle.eccentricity(), "Eccentricity"),
                    () -> assertEquals(281.0640, molniyatle.aop(), "AOP"),
                    () -> assertEquals(10.6910, molniyatle.meanAnomaly(), "Mean anomaly"),
                    () -> assertEquals(2.00612477, molniyatle.meanMotion(), "Mean motion"),
                    () -> assertEquals(16517, molniyatle.revolutionNumber(), "Revolution number"));
        }

        @Test
        @DisplayName("Polar constructor test")
        public void polarConstructorTest() {
            Assertions.assertAll(() -> assertEquals("POLAR SCOUT KODIAK", polartle.name(), "Name"),
                    () -> assertEquals(43764, polartle.catalogNumber(), "Catalog number"),
                    () -> assertEquals('U', polartle.classification(), "Classification"),
                    () -> assertEquals("18099G", polartle.cosparID(), "Cospar ID"),
                    () -> assertEquals(2022, polartle.epochYear(), "Epoch year"),
                    () -> assertEquals(22.73232871, polartle.epochDay(), "Epoch day"),
                    () -> assertEquals(0.00001624, polartle.meanMotionDot(), "Mean motion dot"),
                    () -> assertEquals(0.0, polartle.meanMotionDDot(), "Mean motion ddot"),
                    () -> assertEquals(0.00014434, polartle.bStar(), "BStar"),
                    () -> assertEquals(0, polartle.ephemeris(), "Ephemeris"),
                    () -> assertEquals(999, polartle.tleSetNumber(), "Set number"),
                    () -> assertEquals(97.6452, polartle.inclination(), "Inclination"),
                    () -> assertEquals(95.6829, polartle.lan(), "LAN"),
                    () -> assertEquals(0.0016259, polartle.eccentricity(), "Eccentricity"),
                    () -> assertEquals(38.8651, polartle.aop(), "AOP"),
                    () -> assertEquals(321.3737, polartle.meanAnomaly(), "Mean anomaly"),
                    () -> assertEquals(14.97401529, polartle.meanMotion(), "Mean motion"),
                    () -> assertEquals(17139, polartle.revolutionNumber(), "Revolution number"));
        }

    }

}