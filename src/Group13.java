import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.List;

public class Group13
{

    // To compile:
    // javac Group13.java
    // To run use terminal with form:
    // java Group13 ../data0.txt ../out0.txt

    public static void main(String[] args) throws InterruptedException, FileNotFoundException,IOException {

        // testing the comparator:
        Data.testM_LRMUS();

        if (args.length < 2) {
            System.out.println("Please run with two command line arguments: input and output file names");
            System.exit(0);
        }

        String inputFileName = args[0];
        String outFileName = args[1];

        // read as strings
        //System.out.print("Importing data from "+inputFileName+"...");
        String [] data = readData(inputFileName);
        //System.out.println("done");
        //System.out.println("First is ->"+data[0]+"<-");
        //      printArray(data, 10);

        String [] toSort = data.clone();

        //System.out.println("Beginning sort...");

        // ! Data [] sorted = sort(toSort);
        Data [] sorted = quicksort(toSort, 0, toSort.length);

        //System.out.println("done");

        toSort = data.clone();

        Thread.sleep(10); //to let other things finish before timing; adds stability of runs

        long start = System.currentTimeMillis();

        // ! sorted = sort(toSort);


        long end = System.currentTimeMillis();

        System.out.println(end - start + " ms");
        //System.out.print("\tExporting sorted data to "+outFileName+"...");

        //! writeOutResult(sorted, outFileName);


        //System.out.println("done!");

    }

    // YOUR SORTING METHOD GOES HERE.
    // You may call other methods and use other classes.
    // Note: you may change the return type of the method.
    // You would need to provide your own function that prints your sorted array to
    // a file in the exact same format that my program outputs

    public static void quicksort(TestInteger[] quickArray, int start, int end){
        if (start < end){
            int q = partition(quickArray, start, end);
            quicksort(quickArray, start, q-1);
            quicksort(quickArray, q+1, end);
        }
    }

    public static int partition(TestInteger[] partArray, int start, int end){
        TestInteger x = partArray[end];
        int i = start - 1;

        for (int count = start; count <= end - 1; count++){
            if (partArray[count].compareTo(x) <= 0){
                i++;
                exchange(i, count, partArray);
            }
        }
        exchange(i+1, end, partArray);

        return i+1;
    }

    public static void exchange(int arrayPositionOne, int arrayPositionTwo, TestInteger[] Array){
        TestInteger temp = Array[arrayPositionOne];
        Array[arrayPositionOne] = Array[arrayPositionTwo];
        Array[arrayPositionTwo] = temp;
    }
/*
    private static Data[] sort(String[] toSort) {
        Data[] toSortData = new Data[toSort.length];
        //System.out.print("\tBeginning Initialization...");
        for (int i = 0; i < toSort.length; ++i) {
            toSortData[i] = new Data(toSort[i]);
        }
        //System.out.println("done!");
        Arrays.sort(toSortData, new M_LRMUSComparator());
        return toSortData;
    }
*/
    private static void printArray(String[] Arr, int n) {
        for(int i = 0; i < n; i++) {
            System.out.println(Arr[i]);
        }
    }

    private static String[] readData(String inFile) throws FileNotFoundException,IOException {
        //ArrayList<String> input = new ArrayList<>();

        //List<String> input = FileUtils.readLines(new File(inFile));
        List<String> input = Files.readAllLines(Paths.get(inFile));


                /*Scanner in = new Scanner(new File(inFile));

                while(in.hasNext()) {
                        input.add(in.next());
                }

                in.close();
                */
        // the string array is passed just so that the correct type can be created
        return input.toArray(new String[0]);
    }

    private static void writeOutResult(Data[] sorted, String outputFilename) throws FileNotFoundException {

        PrintWriter out = new PrintWriter(outputFilename);
        for (Data s : sorted) {
            out.println(s.value());
        }
        out.close();

    }

    private static class M_LRMUSComparator implements Comparator<Data> {

        @Override
        public int compare(Data s1, Data s2) {

            /* Length test */
            if(s1.M_LRMUSLength() < s2.M_LRMUSLength()){return -1;}
            if(s1.M_LRMUSLength() > s2.M_LRMUSLength()){return 1;}

            /* Position test*/
            if(s1.M_LRMUSPosition() < s2.M_LRMUSPosition()){return -1;}
            if(s1.M_LRMUSPosition() > s2.M_LRMUSPosition()){return 1;}

            /* Alphabetical test */
            int tmp = s1.M_LRMUSStr().compareTo(s2.M_LRMUSStr()); // NOTE:  This typically returns values outside the set {-1,0,1}, but the sign still determines ordering
            if(tmp!=0){return(tmp);}

            /* Fallback */
            return(s1.value().compareTo(s2.value())); //This too.
        }
    }

