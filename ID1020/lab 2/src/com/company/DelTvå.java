/*
@Author: Albin Jonsson & Malin J.A Marques
created 13/09-2020

Sort an array using insertion sort and count the swaps made in the program.
 */

package com.company;
import java.util.*;

public class DelTvå
{

    public static class insertion
    {


        public static void sort(int[] a)        //sorterar alla element
        {
            int count = 0;
            int len = a.length;
            for(int i = 0; i < len; ++i)
            {
                for(int j = i; j > 0; --j)
                {
                    if(less(a[j],a[j-1]))
                    {
                        exchange(a, j, j - 1);
                        ++count;
                    }
                    else
                    {
                        break;
                    }
                }

            }
            System.out.println("The elements were swapped " + count + " times");
        }
        private static boolean less(int v, int w)       //Jämför två tal för att kolla om ett är mindre än det andra
        {
            return v < w;
        }

        private static void exchange(int[] a, int i, int j) //ändrar plats på talen och skriver ut den nya arrayen
        {
            int swap = a[i];
            a[i] = a[j];
            a[j] = swap;

            int length = a.length;
            System.out.println("the new array looks like:");

            for(int k = 0; k < length; ++k)
                System.out.print(a[k] + " ");
            System.out.println();

        }
    }

    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("How many numbers do you want to sort?");
        int length = scan.nextInt();
        int[] input = new int[length];

        System.out.println("Please enter the numbers you want sorted");
        for(int i = 0; i < length; ++i)
            input[i] = scan.nextInt();
        insertion.sort(input);
    }

}
