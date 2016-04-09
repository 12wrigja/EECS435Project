/**
 * Created by james on 3/29/16.
 */
public class SQLAnalyzer {
    public static void main(String[] args) {

    }


    public static double getSentimentWeight(String[] listOfSentiments) {
        double weight = 0;
        int sum = 0;
        for(int i = 0; i < listOfSentiments.length; i++) {
            sum += getWeightOfSentence(listOfSentiments[i]);
        }
        return (double)sum/listOfSentiments.length;
    }

    // The higher the number, the more positive it is.
    public static int getWeightOfSentence(String sentence) {
        switch (sentence) {
            case "Very negative":
                return -4;
            case "Negative":
                return -1;
            case "Neutral":
                return 0;
            case "Positive":
                return 1;
            case "Very positive":
                return 4;
        }
        return -100;
    }
}
