import java.io.*;
import java.util.*;
import java.lang.*;
//Written by Malin J. A. Marques & Albin Jonsson & August Paulsrud & Elin Liu
//fil skapad 24-09-2020, senast redigerad 25-09-2020
//Det här programmet låter användaren söka efter ord i the text och ger positionen i texten
//Ingen kod är lånad (annat än BinarySearchST och StdIn som refereras till i koden och är lånade från Princetons Algorithms Fourth Edition, men som inte förekommer i denna fil)

public class Lab34 {
    public static void main(String[] args) throws IOException {

        BinarySearchST<String, LinkedList> wordtree = new BinarySearchST<String, LinkedList>();             //skapar binary string träd

        int counter = 0;                                                                                    //counter initieras till noll
        boolean prev = false;                                                                               //indikator för om föregående ord är en bokstav
        StringBuilder newword = new StringBuilder();                                                        //stringbuilder skapas

        File text = new File("/Users/malinm/IdeaProjects/Lab3/src/TheText.txt");                  //sparar filnamn
        FileInputStream thetext = new FileInputStream(text);                                                //skapar en fileinputstream till texten
        InputStream stan =System.in;                                                                     //skapar en file input stream til stan input
        System.setIn(thetext);                                                                              //sätter input till texten

        int wordcount=0;
        while (!StdIn.isEmpty()) {                                                                          //går igenom hela texten (genom att kolla om input är tom
            char c = StdIn.readChar();                                                                      //läser in texten ett char i taget

            if (Character.isLetter(c)) {                                                                    //om charen är en bokstav

                if (prev) {                                                                                 //och tidigare char var en bokstav
                    newword.append(c);                                                                      //läggs boksta´´en in i newword stringbuildern
                } else {
                    newword.delete(0, newword.length());                                                    //om tidigare bokstav inte var det betyder det att vi börjat ett nytt ord
                    newword.append(c);                                                                      //då tömmer vi stringbuildern och lägger in en ny bokstav
                    wordcount=counter;                                                                      //och sparar ordets counter värde
                }
                prev = true;                                                                                //efter detta sätts prev till sant
            } else {                                                                                        //om charen inte är en bokstav
                if (prev) {                                                                                 //men prev var det så betyder det att ett ord precis slutat

                    if (!wordtree.contains(newword.toString())) {                                           //trädet kollas för att se om det innehåller newword
                        LinkedList<Integer> list = new LinkedList<Integer>();                               //om det inte gör det skapas en ny länkad lista för ordets platser
                        list.add(wordcount);                                                                //ordets position sparas i listan
                        wordtree.put(newword.toString(), list);                                             //ordet läggs in i trädet med listan som value
                    } else {
                        LinkedList<Integer> extr = new LinkedList<Integer>();                               //om ordet redan finns i listan skapas en ny länkad lista
                        extr = wordtree.get(newword.toString());                                            //som sätts lika med förra listan, men det får även den nya positionen
                        extr.add(wordcount);
                        wordtree.put(newword.toString(), extr);                                             //ordet läggs in igen nu med en updaterad lista
                    }
                    prev = false;                                                                           //prev sätts till falskt
                }
            }
            counter++;                                                                                      //varje nytt char räknas i countern
        }

        System.out.print("Please enter the word you are looking for:");                                     //ger info till användaren

        System.setIn(stan);                                                                                 //sätter nu om inputen till stan (std in)

        Scanner scn = new Scanner(stan);                                                                    //skapar scanner för användarens svar
          String word = scn.nextLine();                                                                     //word sätts til användarens svar


        if (wordtree.contains(word))                                                                        //kollar om trädet inehåller ordet
        {
            LinkedList<Integer> askedpositions = new LinkedList<Integer>();                                 //om det är fallet skapas en lista med de ombedda positionerna
            askedpositions= wordtree.get(word);                                                             //listan med det ombedda positionerna sätts som lika med listan med positionerna för de ombedda ordet
            System.out.print("Word exists at positions: ");                                                 //användaren får deta vad numrena innebär
            for(int i=0; i < askedpositions.size(); i++)                                                    //cyklar igenom listan
            {
                System.out.print( askedpositions.get(i) + " ");                                             //printar ut varje position som ordet funnits på
            }
        }
        else
            System.out.println("word does not exist in text");                                              //om ordet inte finns i texten informeras användaren om det
    }
}
