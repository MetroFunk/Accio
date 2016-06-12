package com.tec.accio.app;



import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;


import java.io.*;
import java.util.Iterator;

public class TextFileIndexer implements Serializable {

  public  String FILES_TO_INDEX_DIRECTORY = "filesToIndex";
  public  String INDEX_DIRECTORY = "indexDirectory";

  public  String FIELD_PATH = "path";
  public  String FIELD_CONTENTS = "contents";





  public  void createIndex() throws CorruptIndexException, LockObtainFailedException, IOException {
    Analyzer analyzer = new StandardAnalyzer();
    boolean recreateIndexIfExists = true;
    IndexWriter indexWriter = new IndexWriter(INDEX_DIRECTORY, analyzer, recreateIndexIfExists);
    File dir = new File(FILES_TO_INDEX_DIRECTORY);
    File[] files = dir.listFiles();
    for (File file : files) {
      Document document = new Document();

      String path = file.getCanonicalPath();
      document.add(new Field(FIELD_PATH, path, Field.Store.YES, Field.Index.UN_TOKENIZED));

      Reader reader = new FileReader(file);
      document.add(new Field(FIELD_CONTENTS, reader));

      indexWriter.addDocument(document);
    }
    indexWriter.optimize();
    indexWriter.close();
  }

    public void create_binary_file() throws IOException {
        TextFileIndexer indexer  = new TextFileIndexer();
        indexer.FIELD_CONTENTS = this.FIELD_CONTENTS;
        indexer.FIELD_PATH = this.FIELD_PATH;
        indexer.FILES_TO_INDEX_DIRECTORY = this.FILES_TO_INDEX_DIRECTORY;
        indexer.INDEX_DIRECTORY = this.INDEX_DIRECTORY;
        ObjectOutputStream myStream = new ObjectOutputStream(new FileOutputStream("index_lucene.bin"));
        myStream.writeObject(indexer);
        myStream.close();
    }

    public static void main(String[] args) throws Exception {
        TextFileIndexer index = new TextFileIndexer();
        index.createIndex();
        index.create_binary_file();

    }
}
