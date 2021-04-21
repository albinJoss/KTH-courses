package com.company;
//@Author: Malin J. A. Marques & Albin Jonsson
//Fil skapad 02-10-2020, redigerad 03-10-2020
//Den här filen skapar ett Symbol Tree med index som representerar noder i en graf från filen TheDatabase.txt, en graf skapas sedan med nodernas index.
//Då grafen är skapad kan användaren fråga om vägar mellan två noder. Använder BFS för att hitta vägen mellan noderna.
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

public class DelTvå
{
    public static void main(String[] args) throws FileNotFoundException {
        File data = new File("F:\\Algoritmer och datastrukturer\\Lab 4\\src\\com\\company\\TheDatabase");                   //sparar filnamn
        FileInputStream theDatabase = new FileInputStream(data);                                                 //skapar en fileinputstream till texten

        Scanner scn1 = new Scanner(theDatabase);                                                                //skapar första skannern till the database

        Scanner userstart = new Scanner(System.in);                                                             //startar scanners till användarens val av avgångs nod och destinationsnod
        Scanner userend = new Scanner(System.in);

        BinarySearchST<String, Integer> index = new BinarySearchST<String, Integer>(200);               //skapar en binary search ST för att inehålla noder och index


        int i = 0;
        String location;                                                                                         // skapar stäng för vald nod
        String origin;                                                                                           //skapar stäng för startnod
        String destination;                                                                                      //skapar stäng för slutnod

        while (scn1.hasNextLine()) {                                                                             //scannar in databasen

            location = scn1.next();                                                                              //"väljer" en precis inläst nod
            if (!index.contains(location)) {                                                                     //kollar om den noden redan finns i ST
                index.put(location, i);                                                                          //lägger in noden om det inte redan finns
                i += 1;                                                                                          //skapar nytt index för nästa nya nod
            }
            // System.out.println(location + " " + index.get(location));                                          //kan synliggöra noder och index vid behov
        }

        Graph G = new Graph(i);                                                                                 //skapar ny graf med längd=antalet noder
        theDatabase = new FileInputStream(data);                                                                //ser till så att hedatabase åter igen pekar på början av filen
        Scanner scn2 = new Scanner(theDatabase);                                                                //skapar nästa scanner för the database

        while (scn2.hasNextLine()) {                                                                            //läser in databasen
            origin = scn2.next();                                                                               //sätter läser in startnod
            destination = scn2.next();                                                                          //läser in slutnod
            G.addEdge(index.get(origin), index.get(destination));                                               //lägger in kanten i grafen
        }

        String first = "AL";                                                                                       //sparar AL (förstanod)
        String last = "WY";                                                                                        //sparar WY (sistanod


        String[] namekeys = new String[index.size()];                                                               //skapar en sträng av samma längd som index
        for (String name : index.keys(first, last)) {                                                                //går igenom hela iterable arrayn med nod namn och sätter varje som name
            namekeys[index.get(name)] = name;                                                                       //sparar varje nodnomn i dess indexposition i arrayn
        }
        System.out.println("To find the a path from X to Y please insert");                                         //ger instruktione till användaren
        System.out.println("X:");
        String users = userstart.nextLine();                                                                        //användarens startnod sparas som sträng
        System.out.println("Y:");
        String usere = userend.nextLine();                                                                          //användarens slutnod sparas som sträng

        int start = index.get(users);                                                                               //indexen för användarens slut och start nod sparas
        int end = index.get(usere);

        BreadthFirstPaths search = new BreadthFirstPaths(G, start);                                                     //skapar depthfirstPaths med grafen G coh användarebs startnod

        System.out.print("from " + users + " to " + usere + ": ");                                                      //påminner användaren igen om vilka noder som valts
        if (search.hasPathTo(end)) {                                                                                    //kollar först om det finns någon path
            for (int loc : search.pathTo(end))                                                                          //itererar igenom search pathsTo med användarens slut nod som mål
                if (loc == start)
                    System.out.print(namekeys[loc]);                                                                     //startnoden skrivs ut först
                else
                    System.out.print("-" + namekeys[loc]);                                                               //alla andra noder skrivs ut efter det tills slutnoden är nådd
        } else {
            System.out.print("No paths where found");
        }                                                                                                               //fel meddelande skickas


    }

