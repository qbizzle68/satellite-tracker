package test.math;

import com.qbizzle.Math.Vector;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class VectorTest {

    private final Vector emptyConstructor = new Vector();
    private final Vector oneConstructor = new Vector(1.1);
    private final Vector twoConstructor = new Vector(1.1, 2.2);
    private final Vector threeConstructor = new Vector(1.1, 2.2, 3.3);
    private final Vector threeClone = (Vector)threeConstructor.clone();

    @Test
    @DisplayName("Getter tests")
    public void getterTests() {
        Assertions.assertAll(() -> assertEquals(threeConstructor.x(), 1.1),
                             () -> assertEquals(threeConstructor.y(), 2.2),
                             () -> assertEquals(threeConstructor.z(), 3.3));
    }

    @Test
    @DisplayName("Empty constructor call")
    public void emptyConstructorCall() {
        Assertions.assertAll(() -> assertEquals(emptyConstructor.x(), 0.0),
                             () -> assertEquals(emptyConstructor.y(), 0.0),
                             () -> assertEquals(emptyConstructor.z(), 0.0));
    }

    @Test
    @DisplayName("One parameter constructor call")
    public void oneParameterConstructorCall() {
        Assertions.assertAll(() -> assertEquals(oneConstructor.x(), 1.1),
                             () -> assertEquals(oneConstructor.y(), 0.0),
                             () -> assertEquals(oneConstructor.z(), 0.0));
    }

    @Test
    @DisplayName("Two parameter constructor call")
    public void twoParameterConstructorCall() {
        Assertions.assertAll(() -> assertEquals(twoConstructor.x(), 1.1),
                             () -> assertEquals(twoConstructor.y(), 2.2),
                             () -> assertEquals(twoConstructor.z(), 0.0));
    }

    @Test
    @DisplayName("Three parameter constructor call")
    public void threeParameterConstructorCall() {
        Assertions.assertAll(() -> assertEquals(threeConstructor.x(), 1.1),
                             () -> assertEquals(threeConstructor.y(), 2.2),
                             () -> assertEquals(threeConstructor.z(), 3.3));
    }

    @Test
    @DisplayName("Should be properly formatted string")
    public void shouldBeProperlyFormattedString() {
        assertEquals(threeConstructor.toString(), "[1.1, 2.2, 3.3]");
    }

    @Test
    @DisplayName("Cloned value should have the same elements")
    public void clonedValueShouldHaveTheSameElements() {
        Assertions.assertAll(() -> assertEquals(threeConstructor.x(), threeClone.x()),
                             () -> assertEquals(threeConstructor.y(), threeClone.y()),
                             () -> assertEquals(threeConstructor.z(), threeClone.z()));
    }

    @Test
    @DisplayName("Should be two equal vectors")
    public void shouldBeTwoEqualVectors() {
        assertEquals(threeConstructor, threeClone);
    }

    @Test
    @DisplayName("Should not be equal")
    public void shouldNotBeEqual() {
        assertNotEquals(twoConstructor, threeClone);
    }

    @Test
    @DisplayName("Plus operator test")
    public void plusOperatorTest() {
        Vector ans = threeConstructor.plus(twoConstructor);
        assertEquals(ans, new Vector(2.2, 4.4, 3.3));
    }

    @Test
    @DisplayName("Plus equal operator test")
    public void plusEqualOperatorTest() {
        Vector cpy = (Vector)threeConstructor.clone();
        assertEquals(cpy.plusEquals(twoConstructor), new Vector(2.2, 4.4, 3.3));
    }

    @Test
    @DisplayName("Unary minus operator test")
    public void unaryMinusOperatorTest() {
        assertEquals(threeConstructor.minus(), new Vector(-1.1, -2.2, -3.3));
    }

    @Test
    @DisplayName("Binary minus operator test")
    public void binaryMinusOperatorTest() {
        Vector ans = threeConstructor.minus(twoConstructor);
        assertEquals(ans, new Vector(0.0, 0.0, 3.3));
    }

    @Test
    @DisplayName("Binary minus equal operator test")
    public void binaryMinusEqualOperatorTest() {
        Vector cpy = (Vector)threeConstructor.clone();
        assertEquals(cpy.minusEquals(twoConstructor), new Vector(0.0, 0.0, 3.3));
    }

    @Test
    @DisplayName("Scalar multiplication test")
    public void scalarMultiplicationTest() {
        Vector ans = threeConstructor.scale(6.9);
        assertEquals(ans, new Vector(7.590000000000001, 15.180000000000001, 22.77));
    }

    @Test
    @DisplayName("Scalar equal multiplication test")
    public void scalarEqualMultiplicationTest() {
        Vector cpy = (Vector)threeConstructor.clone();
        assertEquals(cpy.scaleEquals(6.9),
                new Vector(7.590000000000001, 15.180000000000001, 22.77));
    }

    @Test
    @DisplayName("Set operator test")
    public void setOperatorTest() {
        Vector vec = new Vector();
        vec.set(threeConstructor);
        assertEquals(vec, new Vector(1.1, 2.2, 3.3));
    }

    @Test
    @DisplayName("Dot operator test")
    public void dotOperatorTest() {
        assertEquals(threeConstructor.dot(new Vector(3.3, 5.5, 7.7)), 41.14);
    }

    @Test
    @DisplayName("Cross operator test")
    public void crossOperatorTest() {
        Vector vec = new Vector(3.3, 5.5, 7.7);
        assertEquals(threeConstructor.cross(vec),
                new Vector(-1.2099999999999973, 2.419999999999998, -1.209999999999999));
    }

    @Test
    @DisplayName("Magnitude test")
    public void magnitudeTest() {
        assertEquals(threeConstructor.mag(), Math.sqrt(16.94));
    }

    @Test
    @DisplayName("Normalize test")
    public void normalizeTest() {
        double mag = threeConstructor.mag();
        assertEquals(threeConstructor.norm(), new Vector(1.1 / mag, 2.2 / mag, 3.3 /mag));
    }

    @Test
    @DisplayName("Setter method tests")
    public void setterMethodTests() {
        Vector cpy = (Vector)threeConstructor.clone();
        cpy.setX(3.3);
        cpy.setY(5.5);
        cpy.setZ(7.7);
        assertEquals(cpy, new Vector(3.3, 5.5, 7.7));
    }

}