//@Author: Malin J. A. Marques & Albin Jonsson
//Fil skapad 02-10-2020, redigerad 03-10-2020
//Gör en directed graph och använder DFS för att hitta en väg mellan två noder.

package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

public class DelTre
{
    public static void main(String[] args) throws FileNotFoundException         //Driver kod
    {
        File data = new File("F:\\Algoritmer och datastrukturer\\Lab 4\\src\\com\\company\\TheDatabase");                   //sparar filnamn
        FileInputStream theDatabase = new FileInputStream(data);                                                 //skapar en fileinputstream till texten
        Scanner scn1 = new Scanner(theDatabase);
        Scanner userstart= new Scanner(System.in);
        Scanner userend= new Scanner(System.in);

        BinarySearchST<String, Integer> index = new BinarySearchST<String, Integer>(200);


        int i = 0;
        String location;
        String origin;
        String destination;

        while (scn1.hasNextLine()) {

            location = scn1.next();
            if (!index.contains(location)) {
                index.put(location, i);
                ++i;
            }
            System.out.println(location + " " + index.get(location));
        }

        DiGraph G = new DiGraph(i);

        theDatabase = new FileInputStream(data);            //ger theDatabase filen igen för att sätta filepointern till början av filen
        Scanner scn2 = new Scanner(theDatabase);

        while (scn2.hasNextLine()) {
            origin = scn2.next();
            destination = scn2.next();

            G.addEdge(index.get(origin), index.get(destination));
            System.out.println("added a path between " + origin + "(index " + index.get(origin) + ") and "
                    + destination + "(index " + index.get(destination) + ")");
        }

        System.out.println("Origin:");
        String users= userstart.nextLine();
        System.out.println("Destination:");
        String usere=userend.nextLine();

        int start = index.get(users);
        int end = index.get(usere);
        System.out.println("start: " + start + "end: " + end);

        String AL = "AL";
        String WY = "WY";


        String[] keys = new String[index.size()];           //Skapar en array så man kan göra ett reverse look up från index til namnet
        for(String name : index.keys(AL, WY)) {
            keys[index.get(name)] = name;
        }

        for(int n = 0; n < keys.length; ++n)
            System.out.println("Name: " + keys[n] + " Index: " + n);
        System.out.println();


        DepthFirstPaths search = new DepthFirstPaths(G, start);

        System.out.print(users + " to " + usere + ": ");
        if (search.hasPathTo(end))
        {

            for (int x : search.pathTo(end))
                if (x == start) System.out.print(keys[x]);
                else System.out.print("->" + keys[x] + "(" + x + ")");
        }
        else
            System.out.println("There is no path");

        System.out.println();
    }


    public class DirectedDFS
    {
        private boolean[] marked;

        public DirectedDFS(DiGraph G, int s)        //Konstruktor för en directed DFS
        {
            marked = new boolean[G.V()];
            dfs(G, s);
        }

        public DirectedDFS(DiGraph G, Iterable<Integer> sources)        //Går över alla sources för att markera dem.
        {
            marked = new boolean[G.V()];
            for(int s : sources)
                if(!marked[s])
                    dfs(G, s);
        }

        private void dfs(DiGraph G, int v)                      //Rekursivt lägger till vilka som är närliggande till platsen på index v
        {
            marked[v] = true;
            for(int w : G.adj(v))
                if(!marked[w])
                    dfs(G, w);
        }

        public boolean marked(int v)
        {
            return marked[v];
        }       //Kollar om platsen som ligger på index v är markerat

    }

    public static class DepthFirstPaths {
        private boolean[] marked;
        private int[] edgeTo;
        private final int s;

        public DepthFirstPaths(DiGraph G, int s)            //Kunstruktor för DepthFirstPaths
        {
            marked = new boolean[G.V()];
            edgeTo = new int[G.V()];
            this.s = s;
            dfs(G, s);
        }

