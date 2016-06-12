package com.tec.accio.app;

import org.tartarus.snowball.ext.SpanishStemmer;

/**
 * Created by metrofunko on 28/05/16.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        AccioIndexer accio = new AccioIndexer();
        Stemmer stemmer = new Stemmer();
        stemmer.runAnalyze("filesToIndex", "stemmed_files", new SpanishStemmer());
        accio.read_documents_array("stemmed_files");
        accio.frequency_generator();
        accio.matrix_generator();
        accio.create_binary_file();
        TextFileIndexer a = new TextFileIndexer();
        a.createIndex();
        a.create_binary_file();
    }
}