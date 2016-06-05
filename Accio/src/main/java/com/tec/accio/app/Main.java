package com.tec.accio.app;



/**
 * Created by metrofunko on 28/05/16.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        AccioIndexer accio = new AccioIndexer();
        accio.read_documents_array("/home/metrofunko/Desktop/Compiladores-FunctionalCare/Accio/filesToIndex");
        accio.frequency_generator();
        accio.matrix_generator();
        accio.printMatrix();
    }

}
