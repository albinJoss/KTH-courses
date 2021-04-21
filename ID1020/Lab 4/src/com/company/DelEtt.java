//@Author: Malin J. A. Marques & Albin Jonsson
//Fil skapad 02-10-2020, redigerad 03-10-2020
//Den här filen skapar ett Symbol Tree med index som representerar noder i en graf från filen TheDatabase.txt, en graf skapas sedan med nodernas index.
//Då grafen är skapad kan användaren fråga om vägar mellan två noder. Använder DFS för att hitta vägen mellan noderna.

package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

public class DelEtt
{
    public static void main(String[] args) throws FileNotFoundException
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

        Graph G = new Graph(i);

        theDatabase = new FileInputStream(data);
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


        String[] keys = new String[index.size()];
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

        System.out.println();
    }


    public static class DepthFirstPaths {
        private boolean[] marked;
        private int[] edgeTo;
        private final int s;

        public DepthFirstPaths(Graph G, int s) {
            marked = new boolean[G.V()];
            edgeTo = new int[G.V()];
            this.s = s;
            dfs(G, s);
        }

        private void dfs(Graph G, int V) {
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
        }

        public Iterable<Integer> pathTo(int v) {
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

        private class Node
        {
            Item item;
            Node next;
        }

        public void push(Item item)
        {
            Node oldfirst = first;
            first = new Node();
            first.item = item;
            first.next = oldfirst;
            ++N;
        }

        public Item pop()
        {
            Item item = first.item;
            first = first.next;
            --N;
            return item;
        }

        public int size()
        {
            return N;
        }

        public boolean isEmpty()
        {
            return N == 0;
        }

        public Iterator<Item> iterator()
        {
            return new ListIterator();
        }

        private class ListIterator implements Iterator<Item>
        {
            private Node current = first;

            public boolean hasNext()
            {
                return current != null;
            }

            public Item next()
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
        public BinarySearchST(int capacity)
        {
            keys = (Key[]) new Comparable[capacity];
            vals = (Value[]) new Object[capacity];
        }
        public int size()
        { return N; }

        public void sort()
        {

        }

        public void printST()
        {
            for(int k = 0; k < size(); ++k)
                System.out.println("Name: " + keys[k] + " Index: " + rank(keys[k]));
        }

        public Boolean isEmpty()
        {return size()==0;}

        public Value get(Key key)
        {
            if (isEmpty()) return null;
            int i = rank(key);
            if (i < N && keys[i].compareTo(key) == 0) return vals[i];
            else return null;
        }
        public int rank(Key key)
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
        public void put(Key key, Value val)
        { // Search for key. Update value if found; grow table if new.
            int i = rank(key);
            if (i < N && keys[i].compareTo(key) == 0)
            { vals[i] = val; return; }
            for (int j = N; j > i; j--)
            { keys[j] = keys[j-1]; vals[j] = vals[j-1]; }
            keys[i] = key; vals[i] = val;
            N++;
        }
        public boolean contains(Key challenger) {
            if (challenger == null) throw new IllegalArgumentException("There is no key");
            else {
                return get(challenger) != null;
            }
        }

        public Key name(int v)
        {
            return keys[v];
        }
        public Iterable<Key> keys(Key lo, Key hi)
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
    public static class Bag<Item> implements Iterable<Item> {
        private Node first;
        private int N;

        private class Node {
            Item item;
            Node next;
        }

        public void add(Item item) {
            Node oldfirst = first;
            first = new Node();
            first.item = item;
            first.next = oldfirst;
            ++N;
        }

        public int size() {
            return N;
        }

        public boolean isEmpty() {
            return N == 0;
        }

        public Iterator<Item> iterator() {
            return new ListIterator();
        }

        private class ListIterator implements Iterator<Item> {
            private Node current = first;

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                Item item = (Item) current.item;
                current = current.next;

                return item;
            }
        }
    }

    public static class Graph
    {
        private final int V; // number of vertices
        private int E; // number of edges
        private Bag<Integer>[] adj; // adjacency lists
        public Graph(int V)
        {
            this.V = V; this.E = 0;
            adj = (Bag<Integer>[]) new Bag[V]; // Create array of lists.
            for (int v = 0; v < V; v++) // Initialize all lists
                adj[v] = new Bag<Integer>(); // to empty.
        }
        public Graph(Scanner in)
        {
            this(in.nextInt()); // Read V and construct this graph.
            int E = in.nextInt(); // Read E.
            for (int i = 0; i < E; i++)
            { // Add an edge.
                int v = in.nextInt(); // Read a vertex,
                int w = in.nextInt(); // read another vertex,
                addEdge(v, w); // and add edge connecting them.
            }
        }
        public int V() { return V; }
        public int E() { return E; }
        public void addEdge(int v, int w)
        {
            adj[v].add(w); // Add w to v’s list.
            adj[w].add(v); // Add v to w’s list.
            E++;
        }
        public Iterable<Integer> adj(int v)
        { return adj[v]; }
    }
    public static class Queue<Item> implements Iterable<Item> {
        private Node first;
        private Node last;
        private int N;

        private class Node {
            Item item;
            Node next;
        }

        public boolean isEmpty() {
            return N == 0;
        }

        public int size() {
            return N;
        }

        public void enqueue(Item item) {
            Node oldlast = last;
            last = new Node();
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
        }

        private class ListIterator implements Iterator<Item> {
            private Node current = first;

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                Item item = (Item) current.item;
                current = current.next;

                return item;
            }
        }
    }
    public class DepthFirstSearch {
        private boolean[] marked;
        private int count;

        public DepthFirstSearch(Graph G, int s) {
            marked = new boolean[G.V()];
            dfs(G, s);
        }

        private void dfs(Graph G, int v) {
            marked[v] = true;
            count++;
            for (int w : G.adj(v))
                if (!marked[w]) dfs(G, w);
        }

        public boolean marked(int w) {
            return marked[w];
        }

        public int count() {
            return count;
        }
    }
}
