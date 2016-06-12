package com.tec.accio.app;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by metrofunko on 11/06/16.
 */
public class Lucene_Searcher {

    public TextFileIndexer indexer;

    public Lucene_Searcher() throws IOException, ClassNotFoundException {
        ObjectInputStream  input = new ObjectInputStream(new FileInputStream("index_lucene.bin"));
        indexer = (TextFileIndexer) input.readObject();
    }

    public  ArrayList<String> searchIndex(String searchString) throws IOException, ParseException {
        System.out.println("Searching for '" + searchString + "'");
        Directory directory = FSDirectory.getDirectory(indexer.INDEX_DIRECTORY);
        IndexReader indexReader = IndexReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        Analyzer analyzer = new StandardAnalyzer();
        QueryParser queryParser = new QueryParser(indexer.FIELD_CONTENTS, analyzer);
        Query query = queryParser.parse(searchString);
        Hits hits = indexSearcher.search(query);
        System.out.println("Number of hits: " + hits.length());

        Iterator<Hit> it = hits.iterator();
        ArrayList<String> array_documents = new ArrayList<>();
        while (it.hasNext()) {
            Hit hit = it.next();
            Document document = hit.getDocument();
            array_documents.add(document.get(indexer.FIELD_PATH));
        }
       return array_documents;

    }
    public static void main(String[] args) throws Exception {
        Lucene_Searcher search = new Lucene_Searcher();
        search.searchIndex("quack");
    }



}

