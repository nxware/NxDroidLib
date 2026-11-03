package net.bsnx.jlib.text;

import net.bsnx.jlib.collections.Collections;

import java.util.ArrayList;
import java.util.List;

public class BinaryClassificator {
	
	public static void main(String[] args) {
		BinaryClassificator bc = new BinaryClassificator("Old", "New");
		bc.addSampleToA("Top", "Gun");
		bc.addSampleToA("Shy", "People");
		bc.addSampleToA("Top", "Hat");
		bc.addSampleToB("Top", "Gear");
		bc.addSampleToB("Shy", "Guy");
		bc.calc();
		System.out.println("P(Old)       = " + bc.probabilityOfA());
		System.out.println("P(New)       = " + bc.probabilityOfB());
		System.out.println();
		System.out.println("P('Top'|Old) = " + bc.probabilityOfWordGivenA("Top"));
		System.out.println("P(Old|'Top') = " + bc.probabilityOfWordInA("Top")); 
	}

	private String labelA;
	private String labelB;
	
	/**
	 * Laplace Smoothing k
	 */
	private int k;

	private List<List<String>> samplesA = new ArrayList<List<String>>();
	private List<List<String>> samplesB = new ArrayList<List<String>>(); 
	private List<String> wordsA = new ArrayList<String>();
	private List<String> wordsB = new ArrayList<String>();
	
	public BinaryClassificator(String labelA, String labelB) {
		this.labelA = labelA;
		this.labelB = labelB;
	}
	
	private void calc() {
		wordsA.clear();
		for (List<String> sample_words : samplesA) {
			wordsA.addAll(sample_words);
		}
		wordsB.clear();
		for (List<String> sample_words : samplesB) {
			wordsB.addAll(sample_words);
		}
		
	}
	
	public void addSampleToA(String...words) {
		samplesA.add(Collections.toList(words));
	}
	
	public void addSampleToB(String...words) {
		samplesB.add(Collections.toList(words));
	}
	
	public int getSampleCount() {
		return samplesA.size() + samplesB.size();
	}
	
	/**
	 * Mit Laplace Smoothing
	 * @return
	 */
	public double probabilityOfA() {
		return ((double) samplesA.size() + 1 ) / (getSampleCount()+2);
	}
	
	public double probabilityOfB() {
		return ((double) samplesB.size() + 1 ) / (getSampleCount()+2);
	}
	
	public double probabilityOfWordGivenA(String word) {
		double count_in_class = countInA(word);
		double words_in_class = wordsA.size();
		double dictionary_size = getDistinctWordCount(); 
		return  (count_in_class + k) / (words_in_class + dictionary_size);
	}
	
	public double probabilityOfWordGivenB(String word) {
		double count_in_class = countInB(word);
		double words_in_class = wordsB.size();
		double dictionary_size = getDistinctWordCount(); 
		return  (count_in_class + k) / (words_in_class + dictionary_size);
	}
	
	/**
	 * P(A|w)
	 * 
	 * @param word
	 * @return
	 */
	public double probabilityOfWordInA(String word) {
		double w_A = probabilityOfWordGivenA(word);
		double A = probabilityOfA();
		double w_B = probabilityOfWordGivenB(word);
		double B = probabilityOfB();
		return (w_A * A) / (w_A * A + w_B * B);
	}
	
	/**
	 * P(B|w)
	 * 
	 * @param word
	 * @return
	 */
	public double probabilityOfWordInB(String word) {
		double w_A = probabilityOfWordGivenA(word);
		double A = probabilityOfA();
		double w_B = probabilityOfWordGivenB(word);
		double B = probabilityOfB();
		return (w_B * B) / (w_A * A + w_B * B);
	}
	
	public int getDistinctWordCount() {
		WordCountList wc = new WordCountList();
		for (String s : wordsA) {
			wc.add(s);
		}
		for (String s : wordsB) {
			wc.add(s);
		}
		return wc.size();
	}
	
	public int countInA(String word) {
		int res = 0;
		for (String s : wordsA) {
			if (s.equals(word)) res++;
		}
		return res;
	}
	
	public int countInB(String word) {
		int res = 0;
		for (String s : wordsB) {
			if (s.equals(word)) res++;
		}
		return res;
	}

	@Override
	public String toString() {
		return "BinaryClassificator [labelA=" + labelA + ", labelB=" + labelB+ "]";
	}
	
}