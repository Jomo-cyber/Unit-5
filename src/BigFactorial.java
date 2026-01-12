public class BigFactorial {
    public static void Main(String[] args) {
        factorial(5);
    }

    public static void factorial(int limit) {
        int pastNum = 0;
        int result = 1;


        for (int i = 0; i < limit; i++) {
            pastNum = i;
            for (int j = pastNum; j >0; j--) {
                result *= pastNum;
            }
            System.out.println(i + " " + result);
        }
    }
}

/*
Exercise 9.2 You might be sick of the factorial method by now, but we’re
going to do one more version.
1. Create a new program called Big.java and write an iterative version of
factorial (using a for loop).
2. Display a table of the integers from 0 to 30 along with their factorials.
At some point around 15, you will probably see that the answers are not
correct anymore. Why not?
3. Convert factorial so that it performs its calculation using BigIntegers
and returns a BigInteger as a result. You can leave the parameter
alone; it will still be an integer.
9.11 Exercises 163
4. Try displaying the table again with your modified factorial method. Is
it correct up to 30? How high can you make it go?
*/