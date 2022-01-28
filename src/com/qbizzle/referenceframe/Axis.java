/** @file
 * This file contains the Axis class which in itself contains the Direction enumerated type.
 */

package com.qbizzle.referenceframe;

import java.util.Objects;

/** The Axis class is used to represent an axis in a reference frame. A reference frame requires
 * three dimensions and therefore three axes to represent the direction of each dimension. The
 * Direction enum is used to distinguish the three axes from one another. Each axis can have an
 * alias to easily identify the meaning of the axis relative to an inertial frame e.g. 'PQW' in
 * the perifocal reference frame or 'SEZ' in the topographic reference frame.
 */
public class Axis implements Cloneable {

    /** Enumerated type to distinguish between the three main axis directions. */
    public enum Direction {
        X, Y, Z
    }

    /** Direction name of this axis in its reference frame. */
    private final Direction m_direction;
    /** An alias for this axis like one of 'IJK' or 'SEZ'. */
    private final char m_alias;
    /** A description of this axis, to keep track of what a direction signifies. */
    private final String m_description;

    /** Constructs an axis with only a direction.
     * @param dir Direction this axis represents in its reference frame.
     */
    public Axis(Direction dir) {
        this(dir, '\0', "");
    }

    /** Constructs an axis with an alias for its direction.
     * @param dir Direction this axis represents in its reference frame.
     * @param alias A character to alias this axis.
     */
    public Axis(Direction dir, char alias) {
        this(dir, alias, "");
    }

    /** Constructs an axis with a description of what the axis signifies or
     * any other notes worth containing in this instance.
     * @param dir Direction this axis represents in its reference frame.
     * @param desc Description to be contained in this instance.
     */
    public Axis(Direction dir, String desc) {
        this(dir, '\0', desc);
    }

    /** Constructs an axis with an alias for its direction and a description
     * of what the axis signifies or any other notes worth containing in this instance.
     * @param dir Direction this axis represents in its reference frame.
     * @param alias A character to alias this axis.
     * @param desc Description to be contained in this instance.
     */
    public Axis(Direction dir, char alias, String desc) {
        m_direction = dir;
        m_alias = alias;
        m_description = desc;
    }

    /// @name Overridden methods
    /// Method overrides from Object and Cloneable
//@{

    /** Converts the axis into a string representation.
     * @return A string describing the contents of this axis instance.
     */
    @Override
    public String toString() {
        return "Axis{" +
                "m_direction=" + m_direction +
                ", m_alias=" + m_alias +
                ", m_description='" + m_description + '\'' +
                '}';
    }

    /** Returns a copy of this axis.
     * @return An axis that is copied from this instance.
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /** Indicates whether this instance is equal to another axis.
     * @param o Axis object to compare this instance to.
     * @return True if the Direction and alias are equal, false if otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Axis axis = (Axis) o;
        return m_alias == axis.m_alias && m_direction == axis.m_direction;
    }

    /** Generates a hash code based on the direction and alias of this instance.
     * @return A hash code for this instance.
     */
    @Override
    public int hashCode() {
        return Objects.hash(m_direction, m_alias);
    }

//@}

    /// @name Getter methods
    /// Methods to retrieve values associated with this axis
///@{

    /** Gets the Direction associated with this axis.
     * @return The enumerated value of this axis' direction.
     */
    public Direction direction() {
        return m_direction;
    }

    /** Gets an alias of this axis if one was set when it was created.
     * @return The character that aliases this axis or the null character if none was set.
     */
    public char alias() {
        return m_alias;
    }

    /** Gets a description of this axis if one was set when it was created.
     * @return The string that describes this axis or an empty string if none was set.
     */
    public String description() {
        return m_description;
    }

///@}

}
