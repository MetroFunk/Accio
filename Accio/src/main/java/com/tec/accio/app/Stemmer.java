package com.tec.accio.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.SpanishStemmer;

public class Stemmer {
	
	List <String> stopwords;
	
	protected Stemmer() throws IOException{
		stopwords = new ArrayList<>();
		 BufferedReader br = new BufferedReader(new FileReader("stopwords_es.txt"));
	        try {
	            String linea;
	            while((linea = br.readLine()) != null){
	            	stopwords.add(linea);
	            }
	        } finally {
	            br.close();
	        }
	}
	
	
	public void runAnalyze(String inputPath, String stemmedPath, SnowballProgram languajeStemming) throws IOException{
		Scanner scan = new Scanner(new File(inputPath));
		FileWriter stemmed = (new FileWriter (stemmedPath));
	    try {
	        String words ="";
	        while(scan.hasNext()) {
	        	words = scan.next();
	        		if(!stopwords.contains(words)){
	        			languajeStemming.setCurrent(words.toLowerCase());
	    	        	languajeStemming.stem();
						stemmed.write(languajeStemming.getCurrent()+"\n");	
	        		}
	        }
	    } finally {
	    	scan.close();
	        stemmed.close();
	    }
	}
	
	public static void main (String [] args) throws IOException{
		Stemmer a = new Stemmer();
		a.runAnalyze("D:/RI/Accio/Accio/filesToIndex/quack.txt", "D:/RI/Accio/Accio/filesToIndex/quack2.txt", new SpanishStemmer());
	}
}