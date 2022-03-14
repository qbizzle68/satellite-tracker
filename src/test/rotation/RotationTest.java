package test.rotation;

import com.qbizzle.math.Matrix;
import com.qbizzle.math.Vector;
import com.qbizzle.math.util;
import com.qbizzle.referenceframe.Axis;
import com.qbizzle.referenceframe.EulerAngles;
import com.qbizzle.referenceframe.EulerOrderList;
import com.qbizzle.referenceframe.ReferenceFrame;
import com.qbizzle.rotation.Rotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RotationTest {

    final double cos15 = Math.cos(Math.toRadians(15));
    final double sin15 = Math.sin(Math.toRadians(15));
    final double cos30 = Math.cos(Math.toRadians(30));
    final double sin30 = Math.sin(Math.toRadians(30));
    final double cos45 = Math.cos(Math.toRadians(45));
    final double sin45 = Math.sin(Math.toRadians(45));
    final double epsilon = 0.00001;
    Matrix xyz, yzx, zxy, zyx2xyz, zyx2yzx;

    RotationTest() {
        xyz = new Matrix();
        yzx = new Matrix();
        zxy = new Matrix();
        zyx2xyz = new Matrix();
        zyx2yzx = new Matrix();

        xyz.set(0, 0, 0.6123724357);
        xyz.set(1, 0, 0.7745190528);
        xyz.set(2, 0, -0.1584936491);
        xyz.set(0, 1, -0.6123724357);
        xyz.set(1, 1, 0.5915063509);
        xyz.set(2, 1, 0.5245190528);
        xyz.set(0, 2, 0.5);
        xyz.set(1, 2, -0.224143868);
        xyz.set(2, 2, 0.8365163037);

        yzx.set(0, 0, 0.8365163037);
        yzx.set(1, 0, 0.5);
        yzx.set(2, 0, -0.224143868);
        yzx.set(0, 1, -0.1584936491);
        yzx.set(1, 1, 0.6123724357);
        yzx.set(2, 1, 0.7745190528);
        yzx.set(0, 2, 0.5245190528);
        yzx.set(1, 2, -0.6123724357);
        yzx.set(2, 2, 0.5915063509);

        zxy.set(0, 0, 0.5915063509);
        zxy.set(1, 0, 0.5245190528);
        zxy.set(2, 0, -0.6123724357);
        zxy.set(0, 1, -0.224143868);
        zxy.set(1, 1, 0.8365163037);
        zxy.set(2, 1, 0.5);
        zxy.set(0, 2, 0.7745190528);
        zxy.set(1, 2, -0.1584936491);
        zxy.set(2, 2, 0.6123724357);

        zyx2xyz.set(0, 0, 0.765110473);
        zyx2xyz.set(1, 0, 0.5998797632);
        zyx2xyz.set(2, 0, -0.2339890706);
        zyx2xyz.set(0, 1, -0.6419365314);
        zyx2xyz.set(1, 1, 0.6822768067);
        zyx2xyz.set(2, 1, -0.3498797632);
        zyx2xyz.set(0, 2, -0.0502404736);
        zyx2xyz.set(1, 2, 0.4179026546);
        zyx2xyz.set(2, 2, 0.9071015743);

        zyx2yzx.set(0, 0, 0.9239033945);
        zyx2yzx.set(1, 0, -0.3825825215);
        zyx2yzx.set(2, 0, 0.0057560374);
        zyx2yzx.set(0, 1, 0.3825825215);
        zyx2yzx.set(1, 1, 0.923468001);
        zyx2yzx.set(2, 1, -0.0289389953);
        zyx2yzx.set(0, 2, 0.0057560374);
        zyx2yzx.set(1, 2, 0.0289389953);
        zyx2yzx.set(2, 2, 0.9995646065);
    }

    @Test
    @DisplayName("Derive X rotation matrix")
    public void deriveXRotationMatrix() {
        Matrix mat = Rotation.getMatrix(Axis.Direction.X, 30);
        assertAll( //  x column
                () -> assertEquals(1, mat.get(0, 0), epsilon, "(0, 0)"),
                () -> assertEquals(0, mat.get(1, 0), epsilon, "(1, 0)"),
                () -> assertEquals(0, mat.get(2, 0), epsilon, "(2, 0)"),
                // y column
                () -> assertEquals(0, mat.get(0, 1), epsilon, "(0, 1)"),
                () -> assertEquals(cos30, mat.get(1, 1), epsilon, "(1, 1)"),
                () -> assertEquals(sin30, mat.get(2, 1), epsilon, "(2, 1)"),
                // z column
                () -> assertEquals(0, mat.get(0, 2), epsilon, "(0, 2)"),
                () -> assertEquals(-sin30, mat.get(1, 2), epsilon, "(1, 2)"),
                () -> assertEquals(cos30, mat.get(2, 2), epsilon, "(2, 2)")
        );
    }

    @Test
    @DisplayName("Derive Y rotation matrix")
    public void deriveYRotationMatrix() {
        Matrix mat = Rotation.getMatrix(Axis.Direction.Y, 30);
        assertAll( //  x column
                () -> assertEquals(cos30, mat.get(0, 0), epsilon, "(0, 0)"),
                () -> assertEquals(0, mat.get(1, 0), epsilon, "(1, 0)"),
                () -> assertEquals(-sin30, mat.get(2, 0), epsilon, "(2, 0)"),
                // y column
                () -> assertEquals(0, mat.get(0, 1), epsilon, "(0, 1)"),
                () -> assertEquals(1, mat.get(1, 1), epsilon, "(1, 1)"),
                () -> assertEquals(0, mat.get(2, 1), epsilon, "(2, 1)"),
                // z column
                () -> assertEquals(sin30, mat.get(0, 2), epsilon, "(0, 2)"),
                () -> assertEquals(0, mat.get(1, 2), epsilon, "(1, 2)"),
                () -> assertEquals(cos30, mat.get(2, 2), epsilon, "(2, 2)")
        );
    }

    @Test
    @DisplayName("Derive Z rotation matrix")
    public void deriveZRotationMatrix() {
        Matrix mat = Rotation.getMatrix(Axis.Direction.Z, 30);
        assertAll( //  x column
                () -> assertEquals(cos30, mat.get(0, 0), epsilon, "(0, 0)"),
                () -> assertEquals(sin30, mat.get(1, 0), epsilon, "(1, 0)"),
                () -> assertEquals(0, mat.get(2, 0), epsilon, "(2, 0)"),
                // y column
                () -> assertEquals(-sin30, mat.get(0, 1), epsilon, "(0, 1)"),
                () -> assertEquals(cos30, mat.get(1, 1), epsilon, "(1, 1)"),
                () -> assertEquals(0, mat.get(2, 1), epsilon, "(2, 1)"),
                // z column
                () -> assertEquals(0, mat.get(0, 2), epsilon, "(0, 2)"),
                () -> assertEquals(0, mat.get(1, 2), epsilon, "(1, 2)"),
                () -> assertEquals(1, mat.get(2, 2), epsilon, "(2, 2)")
        );
    }

    @Test
    @DisplayName("Derive intrinsic X rotation matrix")
    public void deriveIntrinsicXRotationMatrix() {
        Matrix mat = Rotation.getMatrixIntrinsic(util.e1, 45);
        assertAll( //  x column
                () -> assertEquals(1, mat.get(0, 0), epsilon, "(0, 0)"),
                () -> assertEquals(0, mat.get(1, 0), epsilon, "(1, 0)"),
                () -> assertEquals(0, mat.get(2, 0), epsilon, "(2, 0)"),
                // y column
                () -> assertEquals(0, mat.get(0, 1), epsilon, "(0, 1)"),
                () -> assertEquals(cos45, mat.get(1, 1), epsilon, "(1, 1)"),
                () -> assertEquals(sin45, mat.get(2, 1), epsilon, "(2, 1)"),
                // z column
                () -> assertEquals(0, mat.get(0, 2), epsilon, "(0, 2)"),
                () -> assertEquals(-sin45, mat.get(1, 2), epsilon, "(1, 2)"),
                () -> assertEquals(cos45, mat.get(2, 2), epsilon, "(2, 2)")
        );
    }

    @Test
    @DisplayName("Derive intrinsic Y rotation matrix")
    public void deriveIntrinsicYRotationMatrix() {
        Matrix mat = Rotation.getMatrixIntrinsic(util.e2, 45);
        assertAll( //  x column
                () -> assertEquals(cos45, mat.get(0, 0), epsilon, "(0, 0)"),
                () -> assertEquals(0, mat.get(1, 0), epsilon, "(1, 0)"),
                () -> assertEquals(-sin45, mat.get(2, 0), epsilon, "(2, 0)"),
                // y column
                () -> assertEquals(0, mat.get(0, 1), epsilon, "(0, 1)"),
                () -> assertEquals(1, mat.get(1, 1), epsilon, "(1, 1)"),
                () -> assertEquals(0, mat.get(2, 1), epsilon, "(2, 1)"),
                // z column
                () -> assertEquals(sin45, mat.get(0, 2), epsilon, "(0, 2)"),
                () -> assertEquals(0, mat.get(1, 2), epsilon, "(1, 2)"),
                () -> assertEquals(cos45, mat.get(2, 2), epsilon, "(2, 2)")
        );
    }

    @Test
    @DisplayName("Derive intrinsic Z rotation matrix")
    public void deriveIntrinsicZRotationMatrix() {
        Matrix mat = Rotation.getMatrix(Axis.Direction.Z, 45);
        assertAll( //  x column
                () -> assertEquals(cos45, mat.get(0, 0), epsilon, "(0, 0)"),
                () -> assertEquals(sin45, mat.get(1, 0), epsilon, "(1, 0)"),
                () -> assertEquals(0, mat.get(2, 0), epsilon, "(2, 0)"),
                // y column
                () -> assertEquals(-sin45, mat.get(0, 1), epsilon, "(0, 1)"),
                () -> assertEquals(cos45, mat.get(1, 1), epsilon, "(1, 1)"),
                () -> assertEquals(0, mat.get(2, 1), epsilon, "(2, 1)"),
                // z column
                () -> assertEquals(0, mat.get(0, 2), epsilon, "(0, 2)"),
                () -> assertEquals(0, mat.get(1, 2), epsilon, "(1, 2)"),
                () -> assertEquals(1, mat.get(2, 2), epsilon, "(2, 2)")
        );
    }

    @Test
    @DisplayName("Derive XYZ Euler matrix")
    public void deriveXyzEulerMatrix() {
        Matrix mat = Rotation.getEulerMatrix(EulerOrderList.XYZ, new EulerAngles(15, 30, 45));
        assertAll( //  x column
                () -> assertEquals(0.6123724357, mat.get(0, 0), epsilon, "(0, 0)"),
                () -> assertEquals(0.7745190528, mat.get(1, 0), epsilon, "(1, 0)"),
                () -> assertEquals(-0.1584936491, mat.get(2, 0), epsilon, "(2, 0)"),
                // y column
                () -> assertEquals(-0.6123724357, mat.get(0, 1), epsilon, "(0, 1)"),
                () -> assertEquals(0.5915063509, mat.get(1, 1), epsilon, "(1, 1)"),
                () -> assertEquals(0.5245190528, mat.get(2, 1), epsilon, "(2, 1)"),
                // z column
                () -> assertEquals(0.5, mat.get(0, 2), epsilon, "(0, 2)"),
                () -> assertEquals(-0.224143868, mat.get(1, 2), epsilon, "(1, 2)"),
                () -> assertEquals(0.8365163037, mat.get(2, 2), epsilon, "(2, 2)")
        );
    }

    @Test
    @DisplayName("Derive YZX Euler matrix")
    public void deriveYzxEulerMatrix() {
        Matrix mat = Rotation.getEulerMatrix(EulerOrderList.YZX, new EulerAngles(15, 30, 45));
        assertAll( //  x column
                () -> assertEquals(0.8365163037, mat.get(0, 0), epsilon, "(0, 0)"),
                () -> assertEquals(0.5, mat.get(1, 0), epsilon, "(1, 0)"),
                () -> assertEquals(-0.224143868, mat.get(2, 0), epsilon, "(2, 0)"),
                // y column
                () -> assertEquals(-0.1584936491, mat.get(0, 1), epsilon, "(0, 1)"),
                () -> assertEquals(0.6123724357, mat.get(1, 1), epsilon, "(1, 1)"),
                () -> assertEquals(0.7745190528, mat.get(2, 1), epsilon, "(2, 1)"),
                // z column
                () -> assertEquals(0.5245190528, mat.get(0, 2), epsilon, "(0, 2)"),
                () -> assertEquals(-0.6123724357, mat.get(1, 2), epsilon, "(1, 2)"),
                () -> assertEquals(0.5915063509, mat.get(2, 2), epsilon, "(2, 2)")
        );
    }

    @Test
    @DisplayName("Derive ZXY Euler matrix")
    public void deriveZxyEulerMatrix() {
        Matrix mat = Rotation.getEulerMatrix(EulerOrderList.ZXY, new EulerAngles(15, 30, 45));
        assertAll( //  x column
                () -> assertEquals(0.5915063509, mat.get(0, 0), epsilon, "(0, 0)"),
                () -> assertEquals(0.5245190528, mat.get(1, 0), epsilon, "(1, 0)"),
                () -> assertEquals(-0.6123724357, mat.get(2, 0), epsilon, "(2, 0)"),
                // y column
                () -> assertEquals(-0.224143868, mat.get(0, 1), epsilon, "(0, 1)"),
                () -> assertEquals(0.8365163037, mat.get(1, 1), epsilon, "(1, 1)"),
                () -> assertEquals(0.5, mat.get(2, 1), epsilon, "(2, 1)"),
                // z column
                () -> assertEquals(0.7745190528, mat.get(0, 2), epsilon, "(0, 2)"),
                () -> assertEquals(-0.1584936491, mat.get(1, 2), epsilon, "(1, 2)"),
                () -> assertEquals(0.6123724357, mat.get(2, 2), epsilon, "(2, 2)")
        );
    }

    @Test
    @DisplayName("Derive ZYX to XYZ Euler matrix")
    public void deriveXyzToZyxEulerMatrix() {
        Matrix mat = Rotation.getEulerMatrix(
                EulerOrderList.XYZ,
                new EulerAngles(15, 30, 45),
                EulerOrderList.ZYX,
                new EulerAngles(15, 30, 45)
        );
        assertAll( //  x column
                () -> assertEquals(zyx2xyz.get(0, 0), mat.get(0, 0), epsilon, "(0, 0)"),
                () -> assertEquals(zyx2xyz.get(1, 0), mat.get(1, 0), epsilon, "(1, 0)"),
                () -> assertEquals(zyx2xyz.get(2, 0), mat.get(2, 0), epsilon, "(2, 0)"),
                // y column
                () -> assertEquals(zyx2xyz.get(0, 1), mat.get(0, 1), epsilon, "(0, 1)"),
                () -> assertEquals(zyx2xyz.get(1, 1), mat.get(1, 1), epsilon, "(1, 1)"),
                () -> assertEquals(zyx2xyz.get(2, 1), mat.get(2, 1), epsilon, "(2, 1)"),
                // z column
                () -> assertEquals(zyx2xyz.get(0, 2), mat.get(0, 2), epsilon, "(0, 2)"),
                () -> assertEquals(zyx2xyz.get(1, 2), mat.get(1, 2), epsilon, "(1, 2)"),
                () -> assertEquals(zyx2xyz.get(2, 2), mat.get(2, 2), epsilon, "(2, 2)")
        );
    }

    @Test
    @DisplayName("Derive ZYX to YZX Euler matrix")
    public void deriveZyxToYzxEulerMatrix() {
        ReferenceFrame refZYX = new ReferenceFrame(EulerOrderList.ZYX, new EulerAngles(15, 30, 45));
        ReferenceFrame refYZX = new ReferenceFrame(EulerOrderList.YZX, new EulerAngles(15, 30, 45));
        Matrix mat = Rotation.getEulerMatrix(refZYX, refYZX);
        assertAll( //  x column
                () -> assertEquals(zyx2yzx.get(0, 0), mat.get(0, 0), epsilon, "(0, 0)"),
                () -> assertEquals(zyx2yzx.get(1, 0), mat.get(1, 0), epsilon, "(1, 0)"),
                () -> assertEquals(zyx2yzx.get(2, 0), mat.get(2, 0), epsilon, "(2, 0)"),
                // y column
                () -> assertEquals(zyx2yzx.get(0, 1), mat.get(0, 1), epsilon, "(0, 1)"),
                () -> assertEquals(zyx2yzx.get(1, 1), mat.get(1, 1), epsilon, "(1, 1)"),
                () -> assertEquals(zyx2yzx.get(2, 1), mat.get(2, 1), epsilon, "(2, 1)"),
                // z column
                () -> assertEquals(zyx2yzx.get(0, 2), mat.get(0, 2), epsilon, "(0, 2)"),
                () -> assertEquals(zyx2yzx.get(1, 2), mat.get(1, 2), epsilon, "(1, 2)"),
                () -> assertEquals(zyx2yzx.get(2, 2), mat.get(2, 2), epsilon, "(2, 2)")
        );
    }

    /**
     * put getEulerMatrixExtrinsic() test here
     */

    @Test
    @DisplayName("Rotate to another reference frame")
    public void rotateToAnotherReferenceFrame() {
        Vector rotated = Rotation.rotateTo(xyz, new Vector(1, 1, 1));
        assertAll(
                () -> assertEquals(1.228397839, rotated.get(0), epsilon, "x"),
                () -> assertEquals(0.503652968, rotated.get(1), epsilon, "y"),
                () -> assertEquals(1.112372436, rotated.get(2), epsilon, "z")
        );
    }

    @Test
    @DisplayName("Rotate from another reference frame")
    public void rotateFromAnotherReferenceFrame() {
        Vector rotated = Rotation.rotateFrom(zxy, new Vector(1, 1, 1));
        assertAll(
                () -> assertEquals(1.141881536, rotated.get(0), epsilon, "x"),
                () -> assertEquals(1.202541909, rotated.get(1), epsilon, "y"),
                () -> assertEquals(0.5, rotated.get(2), epsilon, "z")
        );
//        zxy.set(0, 0, 0.5915063509);
//        zxy.set(1, 0, 0.5245190528);
//        zxy.set(2, 0, -0.6123724357);
//        zxy.set(0, 1, -0.224143868);
//        zxy.set(1, 1, 0.8365163037);
//        zxy.set(2, 1, 0.5);
//        zxy.set(0, 2, 0.7745190528);
//        zxy.set(1, 2, -0.1584936491);
//        zxy.set(2, 2, 0.6123724357);
    }

    @Test
    @DisplayName("Rotate to with axis")
    public void rotateToWithAxis() {
        Vector rotated = Rotation.rotateTo(Axis.Direction.X, 45, new Vector(1, 1, 1));
        assertAll(
                () -> assertEquals(1, rotated.get(0), epsilon, "x"),
                () -> assertEquals(cos45 + sin45, rotated.get(1), epsilon, "y"),
                () -> assertEquals(cos45 - sin45, rotated.get(2), epsilon, "z")
        );
    }

    @Test
    @DisplayName("Rotate from with axis")
    public void rotateFromWithAxis() {
        Vector rotated = Rotation.rotateTo(Axis.Direction.Y, 45, new Vector(1, 1, 1));
        assertAll(
                () -> assertEquals(cos45 - sin45, rotated.get(0), epsilon, "x"),
                () -> assertEquals(1, rotated.get(1), epsilon, "y"),
                () -> assertEquals(cos45 + sin45, rotated.get(2), epsilon, "z")
        );
    }

    @Test
    @DisplayName("Rotate intrinsic to with axis")
    public void rotateIntrinsicToWithAxis() {
        Vector rotated = Rotation.rotateIntrinsicTo(util.e3, 45, new Vector(1, 1, 1));
        assertAll(
                () -> assertEquals(cos45 + sin45, rotated.get(0), epsilon, "x"),
                () -> assertEquals(cos45 - sin45, rotated.get(1), epsilon, "y"),
                () -> assertEquals(1, rotated.get(2), epsilon, "z")
        );
    }

    @Test
    @DisplayName("Rotate intrinsic from with axis")
    public void rotateIntrinsicFromWithAxis() {
        Vector rotated = Rotation.rotateIntrinsicTo(util.e3, 45, new Vector(1, 1, 1));
        assertAll(
                () -> assertEquals(cos45 + sin45, rotated.get(0), epsilon, "x"),
                () -> assertEquals(cos45 - sin45, rotated.get(1), epsilon, "y"),
                () -> assertEquals(1, rotated.get(2), epsilon, "z")
        );
    }

    @Test
    @DisplayName("Rotate to Euler order")
    public void rotateToEulerOrder() {
        Vector rotated = Rotation.rotateTo(
                EulerOrderList.XYZ,
                new EulerAngles(15, 30, 45),
                new Vector(1, 1, 1)
        );
        assertAll(
                () -> assertEquals(1.228397839, rotated.get(0), epsilon, "x"),
                () -> assertEquals(0.503652968, rotated.get(1), epsilon, "y"),
                () -> assertEquals(1.112372436, rotated.get(2), epsilon, "z")
        );
    }

    @Test
    @DisplayName("Rotate from Euler order")
    public void rotateFromEulerOrder() {
        Vector rotated = Rotation.rotateFrom(
                EulerOrderList.XYZ,
                new EulerAngles(15, 30, 45),
                new Vector(1, 1, 1)
        );
        assertAll(
                () -> assertEquals(0.5, rotated.get(0), epsilon, "x"),
                () -> assertEquals(1.141881536, rotated.get(1), epsilon, "y"),
                () -> assertEquals(1.202541707, rotated.get(2), epsilon, "z")
        );
    }

    /**
     * add extrinsic rotate to and from tests here
     */

    @Test
    @DisplayName("Rotate between reference frames")
    public void rotateBetweenReferenceFrames() {
        ReferenceFrame refZYX = new ReferenceFrame(EulerOrderList.ZYX, new EulerAngles(15, 30, 45));
        ReferenceFrame refYZX = new ReferenceFrame(EulerOrderList.YZX, new EulerAngles(15, 30, 45));
        Vector rotated = Rotation.rotate(refZYX, refYZX, new Vector(1, 1, 1));
        assertAll(
                () -> assertEquals(1.312241953, rotated.get(0), epsilon, "x"),
                () -> assertEquals(0.5698244748, rotated.get(1), epsilon, "y"),
                () -> assertEquals(0.9763816486, rotated.get(2), epsilon, "z")
        );
    }

}