        private void dfs(DiGraph G, int V)              //går igenom och markerar alla kanter som finns i grafen. Detta görs rekursivt
        {
            marked[V] = true;
            for (int w : G.adj(V))
                if (!marked[w]) {
                    edgeTo[w] = V;
                    dfs(G, w);
                }
        }

        public boolean hasPathTo(int v)
        {
            return marked[v];
        }       //Returnerar om det finns ett sätt att ta sig från källan som vi har satt till platsen som finns på index v

        public Iterable<Integer> pathTo(int v)      //Använder en stack för att gå igenom och hitta ett sätt att komma från source till destinationen
        {
            if (!hasPathTo(v)) return null;
            Stack<Integer> path = new Stack<Integer>();
            for (int x = v; x != s; x = edgeTo[x])
                path.push(x);

            path.push(s);
            return path;
        }
    }

    public static class Stack<Item>implements Iterable<Item>
    {
        private Node first;
        private int N;

        private class Node      //Konstruktor för noder
        {
            Item item;
            Node next;
        }

        public void push(Item item)     //Lägger in ett item i stacken
        {
            Node oldfirst = first;
            first = new Node();
            first.item = item;
            first.next = oldfirst;
            ++N;
        }

        public Item pop()           //Används aldrig men är menat för att få ut ett item från stacken
        {
            Item item = first.item;
            first = first.next;
            --N;
            return item;
        }

        public int size()
        {
            return N;
        }       //returnerar storleken på stacken

        public boolean isEmpty()
        {
            return N == 0;
        }       //Kollar om stacken är tom

        public Iterator<Item> iterator()
        {
            return new ListIterator();
        }   //Skapar en ListIterator som ska gå igenom stacken

        private class ListIterator implements Iterator<Item>
        {
            private Node current = first;

            public boolean hasNext()
            {
                return current != null;
            }       //Kollar om det finns ett item på nästa plats i stacken

            public Item next()          //Går till nästa plats i stacken
            {
                Item item = current.item;
                current = current.next;

                return item;
            }
        }
    }

    public static class BinarySearchST<Key extends Comparable<Key>, Value>
    {
        private Key[] keys;         //String
        private Value[] vals;       //Integer
        private int N;

        public BinarySearchST(int capacity)     //Konstruktor till ett binarysearch symbol table
        {
            keys = (Key[]) new Comparable[capacity];
            vals = (Value[]) new Object[capacity];
        }
        public int size()       //Returnerar storleken på trädet
        { return N; }


        public Boolean isEmpty()        //Kollar om trädet är tomt
        {return size()==0;}

        public Value get(Key key)       //Hämtar värdet för en key
        {
            if (isEmpty()) return null;
            int i = rank(key);
            if (i < N && keys[i].compareTo(key) == 0) return vals[i];
            else return null;
        }

        public int rank(Key key)        //Använder mergecompare för att gå igenom trädet och hitta på vilket ställe den ligger
        {
            int lo = 0, hi = N-1;
            while (lo <= hi)
            {
                int mid = lo + (hi - lo) / 2;
                int cmp = key.compareTo(keys[mid]);
                if (cmp < 0) hi = mid - 1;
                else if (cmp > 0) lo = mid + 1;
                else return mid;
            }
            return lo;
        }

        public void put(Key key, Value val)     //Leta efter en key. ändra värdet om den redan finns annars gör trädet större och lägg in den
        { // Search for key. Update value if found; grow table if new.
            int i = rank(key);
            if (i < N && keys[i].compareTo(key) == 0)
            { vals[i] = val; return; }
            for (int j = N; j > i; j--)
            { keys[j] = keys[j-1]; vals[j] = vals[j-1]; }
            keys[i] = key; vals[i] = val;
            N++;
        }

        public boolean contains(Key challenger)         //Kollar igenom trädet för att se om en key redan ligger i det.
        {
            if (challenger == null) throw new IllegalArgumentException("There is no key");
            else {
                return get(challenger) != null;
            }
        }


