public class uc1 {

    public static void main(String[] args) {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);

        // Equality
        System.out.println("Equality: " + q1.equals(q2)); // true

        // Conversion
        System.out.println("Convert 1 ft to inches: " + q1.convertTo(LengthUnit.INCHES));

        // Addition (default = first operand unit)
        System.out.println("Add (default unit): " + q1.add(q2));

        // Addition (explicit target unit)
        System.out.println("Add (yards): " + q1.add(q2, LengthUnit.YARDS));

        // Static conversion
        System.out.println("Static convert: " +
                QuantityLength.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES));
    }
}

/**
 * Standalone enum (UC8)
 * Responsible for all conversion logic
 */
enum LengthUnit {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor) {
        this.toFeetFactor = toFeetFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * toFeetFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toFeetFactor;
    }
}

/**
 * QuantityLength class (UC3 → UC8)
 */
final class QuantityLength {

    private final double value;
    private final LengthUnit unit;
    private static final double EPSILON = 1e-6;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid value");

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    /**
     * Convert this quantity to another unit
     */
    public QuantityLength convertTo(LengthUnit targetUnit) {
        validateUnit(targetUnit);

        double base = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(base);

        return new QuantityLength(converted, targetUnit);
    }

    /**
     * Static conversion API (UC5)
     */
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid value");
        if (source == null || target == null)
            throw new IllegalArgumentException("Units cannot be null");

        double base = source.convertToBaseUnit(value);
        return target.convertFromBaseUnit(base);
    }

    /**
     * Addition (UC6 - default unit = this.unit)
     */
    public QuantityLength add(QuantityLength other) {
        return add(other, this.unit);
    }

    /**
     * Addition with explicit target unit (UC7)
     */
    public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
        if (other == null)
            throw new IllegalArgumentException("Other quantity cannot be null");
        validateUnit(targetUnit);

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        double sumBase = base1 + base2;

        double result = targetUnit.convertFromBaseUnit(sumBase);

        return new QuantityLength(result, targetUnit);
    }

    private void validateUnit(LengthUnit unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");
    }

    /**
     * Equality (UC1 → UC4)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        QuantityLength other = (QuantityLength) obj;

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        return Math.abs(base1 - base2) < EPSILON;
    }

    @Override
    public String toString() {
        return String.format("Quantity(%.6f, %s)", value, unit);
    }
}