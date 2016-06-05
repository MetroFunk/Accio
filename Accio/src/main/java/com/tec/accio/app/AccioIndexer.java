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
    private Vector<String> list_of_terms = new Vector<String>();


    public double[][] matrix;

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
            System.out.println("Reading"+files.length+" files");
            String line=" ";
            for(File filePath: files){
                String fileContent = readOneFile(filePath);
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
                if(term_frequency_in_collection.get(terms[j])!=null)
                    val = val+term_frequency_in_collection.get(terms[j]);
                term_frequency_in_collection.put(terms[j], val);

                double val2=1;
                HashMap<Integer, Double> tempInner = new HashMap<Integer, Double>();
                if(term_frequency_in_files.get(terms[j])!=null)
                    tempInner = term_frequency_in_files.get(terms[j]);

                if(tempInner.get(i)!=null)
                    val2 = val2+tempInner.get(i);

                tempInner.put(i, val2);
                term_frequency_in_files.put(terms[j], tempInner);
            }
        }
        list_of_terms.addAll(term_frequency_in_collection.keySet());
        System.out.println(term_frequency_in_collection);
    }

    public  void matrix_generator(){
        matrix = new double[documents_array.size()][list_of_terms.size()];

        for(int i=0;i<documents_array.size();i++){
            String page=documents_array.get(i).toLowerCase();
            String terms[]=tokenize(page);

            double maxTfThisDoc=-1.0;
            for(int j=0;j<terms.length;j++){
                if(terms[j].isEmpty())continue;

                int ti = list_of_terms.indexOf(terms[j]);
                matrix[i][ti]++;

                if(matrix[i][ti]>=maxTfThisDoc)
                    maxTfThisDoc = matrix[i][ti];
            }
            for(int j=0;j<terms.length;j++){
                if(terms[j].isEmpty())continue;

                int ti = list_of_terms.indexOf(terms[j]);
                matrix[i][ti] = matrix[i][ti]/(maxTfThisDoc*terms.length);

                double numdocuments_arrayWithTerm= (double)term_frequency_in_files.get(terms[j]).size();
                double DF = (double)documents_array.size() / numdocuments_arrayWithTerm;

                matrix[i][ti] = matrix[i][ti] * Math.log(1 + DF);
            }
        }

    }
    public void printMatrix(){
        for(int j=0;j<list_of_terms.size();j++){
            System.out.print("  "+list_of_terms.get(j));
        }
        System.out.println();
        for(int i=0;i<documents_array.size();i++){
            System.out.print(i);
            for(int j=0;j<list_of_terms.size();j++){
                System.out.print(" "+matrix[i][j]);
            }
            System.out.println();
        }
    }

}
