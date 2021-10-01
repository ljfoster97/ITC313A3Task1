public class IncomeRange {
    private double lowerLimit;
    private double upperLimit;
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
