package com.tec.accio.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.*;

import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.SpanishStemmer;


/*
 * @author Gustavo Gonzalez
 */
public class QueryAnalyze {
	
	/*
	 * Atributos de la clase QueryAnalyze
	 */
	private HashMap<String, Map<String, Double>> index;		// Mapa contenedor del indice de la colecion (Wij de cada doc)
	private HashMap<String, Double> idf; 					// Mapa contenedor del IDF de los términos de la colección
	private ObjectInputStream input;						// ObjectInputStream del index 
	private ObjectInputStream input2;						// ObjectInputStream del IDF de los terninos de la coleccion 
	private Stemmer stemmer;								// Objeto que me permite hacer stemming a las palabras
	private SnowballProgram languaje;						// Objeto que nos permite definir el lenguaje del stemming
	private String[] currentQuery;  						// Arreglo contenedor de los terminos de la busqueda
	private Map<String, Double> queryIDF; 					// Mapa contenedor del IDF del query actual
	private HashMap<String,Double> sim; 					// Mapa contenedor del ranking de documentos encontrados
	private List<String> files = new ArrayList<String>();	// Lista contenedora de los archivos encontrados
	private double[][] matrix;								// Matriz contenedora del IDF de la busqueda, Wij del indice
	
	/*
	 * Contructor de la clase
	 * Inicializacion de los atributos
	 */
	public QueryAnalyze() throws IOException {
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
	
	/*
	 * Metodo que toma el query y le hace tokenize, salvandolo en una lista de strings
	 */
	 private String[] tokenize(String text){
	        String terms[]=text.split("[^a-zA-Z0-9ñ]+");
	        return terms;
	 }
	
	 /*
	  * Metodo principal de la clase encargada de ejecutar las demas funciones
	  * @param query del usuario
	  * @return ranking ordenado de mayor a menor, con los documentos mas relevantes (Usando IDF) 
	  * @see LinkedHashMap
	  */
	public LinkedHashMap<String, Double> stemmedQuery(String query){
		cleanQuery();
		currentQuery =  tokenize(stemmer.analyzeQuery(query, languaje));
		calculate_Query_IDF();
		calculateDocsList();
		generate_matrix();
		//printMatrix();
		generate_sim();
		return sortHashMapByValues(sim);
	}
	
	/*
	 * Metodo que calcula el IDF de una consulta (query)
	 */
	private void calculate_Query_IDF(){
		for(String string: currentQuery){
			if (!idf.containsKey(string)){ }
			else{
				queryIDF.put(string, idf.get(string));
			}
		}
	}
	
	/*
	 * Metodo que genera la lista de documentos encontrados
	 */
	private void calculateDocsList(){
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
	
	/*
	 * Metodo generador de la matriz de principal (IDF del query + Wij de cada documento)
	 */
	private void generate_matrix(){
		matrix = new double[queryIDF.size()][files.size()+1];
		Collection<Double> values = queryIDF.values();
		Set<String> words = queryIDF.keySet();
		// Primero, inicializo el IDF del query
		for (int i = 0; i < (queryIDF.size()) ; i++){
			matrix [i] [0] = (double) values.toArray()[i];
		}
		// Segundo, genero el Wij de cada documento 
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

	/*
	 * Metodo que calcula la funcion de similitud, al mismo tiempo que la raiz de la suma de los pesos de
	 * cada documento
	 */
	private void generate_sim(){
		double queryRoot =  calculate_query_root();
		String currentFile;
		double num, root, res;
		for (int i = 0; i < (files.size()) ; i++){
			currentFile = files.get(i); 
			num = root = res = 0;
			for (int j = 0; j < (queryIDF.size()); j++) {
				// calculo de el numerador
				double temp = matrix [j][0] * matrix[j][i+1]; 
				num += temp;
				// calculo de la raiz de los pesos
				double temp2 = matrix[j][i+1] * matrix[j][i+1];
				root += temp2;
			}
			root = Math.sqrt(root);
			// si la raiz de los pesos del query o del documento es 0, no lo tomo en cuenta
			if (!(queryRoot == 0 || root ==0)){
				res = (num / (queryRoot * root) );
				sim.put(currentFile, res);
			}
		}	
	}

	/*
	 * Metodo que calcula la raiz de la suma de los pesos del query
	 * @return suma de los pesos del query
	 */
	private double calculate_query_root(){
		double res = 0;
		for (Map.Entry<String, Double> map : queryIDF.entrySet()){
			double temp = (map.getValue())*(map.getValue());
			res+= temp;
		}
		return Math.sqrt(res);
	}
	

	
	/*private void printMatrix(){
		for (int i = 0; i < matrix.length; i++) {
		    for (int j = 0; j < matrix[0].length; j++) {
		        System.out.print(matrix[i][j] + " ");
		    }
		    System.out.print("\n");
		}
	} */
	
	/*
	 * Metodo inicializador, que limpia los registros y estructuras de datos
	 * antes de ser utilizadas, esto con el fin de evitar datos basura
	 */
	private void cleanQuery(){
		currentQuery = null;
		queryIDF.clear();
		sim.clear();
		files.clear();
	}


	/*
	 * Metodo que ordena de mayor a menor el mapa contenedor del ranking de documentos, de acuerdo a su peso 
	 * @param HashMap con los documentos como llave y pesos como valor
	 * @return LinkedHashMap con sus elementos ordenados por valor
	 * @see LinkedHashMap
	 * @see HashMap
	 */
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

	public ArrayList<String> getRank (String myQuery){
		LinkedHashMap<String, Double> hash = stemmedQuery(myQuery);

		ArrayList<String> result = new ArrayList<String> ();
		for (Map.Entry<String, Double> it : hash.entrySet()){
			result.add(it.getKey());
		}

		for (String i : result){
			System.out.print(i);
		}

		return result;
	}
	/*
	public static void main (String args[]) throws FileNotFoundException, ClassNotFoundException, IOException{
		QueryAnalyze a = new QueryAnalyze();
		//System.out.println(a.stemmedQuery("potato quack gustavo")+"\n");
		//System.out.println(a.stemmedQuery("potato quak")+"\n");
		//System.out.println(a.stemmedQuery("hola mi amigo")+"\n");
		//System.out.println(a.stemmedQuery("kappa pogchamp kreygasm potato elefante")+"\n");
		//System.out.println(a.stemmedQuery("andrew fields")+"\n");
		a.getRank("potato quack gustavo");
	}*/
}
