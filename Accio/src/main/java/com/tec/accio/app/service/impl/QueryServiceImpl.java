package com.tec.accio.app.service.impl;

import com.tec.accio.app.AccioIndexer;
import com.tec.accio.app.Lucene_Searcher;
import com.tec.accio.app.QueryAnalyze;
import com.tec.accio.app.service.QueryService;
import org.apache.lucene.queryParser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by zeru on 04/06/16.
 */
public class QueryServiceImpl implements QueryService {
    @Override
    public ArrayList<String> AccioIndexerService(String myQuery) throws IOException {
        QueryAnalyze a = new QueryAnalyze();
        return a.getRank(myQuery);
    }

    @Override
    public ArrayList<String> LuceneIndexerService(String myQuery) throws IOException, ClassNotFoundException, ParseException {
        Lucene_Searcher a = new Lucene_Searcher();
        return a.searchIndex(myQuery);
    }


}
