package test.math;

import com.qbizzle.Math.Matrix;
import com.qbizzle.Math.Vector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {
    Matrix mat1 = new Matrix();
    Matrix mat2 = new Matrix();

    MatrixTest() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double val = 1.0 + i*3 + j + ((1.0 + i*3 + j) / 10.0);
                mat1.set(i, j, val);
                mat2.set(i, j, val);
            }
        }
    }

    @Test
    @DisplayName("ctor test")
    public void ctorTest() {
        assertAll(() -> assertEquals(1.1, mat1.get(0, 0)),
                () -> assertEquals(2.2, mat1.get(0, 1)),
                () -> assertEquals(3.3, mat1.get(0, 2)),
                () -> assertEquals(4.4, mat1.get(1, 0)),
                () -> assertEquals(5.5, mat1.get(1, 1)),
                () -> assertEquals(6.6, mat1.get(1, 2)),
                () -> assertEquals(7.7, mat1.get(2, 0)),
                () -> assertEquals(8.8, mat1.get(2, 1)),
                () -> assertEquals(9.9, mat1.get(2, 2)));
    }

    @Test
    @DisplayName("Test equals overridden method")
    public void testEqualsOverriddenMethod() {
        assertEquals(mat1, mat2);
    }

    @Test
    @DisplayName("Test toString overridden method")
    public void testToStringOverriddenMethod() {
        assertEquals(mat1.toString(),
                "[ 1.1, 2.2, 3.3\n  4.4, 5.5, 6.6\n  7.7, 8.8, 9.9 ]"
                );
    }

    @Test
    @DisplayName("Test clone overridden method")
    public void testCloneOverriddenMethod() {
        Matrix mat = (Matrix)mat1.clone();
        assertAll(() -> assertEquals(1.1, mat.get(0, 0)),
                () -> assertEquals(2.2, mat.get(0, 1)),
                () -> assertEquals(3.3, mat.get(0, 2)),
                () -> assertEquals(4.4, mat.get(1, 0)),
                () -> assertEquals(5.5, mat.get(1, 1)),
                () -> assertEquals(6.6, mat.get(1, 2)),
                () -> assertEquals(7.7, mat.get(2, 0)),
                () -> assertEquals(8.8, mat.get(2, 1)),
                () -> assertEquals(9.9, mat.get(2, 2)));
    }

    @Test
    @DisplayName("Test vector constructor")
    public void testVectorConstructor() {
        Vector col1 = new Vector(1.1, 4.4, 7.7);
        Vector col2 = new Vector(2.2, 5.5, 8.8);
        Vector col3 = new Vector(3.3, 6.6, 9.9);
        Matrix mat = new Matrix(col1, col2, col3);
        assertAll(() -> assertEquals(1.1, mat.get(0, 0)),
                () -> assertEquals(2.2, mat.get(0, 1)),
                () -> assertEquals(3.3, mat.get(0, 2)),
                () -> assertEquals(4.4, mat.get(1, 0)),
                () -> assertEquals(5.5, mat.get(1, 1)),
                () -> assertEquals(6.6, mat.get(1, 2)),
                () -> assertEquals(7.7, mat.get(2, 0)),
                () -> assertEquals(8.8, mat.get(2, 1)),
                () -> assertEquals(9.9, mat.get(2, 2)));
    }

    @Test
    @DisplayName("Test getter out of bounds exception")
    public void testGetterOutOfBoundsException() {
        assertAll(() -> assertThrows(ArrayIndexOutOfBoundsException.class,
                        () -> mat1.get(4, 4)),
                () -> assertThrows(ArrayIndexOutOfBoundsException.class,
                        () -> mat1.get(-1, -1)));
    }

    @Test
    @DisplayName("Test setter out of bounds exception")
    public void testSetterOutOfBoundsException() {
        assertAll(() -> assertThrows(ArrayIndexOutOfBoundsException.class,
                        () -> mat1.set(4, 4, 10.0)),
                () -> assertThrows(ArrayIndexOutOfBoundsException.class,
                        () -> mat1.set(-1, -1, 10.0)));
    }

    @Test
    @DisplayName("Test column getter method out of bounds exception")
    public void testColumnGetterMethodOutOfBoundsException() {
        assertAll(() -> assertThrows(ArrayIndexOutOfBoundsException.class,
                        () -> mat1.getColumn(4)),
                () -> assertThrows(ArrayIndexOutOfBoundsException.class,
                        () -> mat1.getColumn(-1)));
    }

    @Test
    @DisplayName("Test plus operator method")
    public void testPlusOperatorMethod() {
        Matrix mat = mat1.plus(mat2);
        assertAll(() -> assertEquals(2.2, mat.get(0, 0)),
                () -> assertEquals(4.4, mat.get(0, 1)),
                () -> assertEquals(6.6, mat.get(0, 2)),
                () -> assertEquals(8.8, mat.get(1, 0)),
                () -> assertEquals(11.0, mat.get(1, 1)),
                () -> assertEquals(13.2, mat.get(1, 2)),
                () -> assertEquals(15.4, mat.get(2, 0)),
                () -> assertEquals(17.6, mat.get(2, 1)),
                () -> assertEquals(19.8, mat.get(2, 2)));
    }

    @Test
    @DisplayName("Test plus assignment operator method")
    public void testPlusAssignmentOperatorMethod() {
        Matrix mat = (Matrix)mat1.clone();
        mat.plusEquals(mat2);
        assertAll(() -> assertEquals(2.2, mat.get(0, 0)),
                () -> assertEquals(4.4, mat.get(0, 1)),
                () -> assertEquals(6.6, mat.get(0, 2)),
                () -> assertEquals(8.8, mat.get(1, 0)),
                () -> assertEquals(11.0, mat.get(1, 1)),
                () -> assertEquals(13.2, mat.get(1, 2)),
                () -> assertEquals(15.4, mat.get(2, 0)),
                () -> assertEquals(17.6, mat.get(2, 1)),
                () -> assertEquals(19.8, mat.get(2, 2)));
    }

    @Test
    @DisplayName("Test unary minus operator method")
    public void testUnaryMinusOperatorMethod() {
        Matrix mat = mat1.minus();
        assertAll(() -> assertEquals(1.1, mat.get(0, 0)),
                () -> assertEquals(2.2, mat.get(0, 1)),
                () -> assertEquals(3.3, mat.get(0, 2)),
                () -> assertEquals(4.4, mat.get(1, 0)),
                () -> assertEquals(5.5, mat.get(1, 1)),
                () -> assertEquals(6.6, mat.get(1, 2)),
                () -> assertEquals(7.7, mat.get(2, 0)),
                () -> assertEquals(8.8, mat.get(2, 1)),
                () -> assertEquals(9.9, mat.get(2, 2)));
    }

    @Test
    @DisplayName("Test minus operator method")
    public void testMinusOperatorMethod() {
        Matrix mat = mat1.minus(mat2);
        assertEquals(mat, new Matrix());
    }

    @Test
    @DisplayName("Test minus assignment operator method")
    public void testMinusAssignmentOperatorMethod() {
        Matrix mat = (Matrix)mat1.clone();
        mat.minusEquals(mat2);
        assertEquals(mat, new Matrix());
    }
    
    @Test
    @DisplayName("Test scalar multiplication operator method")
    public void testScalarMultiplicationOperatorMethod() {
        Matrix mat = mat1.scale(6.8);
        assertAll(() -> assertEquals(7.48, mat.get(0, 0), 0.0000001),
                () -> assertEquals(14.96, mat.get(0, 1), 0.0000001),
                () -> assertEquals(22.44, mat.get(0, 2), 0.0000001),
                () -> assertEquals(29.92, mat.get(1, 0), 0.0000001),
                () -> assertEquals(37.4, mat.get(1, 1), 0.0000001),
                () -> assertEquals(44.88, mat.get(1, 2), 0.0000001),
                () -> assertEquals(52.36, mat.get(2, 0), 0.0000001),
                () -> assertEquals(59.84, mat.get(2, 1), 0.0000001),
                () -> assertEquals(67.32, mat.get(2, 2), 0.0000001));
    }

    @Test
    @DisplayName("Test scalar multiplication assignment operator method")
    public void testScalarMultiplicationAssignmentOperatorMethod() {
        Matrix mat = (Matrix)mat1.clone();
        mat.scaleEquals(6.8);
        assertAll(() -> assertEquals(7.48, mat.get(0, 0), 0.0000001),
                () -> assertEquals(14.96, mat.get(0, 1), 0.0000001),
                () -> assertEquals(22.44, mat.get(0, 2),0.000001),
                () -> assertEquals(29.92, mat.get(1, 0), 0.0000001),
                () -> assertEquals(37.4, mat.get(1, 1), 0.0000001),
                () -> assertEquals(44.88, mat.get(1, 2), 0.000001),
                () -> assertEquals(52.36, mat.get(2, 0), 0.0000001),
                () -> assertEquals(59.84, mat.get(2, 1), 0.0000001),
                () -> assertEquals(67.32, mat.get(2, 2), 0.0000001));
    }

    @Test
    @DisplayName("Test scalar multiplication operator method with inverse")
    public void testScalarMultiplicationOperatorMethodWithInverse() {
        Matrix mat = mat1.scale(1.0 / 6.8);
        assertAll(() -> assertEquals(0.16176, Math.round(mat.get(0, 0)*1e5)/1e5),
                () -> assertEquals(0.32353, Math.round(mat.get(0, 1)*1e5)/1e5),
                () -> assertEquals(0.48529, Math.round(mat.get(0, 2)*1e5)/1e5),
                () -> assertEquals(0.64706, Math.round(mat.get(1, 0)*1e5)/1e5),
                () -> assertEquals(0.80882, Math.round(mat.get(1, 1)*1e5)/1e5),
                () -> assertEquals(0.97059, Math.round(mat.get(1, 2)*1e5)/1e5),
                () -> assertEquals(1.13235, Math.round(mat.get(2, 0)*1e5)/1e5),
                () -> assertEquals(1.29412, Math.round(mat.get(2, 1)*1e5)/1e5),
                () -> assertEquals(1.45588, Math.round(mat.get(2, 2)*1e5)/1e5));
    }

    @Test
    @DisplayName("Test scalar multiplication assignment operator method with inverse")
    public void testScalarMultiplicationAssignmentOperatorMethodWithInverse() {
        Matrix mat = (Matrix)mat1.clone();
        mat.scaleEquals(1.0/6.8);
        assertAll(() -> assertEquals(0.16176, Math.round(mat.get(0, 0)*1e5)/1e5),
                () -> assertEquals(0.32353, Math.round(mat.get(0, 1)*1e5)/1e5),
                () -> assertEquals(0.48529, Math.round(mat.get(0, 2)*1e5)/1e5),
                () -> assertEquals(0.64706, Math.round(mat.get(1, 0)*1e5)/1e5),
                () -> assertEquals(0.80882, Math.round(mat.get(1, 1)*1e5)/1e5),
                () -> assertEquals(0.97059, Math.round(mat.get(1, 2)*1e5)/1e5),
                () -> assertEquals(1.13235, Math.round(mat.get(2, 0)*1e5)/1e5),
                () -> assertEquals(1.29412, Math.round(mat.get(2, 1)*1e5)/1e5),
                () -> assertEquals(1.45588, Math.round(mat.get(2, 2)*1e5)/1e5));
    }

    @Test
    @DisplayName("Test Vector multiplication operator method")
    public void testVectorMultiplicationOperatorMethod() {
        Vector vec = new Vector(1.1, 2.2, 3.3);
        Vector ans = mat1.mult(vec);
        assertAll(() -> assertEquals(16.94, ans.x(), 0.000000001),
                () -> assertEquals(38.72, ans.y(), 0.0000000001),
                () -> assertEquals(60.5, ans.z(), 0.0000000001));
    }

    @Test
    @DisplayName("Test matrix multiplication operator method")
    public void testMatrixMultiplicationOperatorMethod() {
        Matrix mat = mat1.mult(mat2);
        assertAll(() -> assertEquals(36.3, Math.round(mat.get(0, 0)*1e5)/1e5),
                () -> assertEquals(43.56, Math.round(mat.get(0, 1)*1e5)/1e5),
                () -> assertEquals(50.82, Math.round(mat.get(0, 2)*1e5)/1e5),
                () -> assertEquals(79.86, Math.round(mat.get(1, 0)*1e5)/1e5),
                () -> assertEquals(98.01, Math.round(mat.get(1, 1)*1e5)/1e5),
                () -> assertEquals(116.16, Math.round(mat.get(1, 2)*1e5)/1e5),
                () -> assertEquals(123.42, Math.round(mat.get(2, 0)*1e5)/1e5),
                () -> assertEquals(152.46, Math.round(mat.get(2, 1)*1e5)/1e5),
                () -> assertEquals(181.5, Math.round(mat.get(2, 2)*1e5)/1e5));
    }

    @Test
    @DisplayName("Test matrix multiplication assignment operator method")
    public void testMatrixMultiplicationAssignmentOperatorMethod() {
        Matrix mat = (Matrix)mat1.clone();
        mat.multEquals(mat2);
        assertAll(() -> assertEquals(36.3, Math.round(mat.get(0, 0)*1e5)/1e5),
                () -> assertEquals(43.56, Math.round(mat.get(0, 1)*1e5)/1e5),
                () -> assertEquals(50.82, Math.round(mat.get(0, 2)*1e5)/1e5),
                () -> assertEquals(79.86, Math.round(mat.get(1, 0)*1e5)/1e5),
                () -> assertEquals(98.01, Math.round(mat.get(1, 1)*1e5)/1e5),
                () -> assertEquals(116.16, Math.round(mat.get(1, 2)*1e5)/1e5),
                () -> assertEquals(123.42, Math.round(mat.get(2, 0)*1e5)/1e5),
                () -> assertEquals(152.46, Math.round(mat.get(2, 1)*1e5)/1e5),
                () -> assertEquals(181.5, Math.round(mat.get(2, 2)*1e5)/1e5));
    }

}