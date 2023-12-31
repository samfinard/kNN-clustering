import java.util.*;

public class TFIDF {
    private List<docLabel> documents;
    private List<String> conceptWords;
    private double[][] tfidfMatrix;
    private Map<String, Integer> docFreq;

    public TFIDF(List<docLabel> documents) {
        this.documents = documents;
        this.conceptWords = createConceptWords(documents);
        this.tfidfMatrix = makeMatrix();
    }


    private double[][] makeMatrix() {
        var res = new double[documents.size()][conceptWords.size()];        
        // Calculate document frequency for each concept word
        docFreq = new HashMap<>();
        for (String word : conceptWords) {
            for (docLabel doc : documents) {
                if (doc.contains(word)) {
                    docFreq.put(word, docFreq.getOrDefault(word, 0) + 1);
                }
            }
        }
        // Calculate TF-IDF for each document and concept word
        for (int i = 0; i < documents.size(); i++) {
            var document = documents.get(i);
            res[i] = getVector(document); 
        }
        return res;
    }

    public double[] getVector(docLabel document) {
        double[] res = new double[conceptWords.size()];
        double docCount = documents.size();
        for (int j = 0; j < conceptWords.size(); j++) {
            String word = conceptWords.get(j);
            // Count the occurrences of the word in the document
            var tf = document.wordCount(word);
            var df = docFreq.getOrDefault(word, 0);
            double idf;
            // Calculate IDF
            idf = Math.log(1 + (docCount / ((double) (df))));
            res[j] = tf * idf;
        }
        return res;
    }
    private List<String> createConceptWords(List<docLabel> docs) {
        Set<String> uniqueWords = new HashSet<>();
        for (var doc : docs) {
            uniqueWords.addAll(Arrays.asList(doc.words));
        }
        return new ArrayList<>(uniqueWords);
    }
}