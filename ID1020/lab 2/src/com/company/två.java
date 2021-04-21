package com.company;

import java.util.Scanner;

public class två
{
    public static class insertion
    {
        public static void sort(int[] a)
        {
            int len = a.length;
            for(int i = 0; i < len; ++i)
            {
                for(int j = i; j > 0; --j)
                {
                    if(less(a[j],a[j-1]))
                    {
                        exchange(a, j, j - 1);
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
        private static boolean less(int v, int w)
        {
            return v < w;
        }

        private static void exchange(int[] a, int i, int j)
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
        ett.insertion.sort(input);
    }
}
