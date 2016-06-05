package com.tec.accio.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.RegEx;

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
		
		File f=new File(inputPath);
        File files[]=f.listFiles();
        Scanner scan;
        FileWriter stemmed;
        String path_in;
        String path_out;
        
        for(File filePath: files){
        	
        	path_in = inputPath+"/"+filePath.getName(); 
        	path_out = stemmedPath+"/"+filePath.getName(); 
        	scan = new Scanner(new File(path_in));
     		stemmed = (new FileWriter (path_out));
     		
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
	}
	
	public String analyzeQuery(String query, SnowballProgram languajeStemming){
		String[] queryArray = query.split("\\s+");
		String fooReturn = "";
		for(String item : queryArray){
			if(!stopwords.contains(item)){
				item = item.replaceAll("[^0-9a-zA-Zñ]+", "");//arreglar
				languajeStemming.setCurrent(item.toLowerCase());
				languajeStemming.stem();
				fooReturn = fooReturn + languajeStemming.getCurrent()+"\n";
			}
		}
		return fooReturn;
	}
	
	/*public static void main (String [] args) throws IOException{
		Stemmer a = new Stemmer();
		a.runAnalyze("filesToIndex", "stemmed_files", new SpanishStemmer());
		//System.out.println(a.analyzeQuery("Donde esta?: ! el aguacate, el  señor_ agricultor de- aguacates", new SpanishStemmer()));
	}*/
}