    private static class Data {
        private class LRMUS{
            public int position=Integer.MAX_VALUE; // Initial Position is as bad as possible
            public int length=Integer.MIN_VALUE;                   // Initial Length is as bad as possible
            public String referenceStr;

            public LRMUS(String str){
                referenceStr=str; // REFERENCE to original string
            }

            public void updateAt(int start){ //0-based position.  Will update LRMUS if there is an improvement at start
                int n = referenceStr.length();
                for(int end=start; end<n;end++){
                    int matchCnt=0;
                    String toMatch=referenceStr.substring(start,end); //indices are 0-based so str.length is 1 more than the final index

                    int m=toMatch.length();
                    if (m<length){ // If the string to match is shorter than our current best then we should skip to the next iteration
                        continue;
                        //break; // If the string to match is shorter than our current best length there's no reason to continue
                    }
                    int testEnd=referenceStr.length()-m;
                    for(int testStart=0;testStart<=testEnd;testStart++){
                        String test=referenceStr.substring(testStart,testStart+m); // For each testable location extract the substring that's the same size as toMatch
                        if(toMatch.compareTo(test)==0){matchCnt++;}
                    }
                    if(matchCnt==0){System.out.println("DANGER!  No Match in updateAt!  This should never happen");}
                    if(matchCnt==1){ // In this case the substring is unique.  If we are here than the match is at least as good as the previous best
                        if(m==this.length && start < this.position){// If it's a tie then we check position
                            this.position=start;
                        } else if(m>this.length) {
                            this.length=m;
                            this.position=start; //
                        }
                        // NOTE:  If the length is the same AND the start >= the new position then there's nothing to update

                        break; // We can't do any better since $m$ is decreasing... our m<length sentinel would capture this... but why waste time
                    }
                    // If we are here then we haven't found a unique match -- we need to extend the length of our string and try again.
                }
            }
            public void findBest(){
                for(int i =0; i< referenceStr.length();i++){
                    updateAt(i); //
                }
            }
            public String getLRMUS() {
                if(referenceStr==null) {
                    System.out.println("ReferenceStr is uninitialized");
                    return "";
                }
                if(length<0) {
                    System.out.println("No LRMUS was found");
                    System.out.println("Returning full string ->"+referenceStr+"<-");
                    return referenceStr;
                }
                if(position<0 || position+length > referenceStr.length()) {
                    System.out.println("Error:  Bad starting position " + position);
                    System.out.println("Returning full string ->"+referenceStr+"<-");
                    return referenceStr;
                }
                return referenceStr.substring(position,position+length);
            }
            public void report() {
                System.out.println("LRMUS for "+referenceStr);
                System.out.println("\tLRMUS length: " + length);
                System.out.println("\tLRMUS Position " + position);
                System.out.println("\tLRMUS "+getLRMUS());
            }
        }

        private LRMUS best=null; // Easy way to indicate variables are uninitialized.
        private String string=null;

        public int M_LRMUSLength(){return best.length;}
        public int M_LRMUSPosition(){return best.position;}
        public String M_LRMUSStr() {return best.getLRMUS();}
        public String value(){return string;}
                /*public void findBestLRMUS() {
                        best = new LRMUS(string);
                        best.findBest(); // Updates best so it contains the best LRMUS
                }*/

        public Data(String str) {
            string = new String(str);
            best = new LRMUS(string);
            best.findBest(); // Updates best so it contains the best LRMUS
        }

        public static void testM_LRMUS() {

            Data testItem,testItem2;
            M_LRMUSComparator comparator=new M_LRMUSComparator();

            testItem=new Data("Morrocco");
            testItem.best.report();

            testItem=new Data("Morrorrcco");
            testItem.best.report();

            testItem=new Data("abcdefghijklm");
            testItem.best.report();

            testItem=new Data("abcdefghdijklm");
            testItem.best.report();

            testItem=new Data("atatat");
            testItem.best.report();

            System.out.println("---");

            testItem=new Data("Zunzibar");
            testItem2=new Data("rorocco");
            System.out.println(comparator.compare(testItem,testItem2));
            System.out.println(comparator.compare(testItem2,testItem2));
            System.out.println(comparator.compare(testItem2,testItem));
            testItem.best.report();
            testItem2.best.report();

            System.out.println("---");

            testItem=new Data("**cat**");
            testItem2=new Data("***cat**");
            System.out.println(comparator.compare(testItem,testItem2));
            System.out.println(comparator.compare(testItem2,testItem2));
            System.out.println(comparator.compare(testItem2,testItem));
            testItem.best.report();
            testItem2.best.report();


            System.out.println("---");
        }

    }

}