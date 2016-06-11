package com.tec.accio.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.SpanishStemmer;

public class QueryAnalyze {
	
	public HashMap<String, Map<String, Double>> index;
	public HashMap<String, Double> idf;
	public ObjectInputStream input;
	public ObjectInputStream input2;
	public Stemmer stemmer;
	public SnowballProgram languaje;
	public String[] currentQuery;
	public Map<String, Double> queryIDF;
	public HashMap<String,Double> sim;
	public List<String> files = new ArrayList<String>();
	private double[][] matrix;
	
	protected QueryAnalyze() throws IOException {
		index = new HashMap<String, Map<String, Double>>();
		sim = new HashMap<String, Double>();
		idf = new HashMap<String, Double>();
		queryIDF = new HashMap<String, Double>();
		stemmer = new Stemmer(); 
		languaje = new SpanishStemmer();
		input = new ObjectInputStream(new FileInputStream("index.bin"));
		input2 = new ObjectInputStream(new FileInputStream("index_idf.bin"));
		try {
			index = (HashMap<String, Map<String, Double>>) input.readObject();
			idf = (HashMap<String, Double>) input2.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			input.close();
			input2.close();
		}
	}
	
	 private String[] tokenize(String text){
	        String terms[]=text.split("[^a-zA-Z0-9ñ]+");
	        return terms;
	 }
	
	public void stemmedQuery(String query){
		currentQuery =  tokenize(stemmer.analyzeQuery(query, languaje));
	}
	
	public void calculate_Query_IDF(){
		for(String string: currentQuery){
			if (!idf.containsKey(string)){ }
			else{
				queryIDF.put(string, idf.get(string));
			}
		}
	}
	
	public void calculateDocsList(){
		Map<String, Double> n;
		for (Map.Entry<String, Map <String, Double>> i : index.entrySet()){
			 n = i.getValue();
			for (Map.Entry<String, Double> j : n.entrySet() ){
				if (!files.contains(j.getKey())){
					files.add(j.getKey());
				}
			}
		}
	}
	
	public void generate_matrix(){
		matrix = new double[queryIDF.size()][files.size()+1];
		Collection<Double> values = queryIDF.values();
		Set<String> words = queryIDF.keySet();
		for (int i = 0; i < (queryIDF.size()) ; i++){
			matrix [i] [0] = (double) values.toArray()[i];
		}
		for (int i = 0; i < (queryIDF.size()); i++){
			String currentWord = (String) words.toArray()[i];
			for (int j = 1; j <= (files.size()) ; j++){
				String actualFile = files.get(j-1);
				Map<String, Double> currentMap = index.get(currentWord);

				for (Map.Entry<String, Double> iterat : currentMap.entrySet()){
					if (iterat.getKey() == actualFile){
						matrix [i] [j] = iterat.getValue();
					}	
				}
			}
		}
	}

	public void generate_sim(){
		double queryRoot =  calculate_query_root();
		String currentFile;
		double num, root, res;
		for (int i = 0; i < (files.size()) ; i++){
			currentFile = files.get(i); 
			num = root = res = 0;
			for (int j = 0; j < (queryIDF.size()); j++) {
				double temp = matrix [j][0] * matrix[j][i+1]; 
				double temp2 = matrix[j][i+1] * matrix[j][i+1];
				num += temp;
				root += temp2;
			}
			root = Math.sqrt(root);
			if (queryRoot == 0 || root ==0 ){
				sim.put(currentFile, 0.0);
			}
			else{
				res = (num / (queryRoot * root) );
				sim.put(currentFile, res);
			}
		}	
	}

	private double calculate_query_root(){
		double res = 0;
		for (Map.Entry<String, Double> map : queryIDF.entrySet()){
			double temp = (map.getValue())*(map.getValue());
			res+= temp;
		}
		return Math.sqrt(res);
	}
	

	
	public void printMatrix(){
		for (int i = 0; i < matrix.length; i++) {
		    for (int j = 0; j < matrix[0].length; j++) {
		        System.out.print(matrix[i][j] + " ");
		    }
		    System.out.print("\n");
		}
	} 
	
	public void cleanQuery(){
	/*
	 * falta
	 */
	}


	private static LinkedHashMap<String, Double> sortHashMapByValues(HashMap<String, Double> passedMap) {
		List<String> mapKeys = new ArrayList<>(passedMap.keySet());
		List<Double> mapValues = new ArrayList<>(passedMap.values());
		Collections.sort(mapValues,Collections.<Double>reverseOrder());
        Collections.sort(mapKeys, Collections.<String>reverseOrder());
		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
		Iterator<Double> valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Double val = valueIt.next();
			Iterator<String> keyIt = mapKeys.iterator();
			while (keyIt.hasNext()) {
				String key = keyIt.next();
				Double comp1 = passedMap.get(key);
				Double comp2 = val;
				if (comp1.equals(comp2)) {
					keyIt.remove();
					sortedMap.put(key, val);
					break;
				}
			}
		}
		return sortedMap;
	}



	
	public static void main (String args[]) throws FileNotFoundException, ClassNotFoundException, IOException{
		QueryAnalyze a = new QueryAnalyze();
		a.stemmedQuery("potato quack gustavo");
		a.calculate_Query_IDF();
		a.calculateDocsList();
		a.generate_matrix();
		a.printMatrix();
		a.generate_sim();
		System.out.println(sortHashMapByValues(a.sim));
		
	}
}
