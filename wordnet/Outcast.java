import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wn;

    public Outcast(WordNet wordnet) {
        this.wn = wordnet;
    }

    public String outcast(String[] nouns) {
        // Pre-condition: nouns are assumed to contain only valid wordnet nouns according to the spec
        int maxDistanceSum = Integer.MIN_VALUE;
        int distanceSum;
        String outcastNoun = "";

        for (int i = 0; i < nouns.length; i++) {
            distanceSum = 0;
            for (int j = 0; j < nouns.length; j++) {
                distanceSum += wn.distance(nouns[i], nouns[j]);
            }
            if (maxDistanceSum < distanceSum) {
                maxDistanceSum = distanceSum;
                outcastNoun = nouns[i];
            }
        }
        return outcastNoun;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
