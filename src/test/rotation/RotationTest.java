package test.rotation;

import com.qbizzle.exception.IllegalRotationAxis;
import com.qbizzle.exception.IllegalRotationAxisNumber;
import com.qbizzle.math.Matrix;
import com.qbizzle.referenceframe.Axis;
import com.qbizzle.rotation.Rotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RotationTest {

    @Test
    @DisplayName("test positive x rotation")
    public void testPositiveXRotation() {
        Matrix xMat = Rotation.getMatrix(Axis.Direction.X, 90);
        assertAll(  // x column
                () -> assertEquals(1, xMat.get(0, 0), 0.000001),
                () -> assertEquals(0, xMat.get(1, 0), 0.000001),
                () -> assertEquals(0, xMat.get(2, 0), 0.000001),
                // y column
                () -> assertEquals(0, xMat.get(0, 1), 0.000001),
                () -> assertEquals(0, xMat.get(1, 1), 0.000001),
                () -> assertEquals(1, xMat.get(2, 1), 0.000001),
                // z column
                () -> assertEquals(0, xMat.get(0, 2), 0.000001),
                () -> assertEquals(-1, xMat.get(1, 2), 0.000001),
                () -> assertEquals(0, xMat.get(2, 2), 0.000001));
    }

    @Test
    @DisplayName("test negative x rotation")
    public void testNegativeXRotation() {
        Matrix xMat = Rotation.getMatrix(Axis.Direction.X, -90);
        assertAll(  // x column
                () -> assertEquals(1, xMat.get(0, 0), 0.000001),
                () -> assertEquals(0, xMat.get(1, 0), 0.000001),
                () -> assertEquals(0, xMat.get(2, 0), 0.000001),
                // y column
                () -> assertEquals(0, xMat.get(0, 1), 0.000001),
                () -> assertEquals(0, xMat.get(1, 1), 0.000001),
                () -> assertEquals(-1, xMat.get(2, 1), 0.000001),
                // z column
                () -> assertEquals(0, xMat.get(0, 2), 0.000001),
                () -> assertEquals(1, xMat.get(1, 2), 0.000001),
                () -> assertEquals(0, xMat.get(2, 2), 0.000001));
    }
    
    @Test
    @DisplayName("test positive y rotation")
    public void testPositiveYRotation() {
        Matrix yMat = Rotation.getMatrix(Axis.Direction.Y, 90);
        assertAll(  // x column
                () -> assertEquals(0, yMat.get(0, 0), 0.000001),
                () -> assertEquals(0, yMat.get(1, 0), 0.000001),
                () -> assertEquals(-1, yMat.get(2, 0), 0.000001),
                // y column
                () -> assertEquals(0, yMat.get(0, 1), 0.000001),
                () -> assertEquals(1, yMat.get(1, 1), 0.000001),
                () -> assertEquals(0, yMat.get(2, 1), 0.000001),
                // z column
                () -> assertEquals(1, yMat.get(0, 2), 0.000001),
                () -> assertEquals(0, yMat.get(1, 2), 0.000001),
                () -> assertEquals(0, yMat.get(2, 2), 0.000001));
    }
    
    @Test
    @DisplayName("test negative y rotation")
    public void testNegativeYRotation() {
        Matrix yMat = Rotation.getMatrix(Axis.Direction.Y, -90);
        assertAll(  // x column
                () -> assertEquals(0, yMat.get(0, 0), 0.000001),
                () -> assertEquals(0, yMat.get(1, 0), 0.000001),
                () -> assertEquals(1, yMat.get(2, 0), 0.000001),
                // y column
                () -> assertEquals(0, yMat.get(0, 1), 0.000001),
                () -> assertEquals(1, yMat.get(1, 1), 0.000001),
                () -> assertEquals(0, yMat.get(2, 1), 0.000001),
                // z column
                () -> assertEquals(-1, yMat.get(0, 2), 0.000001),
                () -> assertEquals(0, yMat.get(1, 2), 0.000001),
                () -> assertEquals(0, yMat.get(2, 2), 0.000001));
    }
    
    @Test
    @DisplayName("test positive z rotation")
    public void testPositiveZRotation() {
        Matrix zMat = Rotation.getMatrix(Axis.Direction.Z, 90);
        assertAll(  // x column
                () -> assertEquals(0, zMat.get(0, 0), 0.000001),
                () -> assertEquals(1, zMat.get(1, 0), 0.000001),
                () -> assertEquals(0, zMat.get(2, 0), 0.000001),
                // y column
                () -> assertEquals(-1, zMat.get(0, 1), 0.000001),
                () -> assertEquals(0, zMat.get(1, 1), 0.000001),
                () -> assertEquals(0, zMat.get(2, 1), 0.000001),
                // z column
                () -> assertEquals(0, zMat.get(0, 2), 0.000001),
                () -> assertEquals(0, zMat.get(1, 2), 0.000001),
                () -> assertEquals(1, zMat.get(2, 2), 0.000001));
    }

    @Test
    @DisplayName("test negative z rotation")
    public void testNegativeZRotation() {
        Matrix zMat = Rotation.getMatrix(Axis.Direction.Z, -90);
        assertAll(  // x column
                () -> assertEquals(0, zMat.get(0, 0), 0.000001),
                () -> assertEquals(-1, zMat.get(1, 0), 0.000001),
                () -> assertEquals(0, zMat.get(2, 0), 0.000001),
                // y column
                () -> assertEquals(1, zMat.get(0, 1), 0.000001),
                () -> assertEquals(0, zMat.get(1, 1), 0.000001),
                () -> assertEquals(0, zMat.get(2, 1), 0.000001),
                // z column
                () -> assertEquals(0, zMat.get(0, 2), 0.000001),
                () -> assertEquals(0, zMat.get(1, 2), 0.000001),
                () -> assertEquals(1, zMat.get(2, 2), 0.000001));
    }

    @Test
    @DisplayName("euler ZXY rotation matrix test")
    public void eulerZxyRotationMatrixTest() throws IllegalRotationAxis, IllegalRotationAxisNumber {
        Matrix eulerMat = Rotation.getEulerMatrix("ZXY", 90, 90, 90);
        assertAll(  // x column
                () -> assertEquals(-1, eulerMat.get(0, 0), 0.000001),
                () -> assertEquals(0, eulerMat.get(1, 0), 0.000001),
                () -> assertEquals(0, eulerMat.get(2, 0), 0.000001),
                // y column
                () -> assertEquals(0, eulerMat.get(0, 1), 0.000001),
                () -> assertEquals(0, eulerMat.get(1, 1), 0.000001),
                () -> assertEquals(1, eulerMat.get(2, 1), 0.000001),
                // z column
                () -> assertEquals(0, eulerMat.get(0, 2), 0.000001),
                () -> assertEquals(1, eulerMat.get(1, 2), 0.000001),
                () -> assertEquals(0, eulerMat.get(2, 2), 0.000001));
    }

    @Test
    @DisplayName("euler ZYX rotation matrix test")
    public void eulerZyxRotationMatrixTest() throws IllegalRotationAxis, IllegalRotationAxisNumber {
        Matrix eulerMat = Rotation.getEulerMatrix("ZYX", 90, 90, 90);
        assertAll(  // x column
                () -> assertEquals(0, eulerMat.get(0, 0), 0.000001),
                () -> assertEquals(0, eulerMat.get(1, 0), 0.000001),
                () -> assertEquals(-1, eulerMat.get(2, 0), 0.000001),
                // y column
                () -> assertEquals(0, eulerMat.get(0, 1), 0.000001),
                () -> assertEquals(1, eulerMat.get(1, 1), 0.000001),
                () -> assertEquals(0, eulerMat.get(2, 1), 0.000001),
                // z column
                () -> assertEquals(1, eulerMat.get(0, 2), 0.000001),
                () -> assertEquals(0, eulerMat.get(1, 2), 0.000001),
                () -> assertEquals(0, eulerMat.get(2, 2), 0.000001));
    }

    @Test
    @DisplayName("euler YZX rotation matrix test")
    public void eulerYzxRotationMatrixTest() throws IllegalRotationAxis, IllegalRotationAxisNumber {
        Matrix eulerMat = Rotation.getEulerMatrix("YZX", 90, 90, 90);
        assertAll(  // x column
                () -> assertEquals(0, eulerMat.get(0, 0), 0.000001),
                () -> assertEquals(1, eulerMat.get(1, 0), 0.000001),
                () -> assertEquals(0, eulerMat.get(2, 0), 0.000001),
                // y column
                () -> assertEquals(1, eulerMat.get(0, 1), 0.000001),
                () -> assertEquals(0, eulerMat.get(1, 1), 0.000001),
                () -> assertEquals(0, eulerMat.get(2, 1), 0.000001),
                // z column
                () -> assertEquals(0, eulerMat.get(0, 2), 0.000001),
                () -> assertEquals(0, eulerMat.get(1, 2), 0.000001),
                () -> assertEquals(-1, eulerMat.get(2, 2), 0.000001));
    }

    @Test
    @DisplayName("euler YXZ rotation matrix test")
    public void eulerYxzRotationMatrixTest() throws IllegalRotationAxis, IllegalRotationAxisNumber {
        Matrix eulerMat = Rotation.getEulerMatrix("YXZ", 90, 90, 90);
        assertAll(  // x column
                () -> assertEquals(1, eulerMat.get(0, 0), 0.000001),
                () -> assertEquals(0, eulerMat.get(1, 0), 0.000001),
                () -> assertEquals(0, eulerMat.get(2, 0), 0.000001),
                // y column
                () -> assertEquals(0, eulerMat.get(0, 1), 0.000001),
                () -> assertEquals(0, eulerMat.get(1, 1), 0.000001),
                () -> assertEquals(1, eulerMat.get(2, 1), 0.000001),
                // z column
                () -> assertEquals(0, eulerMat.get(0, 2), 0.000001),
                () -> assertEquals(-1, eulerMat.get(1, 2), 0.000001),
                () -> assertEquals(0, eulerMat.get(2, 2), 0.000001));
    }

    @Test
    @DisplayName("euler XZY rotation matrix test")
    public void eulerXzyRotationMatrixTest() throws IllegalRotationAxis, IllegalRotationAxisNumber {
        Matrix eulerMat = Rotation.getEulerMatrix("XZY", 90, 90, 90);
        assertAll(  // x column
                () -> assertEquals(0, eulerMat.get(0, 0), 0.000001),
                () -> assertEquals(1, eulerMat.get(1, 0), 0.000001),
                () -> assertEquals(0, eulerMat.get(2, 0), 0.000001),
                // y column
                () -> assertEquals(-1, eulerMat.get(0, 1), 0.000001),
                () -> assertEquals(0, eulerMat.get(1, 1), 0.000001),
                () -> assertEquals(0, eulerMat.get(2, 1), 0.000001),
                // z column
                () -> assertEquals(0, eulerMat.get(0, 2), 0.000001),
                () -> assertEquals(0, eulerMat.get(1, 2), 0.000001),
                () -> assertEquals(1, eulerMat.get(2, 2), 0.000001));
    }

    @Test
    @DisplayName("euler XYZ rotation matrix test")
    public void eulerXyzRotationMatrixTest() throws IllegalRotationAxis, IllegalRotationAxisNumber {
        Matrix eulerMat = Rotation.getEulerMatrix("XYZ", 90, 90, 90);
        assertAll(  // x column
                () -> assertEquals(0, eulerMat.get(0, 0), 0.000001),
                () -> assertEquals(0, eulerMat.get(1, 0), 0.000001),
                () -> assertEquals(1, eulerMat.get(2, 0), 0.000001),
                // y column
                () -> assertEquals(0, eulerMat.get(0, 1), 0.000001),
                () -> assertEquals(-1, eulerMat.get(1, 1), 0.000001),
                () -> assertEquals(0, eulerMat.get(2, 1), 0.000001),
                // z column
                () -> assertEquals(1, eulerMat.get(0, 2), 0.000001),
                () -> assertEquals(0, eulerMat.get(1, 2), 0.000001),
                () -> assertEquals(0, eulerMat.get(2, 2), 0.000001));
    }

}