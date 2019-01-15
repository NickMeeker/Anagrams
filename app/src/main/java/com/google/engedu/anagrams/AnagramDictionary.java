package com.google.engedu.anagrams;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;

    private Random random;
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<String, ArrayList<String>> wordMap;
    private HashMap<Integer, ArrayList<String>> sizeToWords;
    private int wordLength;

    public AnagramDictionary(Reader reader) throws IOException {
        this.wordSet = new HashSet<String>();
        this.wordMap = new HashMap<String, ArrayList<String>>();
        this.wordList = new ArrayList<String>();
        this.sizeToWords = new HashMap<Integer, ArrayList<String>>();
        this.wordLength = DEFAULT_WORD_LENGTH;
        this.random = new Random();

        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            String sorted = sortLetters(word);

            if(wordMap.get(sorted) == null) {
                wordMap.put(sorted, new ArrayList<String>());
            }

            ArrayList<String> arrList = wordMap.get(sorted);
            if(!arrList.contains(word))
                arrList.add(word);

            wordSet.add(word);
            wordList.add(word);

            if(sizeToWords.get(word.length())==null)
                sizeToWords.put(word.length(), new ArrayList<String>());

            sizeToWords.get(word.length()).add(word);
        }
    }

    public String sortLetters(String s){
        char[] sort = s.toCharArray();
        Arrays.sort(sort);
        return new String(sort);
    }

    // The given class, AnagramsActivity, calls this method.
    // Rather than modify the template, this method
    // just returns to getAnagramsWithOneMoreLetter().
    public List<String> getAnagrams(String targetWord) {
        return getAnagramsWithOneMoreLetter(targetWord);
    }

    // "Real work" happens here
    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < 26; i++) {
            String test = word + (char)(i+'a');
            test = sortLetters(test);
            if(wordMap.containsKey(test)) {
                ArrayList<String> strings = wordMap.get(test);
                for(String string:strings){
                    if(string.indexOf(word) >= 0){
                        continue;
                    }
                    result.add(string);
                }
            }
        }
        return result;
    }

    public boolean isGoodWord(String word, String base) {
        if(!wordSet.contains(word))
            return false;

        if(word.indexOf(base) >= 0){
            return false;
        }

        return true;
    }

    public String pickGoodStarterWord() {
        // Can hard-code specific test cases
        //return "warder";

        Random r = new Random();
        ArrayList<String> arr = null;
        while(arr == null){
            arr = sizeToWords.get(wordLength);
            if(wordLength<MAX_WORD_LENGTH)
                wordLength++;
        }
        int i = r.nextInt(arr.size());
        String word;
        int size = 0;
        do{
            size = -1;
            word = arr.get(i);
            ArrayList<String> map = wordMap.get(sortLetters(word));
            if(map != null) {
                size = map.size();
            }
            i++;
            if(i == arr.size())
                i = 0;
        }while(size < MIN_NUM_ANAGRAMS);
        return word;
    }

}