    public static class BreadthFirstPaths {
        private boolean[] marked;                                                                                       //initierar en boolean array för att visa vilka noder som redan passerats
        private int[] edgeTo;                                                                                           //initierar en int array med alla kanter
        private final int s;                                                                                            //initierar en int för start

        public BreadthFirstPaths(Graph G, int s) {
            marked = new boolean[G.V()];                                                                                //ger en rellevant längd till markerade arrayn
            edgeTo = new int[G.V()];                                                                                    //ger en rellevant längd till kant arrayn
            this.s = s;                                                                                                 //ger start medskickat värde
            bfs(G, s);                                                                                                  //kallar bfs
        }

        private void bfs(Graph G, int s) {
            Queue<Integer> queue = new Queue<Integer>();                                                                //skapar kö
            marked[s] = true;                                                                                           //markerar starten
            queue.enqueue(s);                                                                                           //och lägger in den i kön
            while (!queue.isEmpty()) {                                                                                  //då kön inte är tom
                int v = queue.dequeue();                                                                                //tar den bort nästa nod från kön
                for (int w : G.adj(v))
                    if (!marked[w])                                                                                     //för alla icke märkta noder
                    {
                        edgeTo[w] = v;                                                                                  //sparas sista kanten på kortaste sträckan
                        marked[w] = true;                                                                               //markeras  den då vi nu känner till den
                        queue.enqueue(w);                                                                               //lägger till den till kön
                    }
            }
        }

        public boolean hasPathTo(int v) {                                                                               //returnerar om noden bliv märkt under "utforskningen av grafen"
            return marked[v];
        }

        public Iterable<Integer> pathTo(int v) {                                                                        //iterator med vägar
            if (!hasPathTo(v)) return null;                                                                             //returnerar null om det inte finns någon väg
            Stack<Integer> path = new Stack<Integer>();                                                                  //skapar en stack med en path
            for (int x = v; x != s; x = edgeTo[x])                                                                      //x sätts till v och om x då inte är start sätts x till edge to av x
                path.push(x);                                                                                           //pushar x till pathen

            path.push(s);                                                                                               //slutligen pushar start
            return path;                                                                                                //returnerar stacken path
        }
    }

    public static class BinarySearchST<Key extends Comparable<Key>, Value> {
        private Key[] keys;                                                                                             //initierar array emd keys
        private Value[] vals;                                                                                           //initierar array  med värden
        private int N;                                                                                                  //initierar int N där sotleken sparas

        public BinarySearchST(int capacity) {
            keys = (Key[]) new Comparable[capacity];                                                              //skapar båda arrays med med skickad längd
            vals = (Value[]) new Object[capacity];
        }

        public int size() {                                                                                         //returnerar storleken
            return N;
        }

        public Boolean isEmpty() {                                                                                  //kollar om ST är tom
            return size() == 0;
        }

        public Value get(Key key) {
            if (isEmpty())
                return null;                                                                                            //kollar om ST är tom
            int i = rank(key);                                                                                          //initerar int till kanken av med skickad nyckel
            if (i < N && keys[i].compareTo(key) == 0)
                return vals[i];                                                                                         //om i inte är utanför arrayn och nyckeln på positionen med nycklns rank inte är dessamma som nyckeln returneras värdet som nycklen har
            else
                return null;                                                                                       //annars returneras noll
        }

