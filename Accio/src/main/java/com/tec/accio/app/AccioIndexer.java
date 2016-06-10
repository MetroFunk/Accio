package com.tec.accio.app;


import java.io.*;
import java.util.*;

/**
 * Created by metrofunko on 04/06/16.
 */
public class AccioIndexer {



    private HashMap<String, Double> term_frequency_in_collection = new HashMap<String, Double>();
    private HashMap<String, HashMap<Integer, Double>> term_frequency_in_files = new HashMap<String, HashMap<Integer, Double>>();
    private Vector<String> documents_array=new Vector<String>();
    private Vector<String> list_of_terms = new Vector<>();
    private Vector<String> documents_names=new Vector<>();
    public HashMap<String, Map<String, Double>> index = new HashMap<String, Map<String, Double>>();
    public HashMap<String, Double> index_idf = new HashMap<String, Double>();

    private double[][] matrix;


    public String readOneFile(File f){

        StringBuffer page=new StringBuffer();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String line =" ";
            while((line=br.readLine())!=null){
                page.append(line);
                page.append(" ");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return page.toString();
    }

    public  void read_documents_array(String dir){
        try{

            File f=new File(dir);
            File files[]=f.listFiles();
            System.out.println("Reading "+files.length+" files");
            String line=" ";
            for(File filePath: files){
                String fileContent = readOneFile(filePath);
                documents_names.add(filePath.toString());
                documents_array.add(fileContent);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String[] tokenize(String text){
        String terms[]=text.split("[^a-zA-Z0-9]+");
        return terms;
    }

    public void frequency_generator(){
        for(int i=0;i<documents_array.size();i++){
            String page=documents_array.get(i).toLowerCase();

            String terms[]=tokenize(page);

            for(int j=0;j<terms.length;j++){

                if(terms[j].isEmpty())continue;
                double val=1;
                if(term_frequency_in_collection.get(terms[j])!=null){
                    val = val+term_frequency_in_collection.get(terms[j]);}
                term_frequency_in_collection.put(terms[j], val);

                double val2=1;
                HashMap<Integer, Double> tempInner = new HashMap<Integer, Double>();
                if(term_frequency_in_files.get(terms[j])!=null){
                    tempInner = term_frequency_in_files.get(terms[j]);
                }

                if(tempInner.get(i)!=null){
                    val2 = val2+tempInner.get(i);
                }

                tempInner.put(i, val2);
                term_frequency_in_files.put(terms[j], tempInner);
            }
        }
        list_of_terms.addAll(term_frequency_in_collection.keySet());


    }

    public  void matrix_generator(){
        matrix = new double[documents_array.size()][list_of_terms.size()];

        for(int i=0;i<documents_array.size();i++){
            String page=documents_array.get(i).toLowerCase();
            String terms[]=tokenize(page);
            double maxTfThisDoc=-1.0;
            for(int j=0;j<terms.length;j++){
                if(terms[j].isEmpty()){
                    continue;
                }

                int ti = list_of_terms.indexOf(terms[j]);

                matrix[i][ti]++;

                if(matrix[i][ti]>=maxTfThisDoc){

                    maxTfThisDoc = matrix[i][ti];
                }
                double numdocuments_arrayWithTerm= (double)term_frequency_in_files.get(terms[j]).size();
                double DF = (double)documents_array.size() / numdocuments_arrayWithTerm;

                matrix[i][ti] = (Math.log(1 + DF)/Math.log(2)) * (1 + Math.log(matrix[i][ti])/Math.log(2)) ;

                if(index.containsKey(terms[j])){
                    index.get(terms[j]).put(documents_names.get(i),matrix[i][ti]);
                }
                else{
                    HashMap<String, Double> node = new HashMap<>();
                    node.put(documents_names.get(i),matrix[i][ti]);
                    index.put(terms[j],node);
                }

                index_idf.put(terms[j], (matrix[i][ti])/((1 + Math.log(matrix[i][ti]) / Math.log(2))));

            }
        }

    }

    public void create_binary_file() throws IOException {
        ObjectOutputStream myStream = new ObjectOutputStream(new FileOutputStream("index.bin"));
        myStream.writeObject(index);
        myStream.close();
        ObjectOutputStream myStream2 = new ObjectOutputStream(new FileOutputStream("index_idf.bin"));
        myStream2.writeObject(index_idf);
        myStream2.close();
    }



}
