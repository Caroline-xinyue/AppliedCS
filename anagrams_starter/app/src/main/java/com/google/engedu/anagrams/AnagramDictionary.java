/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import android.util.Log;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static final int NUM_CHARS = 256;
    private int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    private HashSet<String> wordSet = new HashSet();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap();
    private ArrayList<String> wordList = new ArrayList<>();

    public AnagramDictionary(Reader reader) throws IOException
    {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null)
        {
            String word = line.trim();
            wordSet.add(word);
            String sortedWord = sortLetters(word);
            ArrayList<String> updatedList = null;
            if(lettersToWord.containsKey(sortedWord))
            {
                updatedList = lettersToWord.get(sortedWord);
            } else
            {
                updatedList = new ArrayList();
            }
            updatedList.add(word);
            lettersToWord.put(sortedWord, updatedList);

            ArrayList<String> updatedsizeList = null;
            int len = word.length();
            if(sizeToWords.containsKey(len))
            {
                updatedsizeList = sizeToWords.get(len);
            } else
            {
                updatedsizeList = new ArrayList();
            }
            updatedsizeList.add(word);
            sizeToWords.put(len, updatedsizeList);
            wordList.add(word);
        }
    }

    public boolean isGoodWord(String word, String base)
    {
        return wordSet.contains(word) && !word.contains(base);
    }

    public List<String> getAnagrams(String targetWord)
    {
        ArrayList<String> result = new ArrayList<String>();
        String sortedStr = sortLetters(targetWord);
        Log.v("sorted String", sortedStr);
        for(String word : wordSet) {
            if(word.length() == sortedStr.length() && word.equals(sortedStr))
            {
                result.add(word);
            }
        }
        return result;
    }

    private String sortLetters(String inputStr)
    {
        String sortedStr = new String();
        int[] charCount = new int[NUM_CHARS];
        // sort letters of the input string
        for(char ch : inputStr.toCharArray())
        {
            charCount[ch - 'a']++;
        }
        for(int i = 0; i < charCount.length; i++)
        {
            for(int j = 0; j < charCount[i]; j++)
            {
                char ch = (char)('a' + i);
                sortedStr += ch;
            }
        }
        return sortedStr;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word)
    {
        ArrayList<String> result = new ArrayList<String>();
        for(char i = 'a'; i <= 'z'; i++)
        {
            String wordWithOneMoreLetter = word + i;
            String sortedWordWithOneMoreLetter = sortLetters(wordWithOneMoreLetter);
            if(lettersToWord.containsKey(sortedWordWithOneMoreLetter))
            {
                result.addAll(lettersToWord.get(sortedWordWithOneMoreLetter));
            }
        }
        return result;
    }

    public String pickGoodStarterWord()
    {
        /*
        If your game is working, proceed to implement pickGoodStarterWord to make the game more
        interesting. Pick a random starting point in the wordList array and check each word in
        the array until you find one that has at least MIN_NUM_ANAGRAMS anagrams.
        Be sure to handle wrapping around to the start of the array if needed.
         */
        /*
        Then in pickGoodStarterWord, restrict your search to the words of length wordLength,
        and once you're done, increment wordLength (unless it's already at MAX_WORD_LENGTH)
        so that the next invocation will return a larger word.
         */
        String starterWord = "";
        ArrayList<String> restrictedLenWordList = sizeToWords.get(wordLength);
        /*
        Random rand = new Random();
        int start = rand.nextInt(wordList.size());
        while(true)
        {
            String word = wordList.get(start);
            if(lettersToWord.get(sortLetters(word)).size() >= MIN_NUM_ANAGRAMS)
            {
                starterWord = word;
                break;
            } else
            {
                int len = wordList.size();
                start = (start + 1) % len;
            }
        }
        */
        return starterWord;
    }
}
