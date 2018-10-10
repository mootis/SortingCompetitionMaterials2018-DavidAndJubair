public class TestInteger implements Comparable<TestInteger>{
    public int value;
    static long counter;

    TestInteger(int givenValue){
        value = givenValue;
    }

    public int compareTo(TestInteger comparingTo) {
        counter++;

        if (this.value < comparingTo.value) {
            return -1;
        } else if (this.value == comparingTo.value) {
            return 0;
        } else {
            return 1;
        }
    }
}