        public Iterable<Key> keys(Key lo, Key hi)           //Använder en kö för att gå igenom alla nycklar från den som har en låg rang till den som har högre rang
        {
            Queue<Key> q = new Queue<Key>();
            for (int i = rank(lo); i < rank(hi); i++)
                q.enqueue(keys[i]);
            if (contains(hi)) {
                q.enqueue(keys[rank(hi)]);
                System.out.println("test1");
            }
            return q;
        }
    }

    public static class Bag<Item> implements Iterable<Item>
    {
        private Bag.Node first;
        private int N;

        private class Node      //Gör en konstruktor för noder för bagen
        {
            Item item;
            Bag.Node next;
        }

        public void add(Item item)      //Skapar en metod som låter den lägga till items i bagen
        {
            Bag.Node oldfirst = first;
            first = new Bag.Node();
            first.item = item;
            first.next = oldfirst;
            ++N;
        }

        public int size() {
            return N;
        }       //Returnerar storleken på bagen

        public boolean isEmpty() {
            return N == 0;
        }       //Kollar om bagen är tom och returnerar resultatet av det

        public Iterator<Item> iterator()
        {
            return new ListIterator();
        }       //Skapar en iterator som går igenom alla items som ligger i bagen genom att skapa en ListIterator

        private class ListIterator implements Iterator<Item>
        {
            private Node current = first;

            public boolean hasNext() {
                return current != null;
            }       //Kollar om nästa nod finns

            public Item next()      //Går till nästa nod
            {
                Item item = (Item) current.item;
                current = current.next;

                return item;
            }
        }
    }

    public static class DiGraph
    {
        private final int V; // håller koll på hur många noder som finns
        private int E; //Håller koll på hur många kanter som finns
        private Bag<Integer>[] adj; //Gör en bag där man lägger in alla närliggande noder

        public DiGraph(int V)           //Konstruktor för DiGraph
        {
            this.V = V;
            this.E = 0;
            adj = (Bag<Integer>[]) new Bag[V]; // Create array of lists.
            for (int v = 0; v < V; v++) // Initialize all lists
                adj[v] = new Bag<Integer>(); // to empty.
        }

        public int V() { return V; }        //Returnerar hur många noder som finns

        public int E() { return E; }        //Returnerar hur många kanter som finns

        public void addEdge(int v, int w)          //Lägger till en kant mellan två noder
        {
            adj[v].add(w); //Lägger till w till vs bag av närliggande noder
            E++;            //inkremerar hur många kanter det finns i grafen
        }
        public DiGraph reverse()        //Skapar en graf som är omvänd
        {
            DiGraph R = new DiGraph(V);
            for(int v = 0; v < V; ++v)
                for(int w : adj(v))
                    R.addEdge(w, v);
            return R;
        }

        public Iterable<Integer> adj(int v)     //Går över de närliggande noderna från noden som ligger på index v
        { return adj[v]; }
    }

    public static class Queue<Item> implements Iterable<Item>
    {
        private Queue.Node first;
        private Queue.Node last;
        private int N;

        private class Node                  //Konstruktor för noderna för kön
        {
            Item item;
            Queue.Node next;
        }

        public boolean isEmpty() {
            return N == 0;
        }       //Returnerar om kön är tom

        public int size() {
            return N;
        }           //Returnerar hur många items som finns i kön

        public void enqueue(Item item)          //Lägger till ett element i listan
        {
            Queue.Node oldlast = last;
            last = new Queue.Node();
            last.item = item;
            last.next = null;

            if (isEmpty())
                first = last;

            else
                oldlast.next = last;

            ++N;
        }

        public Iterator<Item> iterator() {
            return new ListIterator();
        }       //Skapar en iterator som tillkallar Listiterator

        private class ListIterator implements Iterator<Item>
        {
            private Node current = first;

            public boolean hasNext() {
                return current != null;
            }       //Kollar om det finns ett nästa element i kön

            public Item next()          //Går till nästa nod i kön
            {
                Item item = (Item) current.item;
                current = current.next;

                return item;
            }
        }
    }
}
