package com.tec.accio.app.service;

import com.tec.accio.app.AccioIndexer;
import org.apache.lucene.queryParser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zeru on 04/06/16.
 */
public interface QueryService {
    ArrayList<String> AccioIndexerService(String myQuery) throws IOException;
    ArrayList<String> LuceneIndexerService(String myQuery) throws IOException, ClassNotFoundException, ParseException;
}
