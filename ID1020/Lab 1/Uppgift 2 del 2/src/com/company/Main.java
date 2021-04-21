/*
@Author Albin Jonsson & Malin Juad Almeida Marques (vi har jobbat tillsammans på denna uppgift)
2020-09-03

This code will use the input of the user and reverse it and print it in the terminal
The code is made for assignment 2 in lab 1 for the course ID1020. It uses an iterative function to
 */

package com.company;
import java.util.*;

class StackOfStrings<String> {

    private Node first = null;              //skapa nod
    private class Node      //Konstruktor för nod 
    {
        String item;
        Node next;
    }

    public void push (String text)          //Pushar karaktären till stacken
    {
        Node oldfirst= first;
        first = new Node();
        first.item= text;
        first.next= oldfirst;
    }

    public String Pop()                     //poppar bokstaven från stacken
    {
        String item = first.item;
        first = first.next;
        return item;
    }

    public Boolean isEmpty()
    {
        return first == null;           //  returnera 1 om nod är tom
    }
}
public class Main {

    public static void main (String[] args)
    {

        iterative();
    }

    private static void iterative()
    {                                               //Kommer att iterativt ta karaktärerna och pusha dem till stacken och sen popa dem för att skriva ut dem.
        Scanner text = new Scanner(System.in);
        StackOfStrings save = new StackOfStrings();
        String str = text.nextLine();
        char returstr;
        int i;
        for( i=0; i<str.length(); i++) {
            save.push(str.charAt(i));
        }
        for( ; i>0; i--) {
            returstr = (char) save.Pop();
            System.out.print ((i != 1) ? ("[" + returstr + "],") : ("[" + returstr + "]"));
        }

    }
}
