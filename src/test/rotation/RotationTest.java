package test.rotation;

import com.qbizzle.exception.InvalidAxisException;
import com.qbizzle.exception.InvalidEulerRotationLengthException;
import com.qbizzle.math.Matrix;
import com.qbizzle.math.Vector;
import com.qbizzle.referenceframe.Axis;
import com.qbizzle.rotation.EulerAngles;
import com.qbizzle.rotation.Rotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.qbizzle.math.util.*;
import static com.qbizzle.rotation.Rotation.*;
import static org.junit.jupiter.api.Assertions.*;

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
        Vector e1Rotate = Rotate(xMat, e1);
        Vector e2Rotate = Rotate(xMat, e2);
        Vector e3Rotate = Rotate(xMat, e3);
        assertAll(  // x column
                () -> assertEquals(1, e1Rotate.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate.get(1), 0.000001),
                () -> assertEquals(1, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate.get(0), 0.000001),
                () -> assertEquals(-1, e3Rotate.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate.get(2), 0.000001));
        Vector e1Rotate2 = Rotate(Axis.Direction.X, 90, e1);
        Vector e2Rotate2 = Rotate(Axis.Direction.X, 90, e2);
        Vector e3Rotate2 = Rotate(Axis.Direction.X, 90, e3);
        assertAll(  // x column
                () -> assertEquals(1, e1Rotate2.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate2.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate2.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate2.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate2.get(1), 0.000001),
                () -> assertEquals(1, e2Rotate2.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate2.get(0), 0.000001),
                () -> assertEquals(-1, e3Rotate2.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate2.get(2), 0.000001));
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
        Vector e1Rotate = Rotate(xMat, e1);
        Vector e2Rotate = Rotate(xMat, e2);
        Vector e3Rotate = Rotate(xMat, e3);
        assertAll(  // x column
                () -> assertEquals(1, e1Rotate.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate.get(1), 0.000001),
                () -> assertEquals(-1, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate.get(0), 0.000001),
                () -> assertEquals(1, e3Rotate.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate.get(2), 0.000001));
        Vector e1Rotate2 = Rotate(Axis.Direction.X, -90, e1);
        Vector e2Rotate2 = Rotate(Axis.Direction.X, -90, e2);
        Vector e3Rotate2 = Rotate(Axis.Direction.X, -90, e3);
        assertAll(  // x column
                () -> assertEquals(1, e1Rotate2.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate2.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate2.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate2.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate2.get(1), 0.000001),
                () -> assertEquals(-1, e2Rotate2.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate2.get(0), 0.000001),
                () -> assertEquals(1, e3Rotate2.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate2.get(2), 0.000001));
    }
    
    @Test
    @DisplayName("test positive y rotation")
    public void testPositiveYRotation() {
        Matrix yMat = getMatrix(Axis.Direction.Y, 90);
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
        Vector e1Rotate = Rotate(yMat, e1);
        Vector e2Rotate = Rotate(yMat, e2);
        Vector e3Rotate = Rotate(yMat, e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate.get(1), 0.000001),
                () -> assertEquals(-1, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate.get(0), 0.000001),
                () -> assertEquals(1, e2Rotate.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(1, e3Rotate.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate.get(2), 0.000001));
        Vector e1Rotate2 = Rotate(Axis.Direction.Y, 90, e1);
        Vector e2Rotate2 = Rotate(Axis.Direction.Y, 90, e2);
        Vector e3Rotate2 = Rotate(Axis.Direction.Y, 90, e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate2.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate2.get(1), 0.000001),
                () -> assertEquals(-1, e1Rotate2.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate2.get(0), 0.000001),
                () -> assertEquals(1, e2Rotate2.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate2.get(2), 0.000001),
                // z column
                () -> assertEquals(1, e3Rotate2.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate2.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate2.get(2), 0.000001));
    }
    
    @Test
    @DisplayName("test negative y rotation")
    public void testNegativeYRotation() {
        Matrix yMat = getMatrix(Axis.Direction.Y, -90);
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
        Vector e1Rotate = Rotate(yMat, e1);
        Vector e2Rotate = Rotate(yMat, e2);
        Vector e3Rotate = Rotate(yMat, e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate.get(1), 0.000001),
                () -> assertEquals(1, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate.get(0), 0.000001),
                () -> assertEquals(1, e2Rotate.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(-1, e3Rotate.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate.get(2), 0.000001));
        Vector e1Rotate2 = Rotate(Axis.Direction.Y, -90, e1);
        Vector e2Rotate2 = Rotate(Axis.Direction.Y, -90, e2);
        Vector e3Rotate2 = Rotate(Axis.Direction.Y, -90, e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate2.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate2.get(1), 0.000001),
                () -> assertEquals(1, e1Rotate2.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate2.get(0), 0.000001),
                () -> assertEquals(1, e2Rotate2.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate2.get(2), 0.000001),
                // z column
                () -> assertEquals(-1, e3Rotate2.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate2.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate2.get(2), 0.000001));
    }
    
    @Test
    @DisplayName("test positive z rotation")
    public void testPositiveZRotation() {
        Matrix zMat = getMatrix(Axis.Direction.Z, 90);
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
        Vector e1Rotate = Rotate(zMat, e1);
        Vector e2Rotate = Rotate(zMat, e2);
        Vector e3Rotate = Rotate(zMat, e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate.get(0), 0.000001),
                () -> assertEquals(1, e1Rotate.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(-1, e2Rotate.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate.get(1), 0.000001),
                () -> assertEquals(1, e3Rotate.get(2), 0.000001));
        Vector e1Rotate2 = Rotate(Axis.Direction.Z, 90, e1);
        Vector e2Rotate2 = Rotate(Axis.Direction.Z, 90, e2);
        Vector e3Rotate2 = Rotate(Axis.Direction.Z, 90, e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate2.get(0), 0.000001),
                () -> assertEquals(1, e1Rotate2.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate2.get(2), 0.000001),
                // y column
                () -> assertEquals(-1, e2Rotate2.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate2.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate2.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate2.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate2.get(1), 0.000001),
                () -> assertEquals(1, e3Rotate2.get(2), 0.000001));
    }

    @Test
    @DisplayName("test negative z rotation")
    public void testNegativeZRotation() {
        Matrix zMat = getMatrix(Axis.Direction.Z, -90);
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
        Vector e1Rotate = Rotate(zMat, e1);
        Vector e2Rotate = Rotate(zMat, e2);
        Vector e3Rotate = Rotate(zMat, e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate.get(0), 0.000001),
                () -> assertEquals(-1, e1Rotate.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(1, e2Rotate.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate.get(1), 0.000001),
                () -> assertEquals(1, e3Rotate.get(2), 0.000001));
        Vector e1Rotate2 = Rotate(Axis.Direction.Z, -90, e1);
        Vector e2Rotate2 = Rotate(Axis.Direction.Z, -90, e2);
        Vector e3Rotate2 = Rotate(Axis.Direction.Z, -90, e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate2.get(0), 0.000001),
                () -> assertEquals(-1, e1Rotate2.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate2.get(2), 0.000001),
                // y column
                () -> assertEquals(1, e2Rotate2.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate2.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate2.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate2.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate2.get(1), 0.000001),
                () -> assertEquals(1, e3Rotate2.get(2), 0.000001));
    }

    @Test
    @DisplayName("euler ZXY rotation matrix test")
    public void eulerZxyRotationMatrixTest() throws InvalidAxisException, InvalidEulerRotationLengthException {
        Matrix eulerMat = Rotation.getEulerMatrix("ZXY",
                new EulerAngles(90, 90, 90)
        );
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
        Vector e1Rotate = Rotate("ZXY", new EulerAngles(90, 90, 90), e1);
        Vector e2Rotate = Rotate("ZXY", new EulerAngles(90, 90, 90), e2);
        Vector e3Rotate = Rotate("ZXY", new EulerAngles(90, 90, 90), e3);
        assertAll(  // x column
                () -> assertEquals(-1, e1Rotate.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate.get(1), 0.000001),
                () -> assertEquals(1, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate.get(0), 0.000001),
                () -> assertEquals(1, e3Rotate.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate.get(2), 0.000001));
    }

    @Test
    @DisplayName("euler ZYX rotation matrix test")
    public void eulerZyxRotationMatrixTest() throws InvalidAxisException, InvalidEulerRotationLengthException {
        Matrix eulerMat = Rotation.getEulerMatrix("ZYX",
                new EulerAngles(90, 90, 90)
        );
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
        Vector e1Rotate = Rotate("ZYX", new EulerAngles(90, 90, 90), e1);
        Vector e2Rotate = Rotate("ZYX", new EulerAngles(90, 90, 90), e2);
        Vector e3Rotate = Rotate("ZYX", new EulerAngles(90, 90, 90), e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate.get(1), 0.000001),
                () -> assertEquals(-1, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate.get(0), 0.000001),
                () -> assertEquals(1, e2Rotate.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(1, e3Rotate.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate.get(2), 0.000001));
    }

    @Test
    @DisplayName("euler YZX rotation matrix test")
    public void eulerYzxRotationMatrixTest() throws InvalidAxisException, InvalidEulerRotationLengthException {
        Matrix eulerMat = Rotation.getEulerMatrix("YZX",
                new EulerAngles(90, 90, 90)
        );
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
        Vector e1Rotate = Rotate("YZX", new EulerAngles(90, 90, 90), e1);
        Vector e2Rotate = Rotate("YZX", new EulerAngles(90, 90, 90), e2);
        Vector e3Rotate = Rotate("YZX", new EulerAngles(90, 90, 90), e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate.get(0), 0.000001),
                () -> assertEquals(1, e1Rotate.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(1, e2Rotate.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate.get(1), 0.000001),
                () -> assertEquals(-1, e3Rotate.get(2), 0.000001));
    }

    @Test
    @DisplayName("euler YXZ rotation matrix test")
    public void eulerYxzRotationMatrixTest() throws InvalidAxisException, InvalidEulerRotationLengthException {
        Matrix eulerMat = Rotation.getEulerMatrix("YXZ",
                new EulerAngles(90, 90, 90)
        );
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
        Vector e1Rotate = Rotate("YXZ", new EulerAngles(90, 90, 90), e1);
        Vector e2Rotate = Rotate("YXZ", new EulerAngles(90, 90, 90), e2);
        Vector e3Rotate = Rotate("YXZ", new EulerAngles(90, 90, 90), e3);
        assertAll(  // x column
                () -> assertEquals(1, e1Rotate.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate.get(1), 0.000001),
                () -> assertEquals(1, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate.get(0), 0.000001),
                () -> assertEquals(-1, e3Rotate.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate.get(2), 0.000001));
    }

    @Test
    @DisplayName("euler XZY rotation matrix test")
    public void eulerXzyRotationMatrixTest() throws InvalidAxisException, InvalidEulerRotationLengthException {
        Matrix eulerMat = Rotation.getEulerMatrix("XZY",
                new EulerAngles(90, 90, 90)
        );
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
        Vector e1Rotate = Rotate("XZY", new EulerAngles(90, 90, 90), e1);
        Vector e2Rotate = Rotate("XZY", new EulerAngles(90, 90, 90), e2);
        Vector e3Rotate = Rotate("XZY", new EulerAngles(90, 90, 90), e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate.get(0), 0.000001),
                () -> assertEquals(1, e1Rotate.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(-1, e2Rotate.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate.get(1), 0.000001),
                () -> assertEquals(1, e3Rotate.get(2), 0.000001));
    }

    @Test
    @DisplayName("euler XYZ rotation matrix test")
    public void eulerXyzRotationMatrixTest() throws InvalidAxisException, InvalidEulerRotationLengthException {
        Matrix eulerMat = Rotation.getEulerMatrix("XYZ",
                new EulerAngles(90, 90, 90)
        );
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
        Vector e1Rotate = Rotate("XYZ", new EulerAngles(90, 90, 90), e1);
        Vector e2Rotate = Rotate("XYZ", new EulerAngles(90, 90, 90), e2);
        Vector e3Rotate = Rotate("XYZ", new EulerAngles(90, 90, 90), e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate.get(1), 0.000001),
                () -> assertEquals(1, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate.get(0), 0.000001),
                () -> assertEquals(-1, e2Rotate.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(1, e3Rotate.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate.get(2), 0.000001));
    }

    @Test
    @DisplayName("intrinsic x axis rotation")
    public void intrinsicXAxisRotation() {
        Matrix xMat = getMatrixIntrinsic(e1, 90);
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
        Vector e1Rotate = RotateIntrinsic(e1, 90, e1);
        Vector e2Rotate = RotateIntrinsic(e1, 90, e2);
        Vector e3Rotate = RotateIntrinsic(e1, 90, e3);
        assertAll(  // x column
                () -> assertEquals(1, e1Rotate.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate.get(1), 0.000001),
                () -> assertEquals(1, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate.get(0), 0.000001),
                () -> assertEquals(-1, e3Rotate.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate.get(2), 0.000001));
    }

    @Test
    @DisplayName("intrinsic y axis rotation")
    public void intrinsicYAxisRotation() {
        Matrix yMat = getMatrixIntrinsic(e2, 90);
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
        Vector e1Rotate = RotateIntrinsic(e2, 90, e1);
        Vector e2Rotate = RotateIntrinsic(e2, 90, e2);
        Vector e3Rotate = RotateIntrinsic(e2, 90, e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate.get(1), 0.000001),
                () -> assertEquals(-1, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate.get(0), 0.000001),
                () -> assertEquals(1, e2Rotate.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(1, e3Rotate.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate.get(2), 0.000001));
    }

    @Test
    @DisplayName("intrinsic z axis rotation")
    public void intrinsicZAxisRotation() {
        Matrix zMat = getMatrixIntrinsic(e3, 90);
        assertAll(  // x column
                () -> assertEquals(0, zMat.get(0, 0), 0.000001, "0-0"),
                () -> assertEquals(1, zMat.get(1, 0), 0.000001, "1-0"),
                () -> assertEquals(0, zMat.get(2, 0), 0.000001, "2-0"),
                // y column
                () -> assertEquals(-1, zMat.get(0, 1), 0.000001, "0-1"),
                () -> assertEquals(0, zMat.get(1, 1), 0.000001, "1-1"),
                () -> assertEquals(0, zMat.get(2, 1), 0.000001, "2-1"),
                // z column
                () -> assertEquals(0, zMat.get(0, 2), 0.000001, "0-2"),
                () -> assertEquals(0, zMat.get(1, 2), 0.000001, "1-2"),
                () -> assertEquals(1, zMat.get(2, 2), 0.000001, "2-2"));
        Vector e1Rotate = RotateIntrinsic(e3, 90, e1);
        Vector e2Rotate = RotateIntrinsic(e3, 90, e2);
        Vector e3Rotate = RotateIntrinsic(e3, 90, e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate.get(0), 0.000001, "e1-x"),
                () -> assertEquals(1, e1Rotate.get(1), 0.000001, "e1-y"),
                () -> assertEquals(0, e1Rotate.get(2), 0.000001, "e1-z"),
                // y column
                () -> assertEquals(-1, e2Rotate.get(0), 0.000001, "e2-x"),
                () -> assertEquals(0, e2Rotate.get(1), 0.000001, "e2-y"),
                () -> assertEquals(0, e2Rotate.get(2), 0.000001, "e2-z"),
                // z column
                () -> assertEquals(0, e3Rotate.get(0), 0.000001, "e3-x"),
                () -> assertEquals(0, e3Rotate.get(1), 0.000001, "e3-y"),
                () -> assertEquals(1, e3Rotate.get(2), 0.000001, "e3-z"));
    }

    @Test
    @DisplayName("euler XYZ extrinsic matrix")
    public void eulerXyzExtrinsicMatrix() {
        Matrix eulerMat = getEulerMatrixExtrinsic("XYZ", new EulerAngles(90, 90, 90));
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
        Vector e1Rotate = RotateExtrinsic("XYZ", new EulerAngles(90, 90, 90), e1);
        Vector e2Rotate = RotateExtrinsic("XYZ", new EulerAngles(90, 90, 90), e2);
        Vector e3Rotate = RotateExtrinsic("XYZ", new EulerAngles(90, 90, 90), e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate.get(1), 0.000001),
                () -> assertEquals(-1, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate.get(0), 0.000001),
                () -> assertEquals(1, e2Rotate.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(1, e3Rotate.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate.get(2), 0.000001));
    }

    @Test
    @DisplayName("euler XZY extrinsic matrix")
    public void eulerXzyExtrinsicMatrix() {
        Matrix eulerMat = getEulerMatrixExtrinsic("XZY", new EulerAngles(90, 90, 90));
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
        Vector e1Rotate = RotateExtrinsic("XZY", new EulerAngles(90, 90, 90), e1);
        Vector e2Rotate = RotateExtrinsic("XZY", new EulerAngles(90, 90, 90), e2);
        Vector e3Rotate = RotateExtrinsic("XZY", new EulerAngles(90, 90, 90), e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate.get(0), 0.000001),
                () -> assertEquals(1, e1Rotate.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(1, e2Rotate.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate.get(1), 0.000001),
                () -> assertEquals(-1, e3Rotate.get(2), 0.000001));
    }

    @Test
    @DisplayName("euler YXZ extrinsic matrix")
    public void eulerYxzExtrinsicMatrix() {
        Matrix eulerMat = getEulerMatrixExtrinsic("YXZ", new EulerAngles(90, 90, 90));
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
        Vector e1Rotate = RotateExtrinsic("YXZ", new EulerAngles(90, 90, 90), e1);
        Vector e2Rotate = RotateExtrinsic("YXZ", new EulerAngles(90, 90, 90), e2);
        Vector e3Rotate = RotateExtrinsic("YXZ", new EulerAngles(90, 90, 90), e3);
        assertAll(  // x column
                () -> assertEquals(-1, e1Rotate.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate.get(1), 0.000001),
                () -> assertEquals(1, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate.get(0), 0.000001),
                () -> assertEquals(1, e3Rotate.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate.get(2), 0.000001));
    }

    @Test
    @DisplayName("euler YZX extrinsic matrix")
    public void eulerYzxExtrinsicMatrix() {
        Matrix eulerMat = getEulerMatrixExtrinsic("YZX", new EulerAngles(90, 90, 90));
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
        Vector e1Rotate = RotateExtrinsic("YZX", new EulerAngles(90, 90, 90), e1);
        Vector e2Rotate = RotateExtrinsic("YZX", new EulerAngles(90, 90, 90), e2);
        Vector e3Rotate = RotateExtrinsic("YZX", new EulerAngles(90, 90, 90), e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate.get(0), 0.000001),
                () -> assertEquals(1, e1Rotate.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(-1, e2Rotate.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate.get(1), 0.000001),
                () -> assertEquals(1, e3Rotate.get(2), 0.000001));
    }

    @Test
    @DisplayName("euler ZXY extrinsic matrix")
    public void eulerZxyExtrinsicMatrix() {
        Matrix eulerMat = getEulerMatrixExtrinsic("ZXY", new EulerAngles(90, 90, 90));
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
        Vector e1Rotate = RotateExtrinsic("ZXY", new EulerAngles(90, 90, 90), e1);
        Vector e2Rotate = RotateExtrinsic("ZXY", new EulerAngles(90, 90, 90), e2);
        Vector e3Rotate = RotateExtrinsic("ZXY", new EulerAngles(90, 90, 90), e3);
        assertAll(  // x column
                () -> assertEquals(1, e1Rotate.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate.get(1), 0.000001),
                () -> assertEquals(0, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate.get(0), 0.000001),
                () -> assertEquals(0, e2Rotate.get(1), 0.000001),
                () -> assertEquals(1, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(0, e3Rotate.get(0), 0.000001),
                () -> assertEquals(-1, e3Rotate.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate.get(2), 0.000001));
    }

    @Test
    @DisplayName("euler ZYX extrinsic matrix")
    public void eulerZyxExtrinsicMatrix() {
        Matrix eulerMat = getEulerMatrixExtrinsic("ZYX", new EulerAngles(90, 90, 90));
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
        Vector e1Rotate = RotateExtrinsic("ZYX", new EulerAngles(90, 90, 90), e1);
        Vector e2Rotate = RotateExtrinsic("ZYX", new EulerAngles(90, 90, 90), e2);
        Vector e3Rotate = RotateExtrinsic("ZYX", new EulerAngles(90, 90, 90), e3);
        assertAll(  // x column
                () -> assertEquals(0, e1Rotate.get(0), 0.000001),
                () -> assertEquals(0, e1Rotate.get(1), 0.000001),
                () -> assertEquals(1, e1Rotate.get(2), 0.000001),
                // y column
                () -> assertEquals(0, e2Rotate.get(0), 0.000001),
                () -> assertEquals(-1, e2Rotate.get(1), 0.000001),
                () -> assertEquals(0, e2Rotate.get(2), 0.000001),
                // z column
                () -> assertEquals(1, e3Rotate.get(0), 0.000001),
                () -> assertEquals(0, e3Rotate.get(1), 0.000001),
                () -> assertEquals(0, e3Rotate.get(2), 0.000001));
    }

    //  EXCEPTIONS

    @Test
    @DisplayName("test InvalidEulerRotationLengthException")
    public void testInvalidEulerRotationLengthException() {
        assertThrows(InvalidEulerRotationLengthException.class, () -> {
            getEulerMatrix("XY", new EulerAngles(0, 0, 0));
        }, "Too few axes");
        assertThrows(InvalidEulerRotationLengthException.class, () ->{
            getEulerMatrix("XYZX", new EulerAngles(0, 0, 0));
        }, "Too many axes");
    }

    @Test
    @DisplayName("test InvalidAxisException")
    public void testInvalidAxisException() {
        assertThrows(InvalidAxisException.class, () -> {
            getEulerMatrix("XIZ", new EulerAngles(90, 90, 90));
        });
        assertThrows(InvalidAxisException.class, () -> {
            getEulerMatrix("AYZ", new EulerAngles(90, 90, 90));
        });
        assertThrows(InvalidAxisException.class, () -> {
            getEulerMatrix("YXN", new EulerAngles(90, 90, 90));
        });
    }

}