public class uc1 {

    // ===== ENUM =====
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0),
        YARD(3.0),
        CENTIMETER(0.0328084);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }

        public double fromFeet(double feetValue) {
            return feetValue / toFeetFactor;
        }

        public double getFactor() {
            return toFeetFactor;
        }
    }

    // ===== Quantity Class =====
    static class QuantityLength {

        private final double value;
        private final LengthUnit unit;
        private static final double EPSILON = 1e-6;

        public QuantityLength(double value, LengthUnit unit) {
            validate(value, unit);
            this.value = value;
            this.unit = unit;
        }

        private static void validate(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Value must be finite");
            }
        }

        private double toBaseUnit() {
            return unit.toFeet(value);
        }

        // ===== Instance Conversion =====
        public QuantityLength convertTo(LengthUnit targetUnit) {
            validate(this.value, targetUnit);

            double base = toBaseUnit();
            double converted = targetUnit.fromFeet(base);

            return new QuantityLength(converted, targetUnit);
        }

        // ===== Static Conversion API =====
        public static double convert(double value, LengthUnit source, LengthUnit target) {
            validate(value, source);
            validate(value, target);

            double base = source.toFeet(value);
            return target.fromFeet(base);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            QuantityLength other = (QuantityLength) obj;

            return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < EPSILON;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(toBaseUnit());
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // ===== Demonstration Methods (Overloaded) =====

    public static void demonstrateLengthConversion(double value,
                                                   LengthUnit from,
                                                   LengthUnit to) {

        double result = QuantityLength.convert(value, from, to);
        System.out.println(value + " " + from + " = " + result + " " + to);
    }

    public static void demonstrateLengthConversion(QuantityLength quantity,
                                                   LengthUnit to) {

        QuantityLength converted = quantity.convertTo(to);
        System.out.println(quantity + " = " + converted);
    }

    public static void demonstrateLengthEquality(QuantityLength q1,
                                                 QuantityLength q2) {

        System.out.println(q1 + " == " + q2 + " : " + q1.equals(q2));
    }

    public static void main(String[] args) {

        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH);
        demonstrateLengthConversion(3.0, LengthUnit.YARD, LengthUnit.FEET);
        demonstrateLengthConversion(36.0, LengthUnit.INCH, LengthUnit.YARD);

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.CENTIMETER);
        demonstrateLengthConversion(q1, LengthUnit.INCH);

        demonstrateLengthEquality(
                new QuantityLength(1.0, LengthUnit.YARD),
                new QuantityLength(36.0, LengthUnit.INCH)
        );
    }
}