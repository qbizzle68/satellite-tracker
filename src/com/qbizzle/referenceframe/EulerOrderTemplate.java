package com.qbizzle.referenceframe;

import java.util.Objects;

public class EulerOrderTemplate {
    public final Axis.Direction first_rotation;
    public final Axis.Direction second_rotation;
    public final Axis.Direction third_rotation;

    public EulerOrderTemplate(Axis.Direction first_axis, Axis.Direction second_axis, Axis.Direction third_axis) {
        first_rotation = first_axis;
        second_rotation = second_axis;
        third_rotation = third_axis;
    }

    @Override
    public String toString() {
        return "EulerOrder{" +
               "first_rotation=" + first_rotation +
               ", second_rotation=" + second_rotation +
               ", third_rotation=" + third_rotation +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EulerOrderTemplate that = (EulerOrderTemplate) o;
        return first_rotation == that.first_rotation && second_rotation == that.second_rotation && third_rotation == that.third_rotation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first_rotation, second_rotation, third_rotation);
    }
}