        public int rank(Key key) {
            int lo = 0, hi = N - 1;                                                                                     //sparar största och minsta indexet i arrayerna
            while (lo <= hi) {                                                                                          //cyklar igenom arrayenra
                int mid = lo + (hi - lo) / 2;                                                                           //sätter mid till värdet i mitten mellan lo och hi
                int cmp = key.compareTo(keys[mid]);                                                                     //sparar värdet av en jämförelse mellan med skickad nyckeln och nyckeln på plats mid
                if (cmp < 0)
                    hi = mid - 1;                                                                                       //om cmp <0 minskas hi
                else if (cmp > 0)
                    lo = mid + 1;                                                                                       //om cmp >0 ökar lo
                else
                    return mid;                                                                                         //annars är ranken hittad och mid returnneras
            }
            return lo;                                                                                                  //om hela arrayn lyckas cyklas igenom innebär det att det måste vara rank för lo
        }

        public void put(Key key, Value val) {
            int i = rank(key);                                                                                          //sparar medskickade nyckelns rank
            if (i < N && keys[i].compareTo(key) == 0) {                                                                 //om i <N coh keys på plats i =0 sätts värdet på den platsen tillmed skickat värde och returnera
                vals[i] = val;
                return;
            }
            for (int j = N; j > i; j--) {                                                                               //cyklar igenom alla positioner på keys från sista till i
                keys[j] = keys[j - 1];                                                                                  //sätter nyckeln till föregående nyckel
                vals[j] = vals[j - 1];                                                                                  //sätter värde till föregående värde
            }
            keys[i] = key;                                                                                              //sätter nyckeln på rank till nyckel
            vals[i] = val;                                                                                              //sätter värde på rank till värde
            N++;                                                                                                        //uppdatera längden
        }

        public boolean contains(Key challenger) {
            if (challenger == null)
                throw new IllegalArgumentException("There is no key");                                                  //kastar exceptioni fallnyckeln pekar till null
            else {                                                                                                      //ananrs kollar funktionen om nyckeln kan hämtas eller ej
                return get(challenger) != null;
            }
        }

        public Iterable<Key> keys(Key lo, Key hi) {
            Queue<Key> q = new Queue<Key>();                                                                            //kö bildas
            for (int i = rank(lo); i < rank(hi); i++)                                                                   //för varje ranki som är mindre än rank hi
                q.enqueue(keys[i]);                                                                                     //läggs keys av i på kön
            if (contains(hi))                                                                                           //om hi finns
                q.enqueue(keys[rank(hi)]);                                                                              //sätts även hi i kön
            return q;                                                                                                   //kön q returneras
        }
    }

    public static class Graph {
        private final int V;                                                                                            // initera antalet vertices
        private int E;                                                                                                  //initera antalet kanter
        private Bag<Integer>[] adj;                                                                                     // initera adjacency lists

        public Graph(int V) {
            this.V = V;                                                                                                 //sätt V till vi
            this.E = 0;                                                                                                 //och E till 0
            adj = (Bag<Integer>[]) new Bag[V];                                                                          // skapa array av listor
            for (int v = 0; v < V; v++)                                                                                 // cykla igenom arrayn och initiera alla listsor
                adj[v] = new Bag<Integer>();
        }

        public int V() {                                                                                                //returnera v
            return V;
        }

        public int E() {                                                                                                //returnera e
            return E;
        }

        public void addEdge(int v, int w) {
            adj[v].add(w);                                                                                              // lägg till w till vs lista
            adj[w].add(v);                                                                                              // lägg till v till ws lista
            E++;                                                                                                        //öka e
        }

        public Iterable<Integer> adj(int v) {                                                                           //returnera arrayn adj
            return adj[v];
        }
    }


    public static class Bag<Item> implements Iterable<Item> {
        private Bag.Node first;                                                                                         //initera första nodne i Bagen

        private class Node {                                                                                            //definera noderna
            Item item;
            Bag.Node next;
        }

        public void add(Item item) {                                                                                    //lägger in en nod på toppen
            Bag.Node oldfirst = first;
            first = new Bag.Node();                                                                                     //ny först nod
            first.item = item;                                                                                          //ny förstnod innehåll
            first.next = oldfirst;                                                                                      //nästa nod sätts till föredetta första noden
        }

