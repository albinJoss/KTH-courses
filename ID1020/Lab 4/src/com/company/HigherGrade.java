//@Author: Albin Jonsson & Malin J.A Marques
//Written october 6th and october 7th 2020
//This program uses Djikstras algorithm to solve a problem of getting the shortest path between to nodes passing through a third node. komplexitet är E*log(V)

package com.company;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
public class HigherGrade
{




        public static class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer>    //En minimal implementation av indexMinPQ som endast har de funktionerna som krävs för att få Dijakstras algoritm att funka
        {
            private int maxN;        //Maximalt antal av element som fins i PQ
            private int N;           //nuvarande antal av element i pq
            private int[] pq;        //En binär heap som använder 1 baserat index.
            private int[]  inversePQ;        //Inversen av pq
            private Key[] keys;      //prioriteten av index i finns på plats i inuti keys arrayen

            public IndexMinPQ(int maxN)         //Konstruktor för indexMinPQ
            {
                if (maxN < 0) throw new IllegalArgumentException();
                this.maxN = maxN;
                N = 0;
                keys = (Key[]) new Comparable[maxN + 1];        //Finns maxN + 1 då det minsta indexet som används är 1 istället för noll.
                pq   = new int[maxN + 1];
                inversePQ   = new int[maxN + 1];
                for (int i = 0; i <= maxN; i++)
                    inversePQ[i] = -1;
            }

            public boolean isEmpty()        //Returnerar om det finns några element element i kön.
            {
                return N == 0;
            }

            public boolean contains(int i)      //Validerar index och sedan så kollar den om ett index finns i  inversePQ. Returnerar om det finns med eller ej.
            {
                validateIndex(i);
                return  inversePQ[i] != -1;
            }

            public void insert(int i, Key key)      //Validerar index och kollar om det redan finns med i kön, om det gör det så kastas ett exception. Lägger sedan in en nyckel och dens associerade index i kön och låter den simma för att den ska hitta rätt plats.
            {
                validateIndex(i);
                if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
                N++;
                inversePQ[i] = N;
                pq[N] = i;
                keys[i] = key;
                swim(N);
            }

            public int delMin()         //Tar bort den minsta nyckeln och returnerar dens associerade index. Om det inte finns någon nyckel i kön så kastas ett exception.
            {
                if (N == 0) throw new NoSuchElementException("Priority queue underflow");
                int min = pq[1];
                exch(1, N--);
                sink(1);
                assert min == pq[N + 1];        //Kastar ett assertion error om min inte är lika med pq[N +1]
                inversePQ[min] = -1;        // sätter att sista minsta nyckeln inte finns
                keys[min] = null;    // Hjälper till med garbage collection
                return min;
            }

            public void changeKey(int i, Key key)       //Validerar index och om den ligger i kön. Ändrar på key som är associerad med index i och ändrar på dens plats till rätt plats i kön mha swim och sink.
            {
                validateIndex(i);
                if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
                keys[i] = key;
                swim( inversePQ[i]);
                sink( inversePQ[i]);
            }


            private void validateIndex(int i)       //Validerar om indexet är giltigt ock kastar annars ett IllegalArgumenException
            {
                if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
                if (i >= maxN) throw new IllegalArgumentException("index >= capacity: " + i);
            }


            private boolean greater(int i, int j)       //En generell hjälp funktion som jämför prioriteten av två element
            {
                return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
            }

            private void exch(int i, int j)             //En generell hjälp funktio som byter plats på två element
            {
                int swap = pq[i];
                pq[i] = pq[j];
                pq[j] = swap;
                inversePQ[pq[i]] = i;
                inversePQ[pq[j]] = j;
            }



            private void swim(int k)        //En heap hjälp funktion som går igenom och ändrar på ordningen av elementen så att det är i rätt ordning. den jämför med de större numrerna och lägger elementet där.
            {
                while (k > 1 && greater(k/2, k)) {
                    exch(k, k/2);
                    k = k/2;
                }
            }

            private void sink(int k)        //En heap hjälp funktion som går igenom och ändrar på ordningen om elementet är mindre än andra element så låter det sjunka mot botten av kön
            {
                while (2*k <= N)
                {
                    int j = 2*k;
                    if (j < N && greater(j, j+1)) j++;
                    if (!greater(k, j)) break;
                    exch(k, j);
                    k = j;
                }
            }





