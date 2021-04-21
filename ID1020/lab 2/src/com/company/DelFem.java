/*
@Author: Albin Jonsson, August Paulsrud, Elin Liu & Malin J.A Marques
written 15/09-2020

Tests merged sort and
 */

package com.company;
import java.util.*;
public class DelFem
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




        }
    }


    public static class mergesortBU
    {

        private static void merge(int[] a, int[] aux, int lo, int mid, int hi)
        {

            for (int k = lo; k <= hi; k++){
                aux[k] = a[k];
            }

            int i = lo, j = mid+1;
            for (int k = lo; k <= hi; k++){
                if(i > mid){
                    a[k] = aux[j++];
                }
                else if (j > hi) {
                    a[k] = aux[i++];
                }
                else if (less(aux[j], aux[i])){
                    a[k] = aux[j++];
                }
                else{
                    a[k] = aux[i++];
                }
            }
        }

        private static boolean less(int v, int w)
        {
            return v < w;
        }

        public static void sort(int[] a)
        {
            int N = a.length;
            int[] aux = new int[N];
            for (int sz = 1; sz < N; sz = sz + sz) {
                for (int lo = 0; lo < N - sz; lo += sz + sz) {
                    merge(a, aux, lo, lo + sz - 1, Math.min(lo + sz + sz - 1, N - 1));
                }
            }
        }

    }

    public static class mergesort
    {

        private static void merge(int[] a, int[] aux, int lo, int mid, int hi){

            for (int k = lo; k <= hi; k++){
                aux[k] = a[k];
            }

            int i = lo, j = mid+1;
            for (int k = lo; k <= hi; k++){
                if(i > mid){
                    a[k] = aux[j++];
                }
                else if (j > hi) {
                    a[k] = aux[i++];
                }
                else if (less(aux[j], aux[i])){
                    a[k] = aux[j++];
                }
                else{
                    a[k] = aux[i++];
                }
            }
        }

        private static boolean less(int v, int w)
        {
            return v < w;
        }

        private static void sort(int[] a, int[] aux, int lo, int hi)
        {
            if (hi <= lo) return;
            int mid = lo + (hi - lo) / 2;
            sort(a, aux, lo, mid);
            sort(a, aux, mid+1, hi);
            merge(a, aux, lo, mid, hi);
        }

        public static void sort(int[] a)
        {
            int[] aux = new int[a.length];
            sort(a, aux, 0, a.length - 1);
        }

    }

    public static class driver
    {

        public static void main(String[] args)          //The driver code
        {

            //antal test för varje längd
            int testTimes = 50;
            //
            int sortType = 0;
            //0 for merge, all else for insertion

            Scanner scanner = new Scanner(System.in);
            Random rand = new Random();

            System.out.println("How long of a array would you like to sort?");
            int arrayLength = scanner.nextInt();
            double totalMergeTime = 0;
            double totalInsertionTime = 0;
            for(int i = 0; i < testTimes; i++)
            {
                int[] arrayOfInts = new int[arrayLength];

                for(int j = 0; j < arrayLength; j++){
                    arrayOfInts[j] = rand.nextInt(10000);
                }
                int insertArr[] = new int[arrayLength];
                insertArr = arrayOfInts.clone();


               // System.out.println("Here is a array of random ints, ready to be sorted!");
                //printArray(arrayOfInts);

                //bara för ny rad
                //System.out.println("");

                //start measuring time
                long start = System.nanoTime();

                //the function is called

                    mergesort.sort(arrayOfInts);




                //calculating time elapsed
                long end = System.nanoTime();
                long elapsedTime = end - start;
                float elapsedTimeMilli = elapsedTime / 1000000;

                //System.out.println("Here is the same array, sorted");
                //printArray(arrayOfInts);

                //bara för ny rad
                //System.out.println("");

                //printing the time elapsed
                System.out.println("merged sort took " + elapsedTime);
                totalMergeTime += elapsedTime;

                long insertStart = System.nanoTime();

                insertion.sort(insertArr);
                long insertEnd = System.nanoTime();
                long insertElapsedTime = insertEnd - insertStart;
                System.out.println("insertion sort took " + insertElapsedTime);

                totalInsertionTime += insertElapsedTime;

            }
            totalMergeTime /= testTimes;
            totalInsertionTime /= testTimes;

            System.out.println("The average time for merge sort was: " + totalMergeTime + " nanoseconds.");
            System.out.println("The average time for insertion sort was: " + totalInsertionTime + " nanoseconds.");

        }

        private static void printArray(int[] array)
        {
            int length = array.length;

            for(int j = 0; j < length; j++){
                System.out.print(array[j] + " ");
            }

        }

    }
}
