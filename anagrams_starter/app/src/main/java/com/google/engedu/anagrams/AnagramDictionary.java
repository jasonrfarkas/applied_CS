package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import android.util.Log;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashMap<String, ArrayList> lettersToWord  = new HashMap<String, ArrayList>();
    private HashSet<String> wordSet  = new HashSet<String>();
    private HashMap<Integer, ArrayList> sizeToWords = new HashMap<Integer, ArrayList>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            Integer wordSize = new Integer(word.length());
            if (sizeToWords.containsKey(wordSize)){
                ArrayList<String> wordsOfSize = sizeToWords.get(wordSize);
                wordsOfSize.add(word);
            }
            else{
                ArrayList<String> wordsOfSize = new ArrayList<String>();
                wordsOfSize.add(word);
                sizeToWords.put(wordSize, wordsOfSize);
            }
            String sorted_word = sortLetters(word);
            if (lettersToWord.containsKey(sorted_word)){
                ArrayList<String> anagramList =  lettersToWord.get(sorted_word);
                anagramList.add(word);
            }
            else{
                ArrayList<String> anagramList = new ArrayList<String>();
                anagramList.add(word);
                lettersToWord.put(sorted_word, anagramList);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        // substring checking is base insensitive
        if (!wordSet.contains(word) || word.contains(base)){
            return false;
        }
        return true;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        ///Log.d("getAnagrams", "getting anagrams");
        for (String anagram: wordList){
            if(anagram.length()!=targetWord.length() || anagram.equals(targetWord)){
                continue;
            }
            String sorted_target = sortLetters(targetWord);
            String sorted_anagram = sortLetters(anagram);
            if (sorted_anagram.equals(sorted_target)){
                result.add(anagram);
                //Log.d("anagrams", anagram);
            }
        }
        ///Log.d("getAnagrams", "got anagrams");
        return result;
    }

    private String sortLetters(String word){
        char chars[] = word.toCharArray();
        int min_index = 0;
        for (int i=0; i<word.length(); i++){
            min_index = i;
            for(int j=i; j<word.length();j++){
                if(chars[j] < chars[min_index]){
                    min_index = j;
                }
            }
            char temp = chars[i];
            chars[i] = chars[min_index];
            chars[min_index] = temp;
        }
        return new String(chars);
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char alphabet = 'a'; alphabet <= 'z';alphabet++) {
            String longer_word_key = sortLetters(word+alphabet);
            if (lettersToWord.containsKey(longer_word_key)){
                result.addAll(lettersToWord.get(longer_word_key));
            }
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithNMoreLetters(String word, int n) {
        ArrayList<String> result = new ArrayList<String>();
        if (n== 1){
            return getAnagramsWithOneMoreLetter(word);
        }
        else if(n<1){
            return getAnagrams(word);
        }
        ArrayList<String> nMinusOneAnagrams = getAnagramsWithNMoreLetters( word, n);
        for (String nMinusOne: nMinusOneAnagrams){
            for(char alphabet = 'a'; alphabet <= 'z';alphabet++) {
                String longer_word_key = sortLetters(nMinusOne+alphabet);
                if (lettersToWord.containsKey(longer_word_key)){
                    result.addAll(lettersToWord.get(longer_word_key));
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int number_of_anagrams = 0;
        String starter_word = "stop";
        ArrayList<String> wordsOfSize = sizeToWords.get(wordLength);
        while (number_of_anagrams < MIN_NUM_ANAGRAMS){
            int rand = (int)(Math.random()*wordsOfSize.size())%wordsOfSize.size();
            starter_word = wordsOfSize.get(rand);
            number_of_anagrams = getAnagramsWithOneMoreLetter(starter_word).size();
        }
        wordLength = Math.min(wordLength+1, MAX_WORD_LENGTH);
        return starter_word;
    }
}