        public Iterator<Item> iterator() {                                                                              //initerar en ny listiterator
            return new Bag.ListIterator();
        }

        private class ListIterator implements Iterator<Item> {
            private Bag.Node current = first;                                                                           //skapar current nodne och sätter den till first

            public boolean hasNext() {                                                                                  //kollar om det finns en ny nod aka kollar så att valda noden inte är null
                return current != null;
            }

            public Item next() {                                                                                        //sparar item i current noden
                Item item = (Item) current.item;
                current = current.next;                                                                                 //sätter current till nåsta nod

                return item;                                                                                            //returnerar ssparade item
            }
        }
    }
    public static class Queue<Item> implements Iterable<Item> {                                                         //fifo kö
        private Queue.Node first;                                                                                       //initerar variabler för första sista och antalet noder
        private Queue.Node last;
        private int N;

        private class Node {                                                                                            //definerar nod
            Item item;
            Queue.Node next;
        }

        public boolean isEmpty() {
            return N == 0;                                                                                              //returnera om kön är tomm
        }

        public int size() {                                                                                             //returnera storlek
            return N;
        }

        public void enqueue(Item item) {                                                                                //lägg på nod i slutet av listan
            Queue.Node oldlast = last;                                                                                  //spara föredetta sista
            last = new Queue.Node();                                                                                    //sätter sista till en ny nod
            last.item = item;                                                                                           //sättr förra item till item
            last.next = null;                                                                                           //sätter nästa till null

            if (isEmpty())
                first = last;                                                                                           //om listan är tom sätts first = last

            else
                oldlast.next = last;                                                                                    //annars sätts föredetta last next till nu sista noden

            ++N;                                                                                                        //öka antalet noder
        }

        public Item dequeue() {                                                                                         //ta bort första noden
            Item item = (Item) first.item;
            first = first.next;

            if (isEmpty())
                last = null;

            --N;

            return item;
        }

        public Iterator<Item> iterator() {                                                                              //initerar en ny listiterator
            return new Queue.ListIterator();
        }

        private class ListIterator implements Iterator<Item> {
            private Queue.Node current = first;                                                                         //skapar current nodne och sätter den till first

            public boolean hasNext() {                                                                                  //kollar om det finns en ny nod aka kollar så att valda noden inte är null
                return current != null;
            }

            public Item next() {                                                                                        //sparar item i current noden
                Item item = (Item) current.item;
                current = current.next;                                                                                 //sätter current till nåsta nod

                return item;                                                                                            //returnerar ssparade item
            }
        }
    }
    public static class Stack<Item> implements Iterable<Item> {                                                     //LIFO kö
        private Stack.Node first;                                                                                   //första nodensparas
        private int N;                                                                                              // längden sparas

        private class Node {
            Item item;                                                                                              //defineras vad som finns i noder
            Node next;
        }

        public void push(Item item) {                                                                               //lägger in en nod på toppen
            Node oldfirst = first;
            first = new Stack.Node();                                                                               //ny först nod
            first.item = item;                                                                                      //ny förstnod innehåll
            first.next = oldfirst;                                                                                  //nästa nod sätts till föredetta första noden
            ++N;
        }

        public Iterator<Item> iterator() {                                                                          //initerar en ny listiterator
            return new ListIterator();
        }

        private class ListIterator implements Iterator<Item> {
            private Stack.Node current = first;                                                                     //skapar current nodne och sätter den till first

            public boolean hasNext() {                                                                              //kollar om det finns en ny nod aka kollar så att valda noden inte är null
                return current != null;
            }

            public Item next() {                                                                                    //sparar item i current noden
                Item item = (Item) current.item;
                current = current.next;                                                                             //sätter current till nåsta nod

                return item;                                                                                        //returnerar ssparade item
            }
        }
    }
}
