/*
@Author Albin Jonsson

2020-09-02
This code uses the input of the user
The code will reverse the code using a recursive function that  will break it into substrings.
 */

package com.company;
import java.util.*;
public class Main {

    public static String recursiveReverse(String originalString)        //recursively reverses a string of any length
    {
        int len = originalString.length();
        String firstHalf = originalString.substring(0, len / 2); //delar in strängen i två halvor. använder divide and conquer algoritm för att den ska returera den sista bokstaven först
        String secondHalf = originalString.substring(len / 2, len);

        return len <= 1 ? originalString : (
                recursiveReverse(secondHalf) +
                        recursiveReverse(firstHalf)
        );    //kollar om längden av strängen är 1 eller mindre om den är det så går den till basfallet där den skickar tillbaka den strängen, om inte så skickas halvorna till funktionen och den fortsätter.
    }

    public static void print(String printed)
    {
        int len = printed.length();

        for (int i = 0; i < len; ++i) {
            System.out.print((i == len - 1) ? ("[" + printed.charAt(i) + "]") : ("[" + printed.charAt(i) + "],"));
        }
    }

    public static void main(String[] args)      //scans standard input and makes a string of it to reverse in the reverse string function.
    {
        Scanner scanner = new Scanner(System.in);

        String originalString = scanner.nextLine();

        String reversedString = recursiveReverse(originalString);

        print(reversedString);
    }
}
