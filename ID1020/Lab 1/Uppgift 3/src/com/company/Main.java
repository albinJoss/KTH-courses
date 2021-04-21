package com.company;
/*
@Author Albin Jonsson & Malin Juad Almeida Marques (vi har jobbat tillsammans på denna uppgift)
made 2020-09-09

The code will create a doubly circular list which will be able to to enqueue and dequeue items. It has solved the problem 3 in the first lab in the course ID1020 at KTH.
the input is from a users standard input
 */
import java.util.*;
public class Main {
    public static class DLL<Item> implements Iterable<Item> {       //Double linked circular list
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

        public int size()
        {     //kollar storleken av den.
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

            public boolean hasNext()        //Kollar om nästa nod existerar
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

        public String print(DLL list) {         //Skriver ut
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

    public static void main(String[] args) {        //Test för funktionerna
        DLL test = new DLL();

        String cstr = " ";                                                                  //sträng som innehåller change
        while (true) {
            System.out.println(" Do you wish to dequeue?");                                 // ber usern om val         java program < mytextfile.txt
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();                                             //string som user beslut i

            //dequeque
            if (choice.equals("yes") & !test.isEmpty()) {                                   //om sparstrället inte e tomt och usern valde dequeue
                test.dequeue();                                                             //dequeue kallas
                System.out.println(test.print(test));

            } else if (test.isEmpty() & choice.equals("yes")) {                             //user svar check
                System.out.println("there is nothing to remove");                          //felsvar
            }

            //enque
            System.out.println("What do you want to enqueue?");                            //prompt
            cstr = scanner.nextLine();
            for (int i = 0; i < cstr.length(); i++) {
                test.enqueue(String.valueOf(cstr.charAt(i)));
            }
            System.out.println(test.print(test));
        }
    }
}