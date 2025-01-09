public class Tuple<Key, Value> {
        private final Key name;
        private final Value value;

        // Creates a new Tuple
        public Tuple(Key name, Value value) {
            this.name = name;
            this.value =value;
        }

        // Returns the name (key) of the tuple
        public Key name() {
            return name;
        }

        // Returns the value of the tuple
        public Value value() {
            return value;
        }

        // Returns a string representation of the Tuple. For a <String, Float> classification
        // object this could be, (Dog,0.55)
        public String toString() {
            return "("+ name + ", " + value + ")";
        }



}