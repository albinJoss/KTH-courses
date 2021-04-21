package com.company;
import java.util.*;
public class DelSex
{
    //Skriven av Albin Jonsson & Malin J.A. Marques
//Fil skapad och senast redigerad 17-09-2020
//Filen sorterar en integer array med hjälp av mergesort och insertionssort.
//Insertionsort sorterar de arrayna som skapas då mergesort har delat upp original arrayn till olika delar av längden cutoff.
//Filen sorterar även arrayn med cutoff points 1 till 30 (men inte 0 då det inte går att sortera en array utan element).
//Methoderna merge, båda sorts, exch och insertsort är lånade från Princetons Algorithms 4th edition, men har blivit justerade för att fungera med varandra och endast sortera integers.

    public static class insertionMergesort {

        private static void merge(int[] a, int[] aux, int lo, int mid, int hi) {             //metod för att mergea bitarna av arrayn, a= , aux= , lo=längst tal, mid=mitt tal , hi=högst tal

            for (int k = lo; k <= hi; k++) {                                                 //cyklar igenom hela arayn
                aux[k] = a[k];                                                               //lägger in allt i a in i array aux
            }

            int i = lo, j = mid + 1;                                                         //i och j skapas och sätts till lo repsektive mid+1
            for (int k = lo; k <= hi; k++)                                                   //cyklar igenom arrayn från lo till hi
            {
                if (i > mid)                                                                 //om i=mid och därmed lo=mid sätts ak till aux[j++]
                {
                    a[k] = aux[j++];                                                         //alltså sätts ak till mid +2
                } else if (j > hi)                                                           //annars kollas om j> hi
                {
                    a[k] = aux[i++];                                                         //om det stämmer sätts ak till aux[i++] (aka lo+1]
                } else if (less(aux[j], aux[i]))                                             //annars kolla om aux[j] < aux[i]
                {
                    a[k] = aux[j++];                                                         //om det e fallet
                } else {                                                                     //annars sätts ak till auxi++ (aka lo++)
                    a[k] = aux[i++];
                }
            }
        }

        private static boolean less(int v, int w)                                           //kollar om v < w
        {

            return v < w;
        }

        private static void sort(int[] a, int[] aux, int lo, int hi, int cutoff)        //Gör merge sort
        {

            if (hi <= (lo + cutoff - 1)) {                                                  // cutoff -1 används så att insertsort kallas om lo=hi (vid 1 element)
                insertsort(a, lo, hi);                                                      //kallar insertionsort metod
            } else {
                int mid = lo + (hi - lo) / 2;                                               //mid sätts till medelvärda av lo och hi, om k=0 kommer denhär sorten kalla sig själv oändligt
                sort(a, aux, lo, mid, cutoff);                                              //kallas sort med "vanliga" a, aux, lo, mid
                sort(a, aux, mid + 1, hi, cutoff);                                       //kalla sort igen nu med mid1
                merge(a, aux, lo, mid, hi);                                                 //kalla merge med "vanliga" a, aux, lo, mid, hi
            }
        }

        public static void sort(int[] a, int cutoff)                //Tillkallar sort funktionen
        {
            int[] aux = new int[a.length];                                                  //skapa ny arrau aux av samma längd som a
            sort(a, aux, 0, a.length - 1, cutoff);                                   //kallar sort igen med ny arrayn aux, lo=0, och hi=as längd-1
        }

        public static void insertsort(int[] a, int lo, int hi)                               //method to insertionsort array
        {

            for (int i = lo; i < hi; i++)                                     //first for lop circles through array a
                for (int j = i; j > lo; j--)                                  //second loop sorts through a in the other direction
                    if (less(a[j], a[j - 1]))                                 //checks if the chosen element j is less than element j-1
                        exch(a, j, j - 1);                                 // if that is the case echanges spots in the list for j and j-1
                    else break;                                               //breaks inner loop
        }

        private static void exch(int[] a, int i, int j)                 //Exchanges the values in an array
        {
            int extr = a[i];                                                 //copies element from i to an extra int
            a[i] = a[j];                                                      //copies j element to element i
            a[j] = extr;                                                      //fills element j with element i (now stored in extr)
        }
    }

    public static void main(String[] args)          //Driver code
    {


        int testTimes = 100;                                                             //antal gånger som testet utförs

        Random rand = new Random();                                                      //en slumpad sekvens av nummer

        System.out.println("Please enter the length of the array");                      //fråga för användaren
        Scanner scnr = new Scanner(System.in);                                           //scanner öppnas för användarsvar
        int len = scnr.nextInt();                                                        //scnr görs om till in

        int[] average= new int[30];                                                      //skapar array för averages

        for(int i = 0; i < testTimes; i++){                                              //testar ett visst antal gånger
            int[] intarray = new int[len];                                               //array skapas mer längedn som angivits

            for(int j = 0; j < len; j++){                                                //cyklar igenom arrayn
                intarray[j] = rand.nextInt(10000);                                //fyller arrayn med random nummerena
            }

            for(int k=1;k <= 30;k++ ) {                                                  //nummeret indikerar vart cutoffen är

                int[] copyarray = intarray.clone();                                      //skapar kopia av arrayn

                long start = System.nanoTime();                                          //börjar tidtagning start=kl då process börjat
                insertionMergesort.sort(copyarray, k);                                   //kallar insertionmerges sort funktion på kopian så att den inte är sorterad efter första iterationen


                long end = System.nanoTime();                                            //avslutar tidtagning end=kl då process slutat
                long timepassed = end - start;                                           //beräknar skillnaden mellan start klock start och slut klockslag


                average[k-1] +=timepassed/testTimes;

                System.out.println("Sorting took " + timepassed + " nanoseconds! With a cutoff point of "+ k); //skriver ut tiden som passerat
            }

        }
        System.out.println("Average for cutoff points: ");
        for(int i=0;i<average.length;i++){
            System.out.println((i + 1) + "    " + average[i]); }                                                                      //skriver ut alla medelvärden för alla cutoff points

    }
}
