package devjluvisi.mlb.util;

/**
 * Represents a numerical range between two numbers.
 *
 * @author jacob
 *
 */
public class Range {

	private final int max;
	private final int min;

	/**
	 * Defines a range between a set of two integers min and max.
	 *
	 * @param min The minimum number in the range.
	 * @param max The maximum number in the range.
	 */
	public Range(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public int getMax() {
		return this.max;
	}

	public int getMin() {
		return this.min;
	}

	/**
	 * Check if a value is in range of a specified min and max. This method is
	 * inclusive.
	 *
	 * @param val The integer to check.
	 * @return If the integer is in range of min and max.
	 */
	public boolean isInRange(int val) {
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
	public boolean isInRange(int val, boolean b) {
		return b ? (val >= this.min) && (val <= this.max) : (val > this.min) && (val < this.max);
	}

}
