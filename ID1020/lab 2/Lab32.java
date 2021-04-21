import java.util.*;
//Written by Malin J. A. Marques & Albin Jonsson
//File created 21-09-2020, edited 24-09-2020
//This file compares records executiontimes for a Binary Searchtree and a Binary Search
//The functions BinarySearchST  & BST are borrowed form Princetons Algorithms Fourth Edition, they are not present in this java file but are referenced in the code.
//Along with this the parts of main that are dedicated to finding the most common word of N*100 first words are from the frequency counter in Princetons Algorithms Fourth Edition.
public class Lab32 {

    public static void main(String[] args) {

        int[] N = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};                                                          //array med fler talet N initieras

        String strarray[]= new String[N[N.length-1]*100];                                                   //string array med all text initieras

        for(int j=0; j< strarray.length; j++)                                                               //cyclar igenom hela arrayn och kopierar in scannern
        {
            strarray[j] = StdIn.readString();                                                               //utnyttjar funktionen readString i StdIn för att kopiera
        }

        for (int i = 0; i < N.length; i++) {                                                                //utför jämförelsen N.length antal gånger

            long startBST = System.nanoTime();                                                              //börjar tidtagning start=kl då process börjat
            BST<String, Integer> st = new BST<String, Integer>();                                           //skapar nytt BST träd
            for (int b=0; b< (N[i]*100); b++) {                                                             //cyklar igenom hela string arrayn
                String word = strarray[b];                                                                  //word sätts till ordet på plats b i arrayn

                if (!st.contains(word)) st.put(word, 1);                                                //om trädet inte innehåller word läggs ordet in i trädet mha put funktinen
                else st.put(word, st.get(word) + 1);                                                    //annars uppdateras ordets värde i trädet för att matcha hur många gånger det funnits i texten
            }

            String max = " ";
            st.put(max, 0);
            for (String word : st.keys())
                if (st.get(word) > st.get(max))
                    max = word;

            long endBST = System.nanoTime();                                                                //avslutar tidtagning end=kl då process slutat
            long timepassedBST = endBST - startBST;                                                         //beräknar skillnaden mellan start klock start och slut klockslag


            long startBinarySearch = System.nanoTime();                                                     //börjar tidtagning start=kl då process börjat
            BinarySearchST<String, Integer> st2 = new BinarySearchST<String, Integer>();                    //skapar nytt träd
            for (int a=0; a< (N[i]*100); a++) {                                                             //kopierar in N*100 antal ord in i word2
                String word2 = strarray[a];

                if (!st2.contains(word2)) st2.put(word2, 1);                                            //om trädet inte innehåller ordet i word så läggs det till med value 1
                else st2.put(word2, st2.get(word2) + 1);                                                //annars sätts ordet in med ökat value
            }

            String max2 = " ";                                                                               //max initieras till noll
            st2.put(max2, 0);                                                                            //lägger in max 2 med value 0
            for (String word2 : st2.keys())                                                                  //går igenom hela st2 mha word2
                if (st2.get(word2) > st2.get(max2))                                                          //om value in word2 är stööre än max sätts max till word2
                    max2 = word2;

            long endBinarySearch = System.nanoTime();                                                         //avslutar tidtagning end=kl då process slutat

            long timepassedBinarysearch = endBinarySearch - startBinarySearch;                                //beräknar skillnaden mellan start klock start och slut klockslag


            System.out.println("Time for BST in nanoseconds with " + (N[i]*100) + "  elements: " + timepassedBST);
            System.out.println("Time for BinarySearchST in nanoseconds with " + (N[i]*100) + " elements: " + timepassedBinarysearch);

        }
    }
}