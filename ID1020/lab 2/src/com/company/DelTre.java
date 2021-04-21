import java.util.Scanner;

//Author Albin Jonsson & Malin J.A. Marques
//File created and last edited 13-09-2020.
//The program sorts an array using the algorithm type insertionsort, prints the result along with the number of swaps as well as counts the number of inversions in the input array before the array is sorted.
//The method sort and part of exch are borrowed from Princetons Algorithms 4th edition with modifications so that the function works only for integers.

public class DelTre {
    public static int count=0;
    public static void main (String[] args)     //driver code
    {
        while (true){
            System.out.println("how many numbers?");                        //asks for length of int arrray from user
            int choice = new Scanner(System.in).nextInt();                  //user response in
            count=0;                                                        //count resets for every new iteration

            if (choice >=0) {                                               //checks for validity of users choice


                int[] intry = new int[choice];                              //creates array of requested length
                System.out.println("write your numbers:");                  //requests numbers for array

                for (int i = 0; i < choice; i++) {                          //inserts numbers into array using loop
                    Scanner in = new Scanner(System.in);
                    intry[i] = in.nextInt();
                }

                swaps(intry);

                sort(intry);                                                //sorts numbers in array
                System.out.println("\nfinal array:");                       //prints out the final organised version of the array
                for (int i = 0; i < choice; i++) {
                    System.out.print(intry[i] + " ");
                }
                System.out.println("\nnumber of swaps:" + count);           // tells user how many swaps were performed

            }

            System.out.println("\nchoose something else");                    //lets user know to start a new array
        }
    }
    public static void sort(int[] a)                                    //method to sort array
    {
        int N = a.length;                                               //integer N is started to denote the length of a
        for (int i = 0; i < N; i++)                                     //first for lop circles through array a
            for (int j = i; j > 0; j--)                                 //second loop sorts through a in the other direction
                if (less(a[j], a[j-1]))                                 //checks if the chosen element j is less than element j-1
                    exch(a, j, j-1);                                 // if that is the case echanges spots in the list for j and j-1
                else break;                                             //breaks inner loop
    }
    private static boolean less(int v, int w)
    {
        return v < w;                                                   //returns 1 if element v is less than element w
    }

    private static void exch(int[] a, int i, int j)                     //exchanges the numbers
    {   count++;                                                        //increments the counter for each swap
        int extr= a[i];                                                 //copies element from i to an extra int
        a[i]=a[j];                                                      //copies j element to element i
        a[j]=extr;                                                      //fills element j with element i (now stored in extr)
        System.out.println("\nnew array:");                             //introduces  new array
        for(int k=0; k < a.length; k++) {
            System.out.print(a[k] + " ");                                     //prints out every letter
        }

    }
    public static void swaps(int[] a)                           //counts the inversions
    {
        String swapstr = "";
        for (int i = 0; i < a.length; i++){                               //first for lop circles through array a
            for (int j = i + 1; j < a.length; j++)                        //second loop sorts through a in the other direction
            {
                if (!less(a[i], a[j]) && a[i] != a[j])                                    //checks if the chosen element j is less than element j-1
                {
                    count++;                                              // if that is the case echanges spots in the list for j and j-1
                    if (count == 1)
                        swapstr = "[" + i + "," + a[i] + "] [" + j + ", " + a[j] + "]";
                    else {
                        swapstr += ", [" + i + "," + a[i] + "] [" + j + ", " + a[j] + "]";
                    }
                }
            }
        }

        System.out.println( "invertions: " + swapstr);                  //the invertions are printed out
        System.out.println("number of invertions:" + count);            //the number of invertions are printed out
        count = 0;                                                      //count is set to zero so that it can be used to count the number of swaps
    }
}