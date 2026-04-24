public class uc1 {

    // ===== ENUM: Length Units =====
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

    // ===== VALUE OBJECT: QuantityLength =====
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

        // ===== UC6: Addition =====
        public QuantityLength add(QuantityLength other) {
            if (other == null) {
                throw new IllegalArgumentException("Other quantity cannot be null");
            }

            double sumFeet = this.toBaseUnit() + other.toBaseUnit();
            double result = this.unit.fromFeet(sumFeet);

            return new QuantityLength(result, this.unit);
        }

        public static QuantityLength add(QuantityLength q1,
                                         QuantityLength q2,
                                         LengthUnit targetUnit) {

            if (q1 == null || q2 == null) {
                throw new IllegalArgumentException("Operands cannot be null");
            }
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            double sumFeet = q1.toBaseUnit() + q2.toBaseUnit();
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

    // ===== DEMO METHODS =====

    public static void demonstrateConversion(double value,
                                             LengthUnit from,
                                             LengthUnit to) {

        double result = QuantityLength.convert(value, from, to);
        System.out.println(value + " " + from + " = " + result + " " + to);
    }

    public static void demonstrateAddition(QuantityLength q1,
                                           QuantityLength q2) {

        QuantityLength result = q1.add(q2);
        System.out.println(q1 + " + " + q2 + " = " + result);
    }

    public static void demonstrateEquality(QuantityLength q1,
                                           QuantityLength q2) {

        System.out.println(q1 + " == " + q2 + " : " + q1.equals(q2));
    }

    // ===== MAIN METHOD =====
    public static void main(String[] args) {

        // ===== UC5: Conversion =====
        demonstrateConversion(1.0, LengthUnit.FEET, LengthUnit.INCH);
        demonstrateConversion(3.0, LengthUnit.YARD, LengthUnit.FEET);
        demonstrateConversion(2.54, LengthUnit.CENTIMETER, LengthUnit.INCH);

        // ===== UC6: Addition =====
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);
        demonstrateAddition(q1, q2); // 2 FEET

        QuantityLength q3 = new QuantityLength(1.0, LengthUnit.YARD);
        QuantityLength q4 = new QuantityLength(3.0, LengthUnit.FEET);
        demonstrateAddition(q3, q4); // 2 YARD

        // ===== Equality =====
        demonstrateEquality(
                new QuantityLength(1.0, LengthUnit.YARD),
                new QuantityLength(36.0, LengthUnit.INCH)
        );

        demonstrateEquality(
                new QuantityLength(1.0, LengthUnit.CENTIMETER),
                new QuantityLength(0.393701, LengthUnit.INCH)
        );
    }
}