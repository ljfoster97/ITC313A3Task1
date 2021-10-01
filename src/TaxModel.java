public class TaxModel {
    private double centsPerDollar;
    private double baseTax;
    private double overLimit;
    private double perAmount;

    public double getCentsPerDollar() {
        return centsPerDollar;
    }

    public void setCentsPerDollar(double centsPerDollar) {
        this.centsPerDollar = centsPerDollar;
    }

    public double getBaseTax() {
        return baseTax;
    }

    public void setBaseTax(double baseTax) {
        this.baseTax = baseTax;
    }

    public double getOverLimit() {
        return overLimit;
    }

    public void setOverLimit(double overLimit) {
        this.overLimit = overLimit;
    }

    public TaxModel(double centsPerDollar, double baseTax, double overLimit, double perAmount) {
        this.centsPerDollar = centsPerDollar;
        this.baseTax = baseTax;
        this.overLimit = overLimit;
        this.perAmount = perAmount;
    }

    @Override
    public String toString() {
        return "TaxModel{" +
                "centsPerDollar=" + centsPerDollar +
                ", baseTax=" + baseTax +
                ", overLimit=" + overLimit +
                ", perAmount=" + perAmount +
                '}';
    }

    public TaxModel() {
    }
}