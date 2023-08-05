import java.util.*;

public class kNN {
    private TFIDF tfidfMatrix;
    private List<docLabel> docLabels;
    boolean logAddOne;

    public kNN(List<docLabel> docs,boolean logAddOne) {
        this.docLabels = docs;
        this.tfidfMatrix = new TFIDF(docs, logAddOne);
        this.logAddOne = logAddOne;
    }
    public String classifyDocument(String unknownDoc, int k, String distanceMetric) { // e c    n
        List<docLabel> docDist = new ArrayList<>();
        // calculate distance from unknownDoc to all others
        for (docLabel doc : docLabels) {
            double distance;
            if (distanceMetric.equalsIgnoreCase("ncd")) {
                distance = similarity.NCD(unknownDoc, doc.doc);
            } else {
                var unknownDocVector = tfidfMatrix.getVector(unknownDoc, logAddOne);
                var otherDocVector = tfidfMatrix.getVector(doc.doc, logAddOne);

                if (distanceMetric.equalsIgnoreCase("cos")) {
                    distance = similarity.cosineSim(unknownDocVector, otherDocVector);
                } else { // Default to Euclidean distance
                    distance = similarity.euclideanDist(unknownDocVector, otherDocVector);
                }
            }
            docDist.add(new docLabel(doc.doc, doc.label, distance));
        }
        // sort the distances and keep the top k
        Collections.sort(docDist, (a, b) -> Double.compare(b.distance, a.distance));
        var topKDocs = docDist.subList(0, k);

        // count the labels and return the most frequent one
        Map<String, Integer> labelFrequency = new HashMap<>();
        for (docLabel doc : topKDocs) {
            labelFrequency.put(doc.label, labelFrequency.getOrDefault(doc.label, 0) + 1);
        }

        // Find the label with the highest frequency
        Map.Entry<String, Integer> mostFreq = null;
        for (Map.Entry<String, Integer> entry : labelFrequency.entrySet()) {
            if (mostFreq == null || entry.getValue() > mostFreq.getValue()) {
                mostFreq = entry;
            }
        }
        return mostFreq.getKey();
    }

}
