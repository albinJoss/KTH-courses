import java.util.*;
//Skriven av Malin J. A. Marques & Albin Jonsson & August Paulsrud & Elin Liu
//skapad 24-09-2020
//Den här koden ger ut hash-koder till orden som finns i the text
//There is no borrowed code
public class Lab33 {

        private static int hash(String x, int M)                            //skriver om hash
        {
            return ((x.hashCode() & 0x7fffffff) % M);
        }

        public static void main(String[] args)
        {
            BST<String, Integer> st = new BST<String, Integer>();           //Binary search tree (BST) initieras
            int length = Integer.parseInt(args[0]);
            int M = length/4;                                               //variabeln sätts till 1/4 av längden
            int hashSum[] = new int[M];                                     //en int array av längd M startas för att lagra hash i
            int hash = 0;                                                   //
            for (int i = 0; i < length; i++) {                              //&& !StdIn.isEmpty()  <- skulle behövas om man inte vet hur lång texten är
                String word = StdIn.readString();                           //läser the text in i word mha Stdins funktion readstring
                if(!(st.contains(word)))                                    //om trädet inte innehåller ordet
                {
                    hash = hash(word, M);                                   //hash tar in hash kod för visst ord
                    System.out.println(word+ " "+hash);                               //printar ut hash för det ordet
                    ++hashSum[hash];                                        //ökar hashsum på plats hash
                }
                else
                    st.put(word, 1);                                    //annars läggs ordet in i trädet med value 1



            }



    }
}
