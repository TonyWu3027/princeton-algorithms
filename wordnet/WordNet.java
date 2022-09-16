import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Iterator;

public class WordNet {

    private int synsetCount; // the number of synsets
    private final HashMap<String, Bag<Integer>> nounIdMap;
    // a symbol table with nouns as keys and an iterable of IDs of the synsets whereby the noun appears as values
    // since a noun can exist in more than one synset
    private final HashMap<Integer, String> idSynsetMap;
    // a symbol table with IDs as keys and the synsets in String as values
    private Digraph G; // the directed acyclic graph of the WordNet
    private final SAP sap; // the shortest ancestral path object

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Name of the input files should not be null");

        this.nounIdMap = new HashMap<>();
        this.idSynsetMap = new HashMap<>();

        this.readSynsetsFile(synsets);
        this.readHypernymsFile(hypernyms);
        this.validateRootedDAG();

        this.sap = new SAP(this.G);
    }

    /**
     * Read the synsets file and store the ID and synsets
     * in {@code nounIdMap} mapping
     *
     * @param name the name of the synset file
     */
    private void readSynsetsFile(String name) {
        In in = new In(name);

        int id;
        String synsetStr;
        String[] fields, synset;
        Bag<Integer> ids;

        while (in.hasNextLine()) {
            fields = in.readLine().split(",");

            id = Integer.parseInt(fields[0]);
            synsetStr = fields[1];
            synset = synsetStr.split(" ");

            this.idSynsetMap.put(id, synsetStr);

            for (String noun : synset) {
                // associate the synset with its id in the mapping
                ids = this.nounIdMap.getOrDefault(noun, new Bag<>());
                ids.add(id);
                this.nounIdMap.put(noun, ids);
            }

        }

        this.synsetCount = this.idSynsetMap.size();
    }

    /**
     * Read the hypernyms file and create a directed graph
     * of the WordNet
     *
     * @param name the name of the hypernyms file
     */
    private void readHypernymsFile(String name) {
        In in = new In(name);

        int synset, hypernym;
        String[] fields;

        this.G = new Digraph(this.synsetCount);

        while (in.hasNextLine()) {
            fields = in.readLine().split(",");

            synset = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                hypernym = Integer.parseInt(fields[i]);

                // create an edge synset -> hypernym
                G.addEdge(synset, hypernym);
            }
        }
    }

    /**
     * Validate if {@code G} is a rooted directed acyclic graph or not,
     *
     * @throws IllegalArgumentException if {@code G} is not a singly rooted DAG
     */
    private void validateRootedDAG() {
        // check for cycle in the digraph
        DirectedCycle dc = new DirectedCycle(this.G);
        if (dc.hasCycle())
            throw new IllegalArgumentException("There is a cycle in the WordNet digraph");

        // check if the digraph is rooted
        int rootCount = 0;
        for (int i = 0; i < this.G.V(); i++) {
            // a root has 0 out-degree
            if (this.G.outdegree(i) == 0) rootCount++;
        }

        if (rootCount != 1) {
            String msg = String.format("The WordNet digraph should have 1 root, but %d found",
                                       rootCount);
            throw new IllegalArgumentException(msg);
        }

    }

    /**
     * Returns all WordNet nouns
     *
     * @return all WordNet nouns
     */
    public Iterable<String> nouns() {
        return nounIdMap.keySet();
    }

    /**
     * Is the word a WordNet noun?
     *
     * @param word the word to be checked
     * @return <code>true</code> if word is a WordNet noun, vice versa
     */
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("Word should not be null");

        return this.nounIdMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)

    /**
     * The length of the shortest ancestral path between any synset containing in nounA
     * and any synset containing nounB; -1 if no such path
     *
     * @param nounA a noun
     * @param nounB another noun
     * @return The length of the SAP; -1 if no such path
     * @throws IllegalArgumentException if any noun is null or is not a recorded noun
     */
    public int distance(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);

        // retrieve the sets of IDs containing nounA and nounB
        Bag<Integer> setA = this.nounIdMap.get(nounA);
        Bag<Integer> setB = this.nounIdMap.get(nounB);

        return this.sap.length(setA, setB);
    }

    /**
     * a synset that is the common ancestor of nounA and nounB
     * in the shortest ancestral path
     *
     * @param nounA a noun
     * @param nounB another noun
     * @return a synset that is the common ancestor of noun A and nounB; null if no such path
     * @throws IllegalArgumentException if any noun is null or is not a recorded noun
     */
    public String sap(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);

        // retrieve the sets of IDs containing nounA and nounB
        Bag<Integer> setA = this.nounIdMap.get(nounA);
        Bag<Integer> setB = this.nounIdMap.get(nounB);

        int ancestorId = this.sap.ancestor(setA, setB);
        return this.idSynsetMap.get(ancestorId);
    }

    /**
     * Check if a given word is a noun or not.
     *
     * @param word a given word to be checked
     * @throws IllegalArgumentException if word is null or word is not a noun
     */
    private void validateNoun(String word) {
        if (!isNoun(word)) {
            String msg = String.format("%s is not a noun", word);
            throw new IllegalArgumentException(msg);
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");

        String word1 = "tony";
        StdOut.printf("%s is noun is: %b, expecting: %b\n", word1, wn.isNoun(word1), false);

        String word2 = "zone";
        StdOut.printf("%s is noun is: %b, expecting: %b\n", word2, wn.isNoun(word2), true);

        StdOut.printf("Distance between 'worm' and 'bird' is: %d, expecting: 5\n",
                      wn.distance("worm", "bird"));

        int count = 0;
        Iterator<String> nouns = wn.nouns().iterator();
        while (nouns.hasNext()) {
            count++;
            nouns.next();
        }
        StdOut.printf("Number of nouns is %d, expecting: 119188", count);
    }
}
