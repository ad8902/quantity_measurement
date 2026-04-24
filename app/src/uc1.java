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
    }

    // ===== VALUE OBJECT =====
    static class QuantityLength {

        private final double value;
        private final LengthUnit unit;
        private static final double EPSILON = 1e-6;

        public QuantityLength(double value, LengthUnit unit) {
            validate(value, unit);
            this.value = value;
            this.unit = unit;
        }

        // ===== Validation =====
        private static void validate(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Value must be finite");
            }
        }

        public double getValue() {
            return value;
        }

        public LengthUnit getUnit() {
            return unit;
        }

        // ===== Base Conversion =====
        private double toBaseUnit() {
            return unit.toFeet(value);
        }

        // ===== UC5: Conversion =====
        public QuantityLength convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            double base = toBaseUnit();
            double converted = targetUnit.fromFeet(base);

            return new QuantityLength(converted, targetUnit);
        }

        public static double convert(double value, LengthUnit source, LengthUnit target) {
            validate(value, source);
            if (target == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            double base = source.toFeet(value);
            return target.fromFeet(base);
        }

        // ===== PRIVATE UTILITY (UC7 KEY IMPROVEMENT) =====
        private static double addInBase(QuantityLength q1, QuantityLength q2) {
            return q1.toBaseUnit() + q2.toBaseUnit();
        }

        // ===== UC6: Addition (result in first operand unit) =====
        public QuantityLength add(QuantityLength other) {
            if (other == null) {
                throw new IllegalArgumentException("Other quantity cannot be null");
            }

            double sumFeet = addInBase(this, other);
            double result = this.unit.fromFeet(sumFeet);

            return new QuantityLength(result, this.unit);
        }

        // ===== UC7: Addition with Explicit Target Unit =====
        public static QuantityLength add(QuantityLength q1,
                                         QuantityLength q2,
                                         LengthUnit targetUnit) {

            if (q1 == null || q2 == null) {
                throw new IllegalArgumentException("Operands cannot be null");
            }
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            double sumFeet = addInBase(q1, q2);
            double result = targetUnit.fromFeet(sumFeet);

            return new QuantityLength(result, targetUnit);
        }

        // ===== Equality =====
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

    // ===== DEMO =====
    public static void main(String[] args) {

        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCH);

        // UC6
        System.out.println("UC6: " + a.add(b)); // 2 FEET

        // UC7
        System.out.println("To FEET: " +
                QuantityLength.add(a, b, LengthUnit.FEET));

        System.out.println("To INCHES: " +
                QuantityLength.add(a, b, LengthUnit.INCH));

        System.out.println("To YARDS: " +
                QuantityLength.add(a, b, LengthUnit.YARD));

        // Example
        QuantityLength c = new QuantityLength(2.54, LengthUnit.CENTIMETER);
        QuantityLength d = new QuantityLength(1.0, LengthUnit.INCH);

        System.out.println("CM result: " +
                QuantityLength.add(c, d, LengthUnit.CENTIMETER));
    }
}