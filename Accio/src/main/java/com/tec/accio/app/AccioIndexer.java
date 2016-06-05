package com.tec.accio.app;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Created by metrofunko on 04/06/16.
 */
public class AccioIndexer {

    List<String> stopwords = Arrays.asList("a");

    Map<String, List<Tuple>> index = new HashMap<String, List<Tuple>>();
    List<String> files = new ArrayList<String>();

    public void indexFile(File file) throws IOException {
        int fileno = files.indexOf(file.getPath());
        if (fileno == -1) {
            files.add(file.getPath());
            fileno = files.size() - 1;
        }

        int pos = 0;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        for (String line = reader.readLine(); line != null; line = reader
                .readLine()) {
            for (String _word : line.split("\\W+")) {
                String word = _word.toLowerCase();
                pos++;
                if (stopwords.contains(word))
                    continue;
                List<Tuple> idx = index.get(word);
                if (idx == null) {
                    idx = new LinkedList<Tuple>();
                    index.put(word, idx);
                }
                idx.add(new Tuple(fileno, pos));
            }
        }
        System.out.println("indexed " + file.getPath() + " " + pos + " words");
    }

    public void search(List<String> words) {
        for (String _word : words) {
            Set<String> answer = new HashSet<String>();
            String word = _word.toLowerCase();
            List<Tuple> idx = index.get(word);
            if (idx != null) {
                for (Tuple t : idx) {
                    answer.add(files.get(t.fileno));
                }
            }
            System.out.print(word);
            for (String f : answer) {
                System.out.print(" " + f);
            }
            System.out.println("");
        }
    }
 /*
    public static void main(String[] args) {
        try {
            AccioIndexer idx = new AccioIndexer();
            idx.indexFile(new File("/home/zeru/IdeaProjects/Accio/Accio/filesToIndex/quack.txt"));

            idx.search(Arrays.asList("/home/zeru/IdeaProjects/Accio/Accio/filesToIndex/quack.txt".split(",")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    private class Tuple {
        private int fileno;
        private int position;

        public Tuple(int fileno, int position) {
            this.fileno = fileno;
            this.position = position;
        }
    }



}
