package com.company;
/*
@Author Albin Jonsson & Malin Juad Almeida Marques (vi har jobbat tillsammans på denna uppgift)
made 2020-09-09

The code will create a doubly circular list which will be able to to enqueue and dequeue items from the front and back of the queue. It has solved problem 4 in Lab 1 of the course3 ID1020 at KTH

It uses the input that is entered in the prompt.
 */
import java.util.*;
public class Main {
    public static class DLL<Item> implements Iterable<Item> {
        private Node firstNode;
        private int numOfElements = 0;

        private class Node<Item>        //konstruktor av noderna
        {
            Item item;
            Node next;
            Node prev;
        }

        public boolean isEmpty()        //Kollar om listan är tom
        {
            return firstNode == null;
        }

        public int size() {     //Kollar strolek
            return numOfElements;
        }

        public void enqueue(Item item)      //En fuktion som lägger till ett item i början av kön
        {
            Node newElement = new Node();           //ny nod skapas
            newElement.item = item;                 //item som skickas med sätts som item i noden

            if (!isEmpty()) {
                newElement.next = firstNode;         //pekaren next i newelement sätts till firstNode
                newElement.prev = firstNode.prev;    //pekaren prev i new element sätts till first element prev
                firstNode.prev.next = newElement;    //first node prevs next pekare sätts på new element
                firstNode.prev = newElement;         ////länkar det elementet som var först innan nu pekar på newElement som elementet som kommer före det
            } else {
                firstNode = newElement;
                firstNode.prev = newElement;        //Sätter att det pekar på sig själv som sitt sista element.
                firstNode.next = newElement;        //Gör så att det pekar på sig själv som nästa element

            }
            firstNode = newElement;
            ++numOfElements;
        }

        public void backEnqueue(Item item) {        //sätter in ett element längst bak i listan.
            Node newElement = new Node();
            newElement.item = item;
            if (!isEmpty()) {
                newElement.next = firstNode;
                newElement.prev = firstNode.prev;
                firstNode.prev.next = newElement;
                firstNode.prev = newElement;
            } else {
                firstNode = newElement;
                firstNode.prev = newElement;
                firstNode.next = newElement;

            }
            firstNode.prev = newElement;
        }

        public Item dequeue()       //En funktion som tar bort det sista itemet i kön och returerar det.
        {
            Item out = (Item) firstNode.item;
            if (!isEmpty() && numOfElements != 1) {
                out = (Item) firstNode.prev.item;      //sista Item är det som ska skickas ut
                firstNode.prev = firstNode.prev.prev;   //sista noden ska sättas till den näst sista
                firstNode.prev.next = firstNode;        //sista nodens länk till nästa nod sätts till den första noden
            } else if (numOfElements == 1) {
                firstNode = null;
            } else {
                return null;
            }
            --numOfElements;
            return out;
        }

        public Item frontDequeue() {        //tar bort ett element från början av listan
            Item out = null;
            if (!isEmpty()) {
                out = (Item) firstNode.item;
                if (firstNode == firstNode.next & firstNode == firstNode.prev) {
                    firstNode.item = null;
                } else {
                    firstNode = firstNode.next;
                    firstNode.prev.prev.next = firstNode;
                    firstNode.prev = firstNode.prev.prev;
                }
            } else if (numOfElements == 1) {
                firstNode = null;
            } else {
                return null;
            }
            --numOfElements;
            return out;
        }

        public String toString()     //returnerar rekursivt en sträng som skriver ut alla noder i FIFO ordning
        {
            StringBuilder s = new StringBuilder();
            for (Item item : this) {
                s.append('[');
                s.append(item);
                s.append(']');
                s.append(',');
            }
            return s.toString();
        }

        public Iterator<Item> iterator()    //Tillkallar klassen Iterator som kommer att gå igenom vad som finns i listan i FIFO order.
        {
            return new linkIterator(firstNode.prev);
        }

        private class linkIterator implements Iterator<Item> {
            private Node currentNode;

            public linkIterator(Node firstNode) {
                currentNode = firstNode;        //Börjar vid den noden som den blir tillsagd

            }

            public boolean hasNext()        //Kollar om det finns en nod på nästa plats
            {
                return Objects.equals(currentNode, firstNode);
            }

            public Item next()      //kollar om den har ett nästa object om den har det så skickar den ut det sista objektet i listan.
            {
                if (!hasNext())
                    throw new NoSuchElementException("all elements have been dequeued");
                Item item = (Item) currentNode.item;
                currentNode = currentNode.prev;
                return item;

            }
        }

        public String print(DLL list)       //Skriver ut listan
        {
            String listString;
            if (!isEmpty()) {
                listString = "[" + list.firstNode.item + "]";
                Node pointer = list.firstNode.next;

                while (pointer != list.firstNode) {
                    listString += ", [" + pointer.item + "]";
                    pointer = pointer.next;
                }
            } else {
                listString = "[" + null + "]";
            }
            return listString;


        }




    }
    public static void main(String[] args)      //En test klass för att lägga in och ta bort saker i den.
    {   DLL save = new DLL();
        while (true)
        {
            while (true)
            {
                System.out.println("Do you wish to: ");          //java program < mytextfile.txt               *
                System.out.println("1. remove the last letter");
                System.out.println("2. remove the first letter");
                System.out.println("3. add a text to the end");
                System.out.println("4. add a text to the front");

                Scanner scanner = new Scanner(System.in);
                String s = scanner.nextLine();

                if (s.equals("1")) {
                    System.out.println("1. remove the last letter");
                    if (!save.isEmpty()) {
                        if(save.firstNode==save.firstNode.next & save.firstNode==save.firstNode.prev)
                        {
                            save.firstNode.item= null;
                        }
                        else {
                            save.dequeue();
                        }
                        System.out.println(save.print(save));
                    }
                    else
                        s="10";
                }
                if (s.equals("2")) {
                    System.out.println("2. remove the first letter");
                    if (!save.isEmpty())
                    {
                        save.frontDequeue();
                        System.out.println(save.print(save));
                    }
                    else

                        s="10";
                }
                if (s.equals("3")) {
                    System.out.println("3. add a text to the end");
                    Scanner scnr = new Scanner(System.in);
                    String nw = scnr.nextLine();
                    for (int i=0; i<nw.length();i++)
                    {
                        save.backEnqueue(String.valueOf(nw.charAt(i)));
                        for(int j=nw.length(); j==0; j-- ) {
                            save.firstNode = save.firstNode.prev;
                        }
                    }
                    System.out.println(save.print(save));
                }
                if (s.equals("4")) {
                    System.out.println("4. add a text to the front");
                    Scanner scnr = new Scanner(System.in);
                    String nw = scnr.nextLine();
                    for (int i=0; i<nw.length();i++)
                    {
                        save.enqueue(String.valueOf(nw.charAt(i)));
                    }

                    System.out.println(save.print(save));

                }
}}}}