            public Iterator<Integer> iterator()     //Returnerar en iterator som iterarer över prioritetskön i stigande ordning
            {
                return new HeapIterator();
            }

            private class HeapIterator implements Iterator<Integer>
            {

                private IndexMinPQ<Key> copy;       //Ny prioritetskö

                public HeapIterator()       //Kopierar över alla element från heap till copy, linjär tid.
                {
                    copy = new IndexMinPQ<Key>(pq.length - 1);
                    for (int i = 1; i <= N; i++)
                        copy.insert(pq[i], keys[pq[i]]);
                }

                public boolean hasNext()        //Kollar om det finns ett nästa element
                {
                    return !copy.isEmpty();
                }

                public void remove()            //Tar bort ett element
                {
                    throw new UnsupportedOperationException();
                }

                public Integer next()           //Går till nästa elementet
                {
                    if (!hasNext()) throw new NoSuchElementException();
                    return copy.delMin();
                }
            }


            public static class DijkstraSP
            {
                private DirectedEdge[] edgeTo;
                private double[] distTo;
                private IndexMinPQ<Double> pq;

                public DijkstraSP(EdgeWeightedDiGraph G, int s)         //Konstruktor för en SP som använder Dijakstra algoritm
                {
                    edgeTo = new DirectedEdge[G.V()];
                    distTo = new double[G.V()];
                    pq = new IndexMinPQ<Double>(G.V());
                    for (int v = 0; v < G.V(); v++)
                        distTo[v] = Double.POSITIVE_INFINITY;
                    distTo[s] = 0.0;
                    pq.insert(s, 0.0);
                    while (!pq.isEmpty())
                        relax(G, pq.delMin());
                }

                private void relax(EdgeWeightedDiGraph G, int v)    //Går igenom de olika edges som finns och kollar vilken som är kortare. Om det finns en kortare än den som för tillfället är satt som kortaste distansen så ändras vilken edge som leder till den till den som är kortare och den andra kanten tas för tillfället bort
                {
                    for(DirectedEdge e : G.adj(v))
                    {
                        int w = e.to();
                        if (distTo[w] > distTo[v] + e.weight())
                        {
                            distTo[w] = distTo[v] + e.weight();
                            edgeTo[w] = e;
                            if (pq.contains(w)) pq.changeKey(w, distTo[w]);
                            else pq.insert(w, distTo[w]);
                        }
                    }
                }

                public double distTo(int v)                 //Returnerar vikten av vägen från source till nod v
                {
                    return distTo[v];
                }

                public boolean hasPathTo(int v)                 //Returnerar om det finns en väg mellan source och noden v
                {
                    return distTo[v] < Double.POSITIVE_INFINITY;
                }

                public Iterable<DirectedEdge> pathTo(int v)         //Går över DirectedEdges för att hitta ett path från source till den som man ska till
                {
                    if (!hasPathTo(v)) return null;
                    Stack<DirectedEdge> path = new Stack<DirectedEdge>();
                    for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
                        path.push(e);
                    return path;
                }
            }

            public static class DirectedEdge       //Api för en vägd en vägs graf för kanterna
            {
                private final int v;        //noden där kanten börjar
                private final int w;        //Noden där kanten slutar
                private final double weight;    //Vikten av kanten

                public DirectedEdge(int v, int w, double weight)       //Konstruktor för DirectedEdge
                {
                    this.v = v;
                    this.w = w;
                    this.weight = weight;
                }

                public double weight()      //Returnerar vikten för en kant
                {
                    return weight;
                }

                public int from()       //Returnerar noden där kanten börjar
                {
                    return v;
                }

                public int to()     //returnerar noden där kanten slutar
                {
                    return w;
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
                    return new Bag.ListIterator();
                }       //Skapar en iterator som går igenom alla items som ligger i bagen genom att skapa en ListIterator

                private class ListIterator implements Iterator<Item>
                {
                    private Bag.Node current = first;

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

            public static class EdgeWeightedDiGraph        //API för en en vägs graf med vägda kanter.
            {
                private final int V;        //Antal med noder
                private int E;              //Antal av kanter
                private Bag<DirectedEdge>[] adj;        //Närliggande noder

                public EdgeWeightedDiGraph(int V)       //Konstruktor av EdgeWeightedDiGraph
                {
                    this.V = V;
                    this.E = 0;
                    adj = (Bag<DirectedEdge>[]) new Bag[V];
                    for(int v = 0; v < V; ++v)
                        adj[v] = new Bag<DirectedEdge>();
                }

