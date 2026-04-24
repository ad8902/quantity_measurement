public class uc1 {

    // Inner class to represent Feet measurement
    static class Feet {
        private final double value;

        // Constructor
        public Feet(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        // Override equals method
        @Override
        public boolean equals(Object obj) {

            // Same reference check (Reflexive)
            if (this == obj) {
                return true;
            }

            // Null and type check
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            // Type casting
            Feet other = (Feet) obj;

            // Compare double values safely
            return Double.compare(this.value, other.value) == 0;
        }
    }

    // Main method for demonstration
    public static void main(String[] args) {

        Feet value1 = new Feet(1.0);
        Feet value2 = new Feet(1.0);

        boolean result = value1.equals(value2);

        System.out.println("Are values equal? " + result);
    }
}