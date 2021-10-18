package devjluvisi.mlb.util;

/**
 * Represents a numerical range between two numbers.
 *
 * @author jacob
 */
public record Range(double min, double max) {

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    /**
     * Check if a value is in range of a specified min and max. This method is
     * inclusive.
     *
     * @param val The integer to check.
     * @return If the integer is in range of min and max.
     */
    public boolean isInRange(double val) {
        return (val >= this.min) && (val <= this.max);
    }

    /**
     * If the value is in range of the specified range max and min.
     *
     * @param val The value to check
     * @param b   If the method should be inclusive or exclusive. (Inclusive = true,
     *            Exclusive = false)
     * @return If the value is in range for the specified parameters.
     */
    public boolean isInRange(double val, boolean b) {
        return b ? (val >= this.min) && (val <= this.max) : (val > this.min) && (val < this.max);
    }

}