                public int V()      //Returnerar hur många noder som finns
                {
                    return V;
                }

                public int E()      //returnerar hur många kanter som finns i grafen
                {
                    return E;
                }

                public void addEdge(DirectedEdge e)     //Lägger till en kant
                {
                    adj[e.from()].add(e);
                    ++E;
                }

                public Iterable<DirectedEdge> adj(int v)    //Returnerar närliggande noder.
                {
                    return adj[v];
                }

                public Iterable<DirectedEdge> edges()       //Returnerar en bag för att iterera igenom alla edges som man går igenom.
                {
                    Bag<DirectedEdge> bag = new Bag<DirectedEdge>();
                    for(int v = 0; v < V; ++v)
                        for(DirectedEdge e : adj[v])
                            bag.add(e);
                    return bag;
                }
            }

            public static class Stack<Item>implements Iterable<Item>
            {                                                          //LIFO kö
                private Stack.Node first;                                                                                             //första nodensparas
                private int N;                                                                                                  // längden sparas

                private class Node {
                    Item item;                                                                                                  //defineras vad som finns i noder
                    Stack.Node next;
                }

                public void push(Item item) {                                                                                   //lägger in en nod på toppen
                    Stack.Node oldfirst = first;
                    first = new Stack.Node();                                                                                         //ny först nod
                    first.item = item;                                                                                          //ny förstnod innehåll
                    first.next = oldfirst;                                                                                      //nästa nod sätts till föredetta första noden
                    ++N;
                }

                public Iterator<Item> iterator()
                {                                                                              //initerar en ny listiterator
                    return new Stack.ListIterator();
                }

                private class ListIterator implements Iterator<Item>
                {
                    private Stack.Node current = first;                                                                               //skapar current nodne och sätter den till first

                    public boolean hasNext()
                    {                                                                                  //kollar om det finns en ny nod aka kollar så att valda noden inte är null
                        return current != null;
                    }

                    public Item next()
                    {                                                                                        //sparar item i current noden
                        Item item = (Item) current.item;
                        current = current.next;                                                                                 //sätter current till nåsta nod

                        return item;                                                                                            //returnerar ssparade item
                    }
                }
            }
            public static void main(String[] args) throws FileNotFoundException
            {
                File data = new File("F:\\Higher_grade_input.txt");                   //sparar filnamn
                FileInputStream NYC = new FileInputStream(data);                                                 //skapar en fileinputstream till texten

                Scanner scn1 = new Scanner(NYC);                                                                //skapar första skannern till the database

                int nodes = scn1.nextInt();
                int edges = scn1.nextInt();

                int v = 0;
                int w = 0;
                double weight = 0.0;

                EdgeWeightedDiGraph G = new EdgeWeightedDiGraph(nodes);


                while(scn1.hasNextLine())
                {
                    v = scn1.nextInt();
                    w = scn1.nextInt();
                    weight = scn1.nextDouble();

                    DirectedEdge e = new DirectedEdge(v, w, weight);
                    G.addEdge(e);
                }

                System.out.println(G.E());

                System.out.println("Start:");
                Scanner userstart = new Scanner(System.in);                                                             //startar scanners till användarens val av avgångs nod och destinationsnod
                int start= userstart.nextInt();

                System.out.println("End:");
                Scanner userend = new Scanner(System.in);
                int end= userend.nextInt();

                System.out.println("Through:");
                Scanner passThrough = new Scanner(System.in);
                int passby= passThrough.nextInt();

                DijkstraSP DSP1 = new DijkstraSP(G, start);
                DijkstraSP DSP2 = new DijkstraSP(G, passby);
                double totWeight = 0;

                if(DSP1.hasPathTo(passby) && DSP2.hasPathTo(end))
                {

                    for(DirectedEdge ef : DSP1.pathTo(passby)) {
                        if (ef.from() == start) {
                            System.out.println("[" + ef.from() + "]");
                            System.out.println("[" + ef.to() + "]");
                        } else
                            System.out.println("[" + ef.to() + "]");

                    }
                    totWeight += DSP1.distTo(passby);
                    for(DirectedEdge efg : DSP2.pathTo(end)) {

                        System.out.println("[" + efg.to() + "]");
                    }
                    totWeight += DSP2.distTo(end);
                }
                else
                    System.out.println("No paths");
                System.out.println();
                System.out.println("The total weight in the path is: " + totWeight);


            }
        }
    }



