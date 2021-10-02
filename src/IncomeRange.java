/**
 * Author: Lyndon Foster.
 * Course: ITC313 - Programming in Java 2.
 * Assessment Title: Assessment Item 3, Task 1 - Tax Management Database Application
 * Date: October 16th, 2021.
 *
 * Data structure for the income range of the users tax calculations.
 */
public class IncomeRange {
    // Fields.
    private double lowerLimit;
    private double upperLimit;

    // Getter & setter methods.
    public double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(double upperLimit) {
        this.upperLimit = upperLimit;
    }

    public IncomeRange(double lowerLimit, double upperLimit) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    public IncomeRange() {
    }

    @Override
    public String toString() {
        return "IncomeRange{" +
                "lowerLimit=" + lowerLimit +
                ", upperLimit=" + upperLimit +
                '}';
    }

    // Override .hashcode() to correctly store the upper and lower limit values
    // when adding to the linkedhashmap.
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(lowerLimit);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(upperLimit);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    // Override .equals() method to compare static income range from
    // instances of this classes values correctly.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        IncomeRange other = (IncomeRange) obj;
        if (Double.doubleToLongBits(lowerLimit) != Double.doubleToLongBits(other.lowerLimit)){
            return false;
        }
        else {
            return Double.doubleToLongBits(upperLimit) == Double.doubleToLongBits(other.upperLimit);
        }
    }
}
