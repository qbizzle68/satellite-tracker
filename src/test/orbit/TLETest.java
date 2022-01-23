package test.orbit;

import com.qbizzle.Orbit.BadTLEFormatException;
import com.qbizzle.Orbit.TLE;
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

        public ConstructorTest() throws BadTLEFormatException {
        }

        @Test
        @DisplayName("LEO constructor test")
        public void leoConstructorTest() {
            Assertions.assertAll(() -> assertEquals("ISS (ZARYA)", leotle.Name()),
                    () -> assertEquals(25544, leotle.CatalogNumber()),
                    () -> assertEquals('U', leotle.Classification()),
                    () -> assertEquals("98067A", leotle.CosparID()),
                    () -> assertEquals(2022, leotle.EpochYear()),
                    () -> assertEquals(22.91470718, leotle.EpochDay()),
                    () -> assertEquals(0.00005958, leotle.MeanMotionDot()),
                    () -> assertEquals(0.0, leotle.MeanMotionDDot()),
                    () -> assertEquals(0.00011386, leotle.BStar()),
                    () -> assertEquals(0, leotle.Ephemeris()),
                    () -> assertEquals(999, leotle.TLESetNumber()),
                    () -> assertEquals(51.6445, leotle.Inclination()),
                    () -> assertEquals(336.0056, leotle.LAN()),
                    () -> assertEquals(0.0006830, leotle.Eccentricity()),
                    () -> assertEquals(51.7508, leotle.AOP()),
                    () -> assertEquals(17.5213, leotle.MeanAnomaly()),
                    () -> assertEquals(15.49594026, leotle.MeanMotion()),
                    () -> assertEquals(32265, leotle.RevolutionNumber()));
        }

        @Test
        @DisplayName("GEO constructor test")
        public void geoConstructorTest() {
            Assertions.assertAll(() -> assertEquals("TDRS 3", geotle.Name(), "Name"),
                    () -> assertEquals(19548, geotle.CatalogNumber(), "Catalog number"),
                    () -> assertEquals('U', geotle.Classification(), "Classification"),
                    () -> assertEquals("88091B", geotle.CosparID(), "Cospar ID"),
                    () -> assertEquals(2022, geotle.EpochYear(), "Epoch year"),
                    () -> assertEquals(22.74750692, geotle.EpochDay(), "Epoch day"),
                    () -> assertEquals(-0.00000288, geotle.MeanMotionDot(), "Mean motion dot"),
                    () -> assertEquals(0.0, geotle.MeanMotionDDot(), "Mean motion ddot"),
                    () -> assertEquals(0.0, geotle.BStar(), "BStar"),
                    () -> assertEquals(0, geotle.Ephemeris(), "Ephemeris"),
                    () -> assertEquals(999, geotle.TLESetNumber(), "Set number"),
                    () -> assertEquals(13.8173, geotle.Inclination(), "Inclination"),
                    () -> assertEquals(352.4733, geotle.LAN(), "LAN"),
                    () -> assertEquals(0.0040871, geotle.Eccentricity(), "Eccentricity"),
                    () -> assertEquals(328.9668, geotle.AOP(), "AOP"),
                    () -> assertEquals(20.9178, geotle.MeanAnomaly(), "Mean anomaly"),
                    () -> assertEquals(1.00275354, geotle.MeanMotion(), "Mean motion"),
                    () -> assertEquals(10925, geotle.RevolutionNumber(), "Revolution number"));
        }

        @Test
        @DisplayName("MEO constructor test")
        public void meoConstructorTest() {
            Assertions.assertAll(() -> assertEquals("GPS BIIR-2  (PRN 13)", meotle.Name(), "Name"),
                    () -> assertEquals(24876, meotle.CatalogNumber(), "Catalog number"),
                    () -> assertEquals('U', meotle.Classification(), "Classification"),
                    () -> assertEquals("97035A", meotle.CosparID(), "Cospar ID"),
                    () -> assertEquals(2022, meotle.EpochYear(), "Epoch year"),
                    () -> assertEquals(22.27835439, meotle.EpochDay(), "Epoch day"),
                    () -> assertEquals(0.00000064, meotle.MeanMotionDot(), "Mean motion dot"),
                    () -> assertEquals(0.0, meotle.MeanMotionDDot(), "Mean motion ddot"),
                    () -> assertEquals(0.0, meotle.BStar(), "BStar"),
                    () -> assertEquals(0, meotle.Ephemeris(), "Ephemeris"),
                    () -> assertEquals(999, meotle.TLESetNumber(), "Set number"),
                    () -> assertEquals(55.4939, meotle.Inclination(), "Inclination"),
                    () -> assertEquals(161.3762, meotle.LAN(), "LAN"),
                    () -> assertEquals(0.0056142, meotle.Eccentricity(), "Eccentricity"),
                    () -> assertEquals(54.1976, meotle.AOP(), "AOP"),
                    () -> assertEquals(306.2926, meotle.MeanAnomaly(), "Mean anomaly"),
                    () -> assertEquals(2.00563186, meotle.MeanMotion(), "Mean motion"),
                    () -> assertEquals(17972, meotle.RevolutionNumber(), "Revolution number"));
        }

        @Test
        @DisplayName("Molniya constructor test")
        public void molniyaConstructorTest() {
            Assertions.assertAll(() -> assertEquals("MOLNIYA 3-50", molniyatle.Name(), "Name"),
                    () -> assertEquals(25847, molniyatle.CatalogNumber(), "Catalog number"),
                    () -> assertEquals('U', molniyatle.Classification(), "Classification"),
                    () -> assertEquals("99036A", molniyatle.CosparID(), "Cospar ID"),
                    () -> assertEquals(2022, molniyatle.EpochYear(), "Epoch year"),
                    () -> assertEquals(21.95943339, molniyatle.EpochDay(), "Epoch day"),
                    () -> assertEquals(-0.00000413, molniyatle.MeanMotionDot(), "Mean motion dot"),
                    () -> assertEquals(0.0, molniyatle.MeanMotionDDot(), "Mean motion ddot"),
                    () -> assertEquals(-0.00032734, molniyatle.BStar(), "BStar"),
                    () -> assertEquals(0, molniyatle.Ephemeris(), "Ephemeris"),
                    () -> assertEquals(999, molniyatle.TLESetNumber(), "Set number"),
                    () -> assertEquals(63.0212, molniyatle.Inclination(), "Inclination"),
                    () -> assertEquals(357.5613, molniyatle.LAN(), "LAN"),
                    () -> assertEquals(0.7429748, molniyatle.Eccentricity(), "Eccentricity"),
                    () -> assertEquals(281.0640, molniyatle.AOP(), "AOP"),
                    () -> assertEquals(10.6910, molniyatle.MeanAnomaly(), "Mean anomaly"),
                    () -> assertEquals(2.00612477, molniyatle.MeanMotion(), "Mean motion"),
                    () -> assertEquals(16517, molniyatle.RevolutionNumber(), "Revolution number"));
        }

        @Test
        @DisplayName("Polar constructor test")
        public void polarConstructorTest() {
            Assertions.assertAll(() -> assertEquals("POLAR SCOUT KODIAK", polartle.Name(), "Name"),
                    () -> assertEquals(43764, polartle.CatalogNumber(), "Catalog number"),
                    () -> assertEquals('U', polartle.Classification(), "Classification"),
                    () -> assertEquals("18099G", polartle.CosparID(), "Cospar ID"),
                    () -> assertEquals(2022, polartle.EpochYear(), "Epoch year"),
                    () -> assertEquals(22.73232871, polartle.EpochDay(), "Epoch day"),
                    () -> assertEquals(0.00001624, polartle.MeanMotionDot(), "Mean motion dot"),
                    () -> assertEquals(0.0, polartle.MeanMotionDDot(), "Mean motion ddot"),
                    () -> assertEquals(0.00014434, polartle.BStar(), "BStar"),
                    () -> assertEquals(0, polartle.Ephemeris(), "Ephemeris"),
                    () -> assertEquals(999, polartle.TLESetNumber(), "Set number"),
                    () -> assertEquals(97.6452, polartle.Inclination(), "Inclination"),
                    () -> assertEquals(95.6829, polartle.LAN(), "LAN"),
                    () -> assertEquals(0.0016259, polartle.Eccentricity(), "Eccentricity"),
                    () -> assertEquals(38.8651, polartle.AOP(), "AOP"),
                    () -> assertEquals(321.3737, polartle.MeanAnomaly(), "Mean anomaly"),
                    () -> assertEquals(14.97401529, polartle.MeanMotion(), "Mean motion"),
                    () -> assertEquals(17139, polartle.RevolutionNumber(), "Revolution number"));
        }

    }

}