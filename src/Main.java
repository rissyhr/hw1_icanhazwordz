import java.io.*;
import java.util.*;

public class Main {

    static char[] twoPoints = {'c', 'f', 'h', 'l', 'm', 'p', 'v', 'w', 'y'};
    static char[] threePoints = {'j', 'k', 'q', 'x', 'z'};

    public static void main(String[] args) {

        ArrayList<String> sortedWordsList = new ArrayList<>();
        HashMap<String,String> map = new HashMap<>();

        try {
            File file = new File("resources/dictionary.txt");
            if (!file.exists()) {
                System.out.print("File Not Found");
                return;
            }
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String word;
            while ((word = bufferedReader.readLine()) != null) {
                String wordWithoutQu = word.replace("qu","q");
                sortedWordsList.add(getSortedString(wordWithoutQu));
                map.put(getSortedString(wordWithoutQu) , word);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Please Enter up to 16 letters!");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        scanner.close();
        str = str.replace("qu","q");

        str = getSortedString(str);
        System.out.println(str);
        char[] searchKey = str.toCharArray();
        int totalNumber = (int) Math.pow(2, searchKey.length);

        // make search patterns
        String[] searchPatterns = new String[totalNumber];
        Arrays.fill(searchPatterns, "");
        int index = searchPatterns.length;
        for (int i = 0; i < searchKey.length; i++) {
            index /= 2;
            System.out.println(searchKey[i] + " index: " + index);
            int cnt = index;
            for (int j = 0; j < searchPatterns.length; j++) {
                searchPatterns[j] = searchPatterns[j] + searchKey[i];
                cnt--;
                if (cnt == 0) {
                    if (j + index == searchPatterns.length - 1) {
                        break;
                    } else {
                        cnt = index;
                        j += index;
                    }
                }
            }
        }

        // do binary search
        Collections.sort(sortedWordsList);
        int currentHigh = 0;
        int score;
        for (String searchWord : searchPatterns) {
            if (searchWord.length() > 2) {
                int n = Collections.binarySearch(sortedWordsList, searchWord);
                if (n > 0){
                    String anagram = map.get(sortedWordsList.get(n));
                    score = calcScore(anagram);
                    if (score >= currentHigh){
                        System.out.println("Score:" + score + " Anagram: " + anagram);
                        currentHigh = score;
                    }
                }
            }
        }
    }

    static int calcScore(String str) {
        int score = 0;
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (Arrays.binarySearch(threePoints, c) >= 0) {
                score += 3;
            } else if (Arrays.binarySearch(twoPoints, c) >= 0) {
                score += 2;
            } else {
                score++;
            }
        }
        score++;   // bonus point
        return score * score;
    }

    static String getSortedString(String str) {
        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}

