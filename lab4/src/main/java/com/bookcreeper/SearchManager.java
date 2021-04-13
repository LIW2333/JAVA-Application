package com.bookcreeper;

import java.io.*;
// import java.nio.file.Path;
// import java.nio.file.Paths;
import java.util.*;

import org.apache.lucene.analysis.Analyzer;
// import org.apache.lucene.analysis.TokenStream;

// import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
// import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
// import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
// import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
// import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
// import org.apache.lucene.util.Version;
// import org.eclipse.jetty.http.HttpParser.FieldState;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class SearchManager {

  // List<Document> docs = new ArrayList<Document>();
  // File f=new File("data");
  String path = "data";

  public void createIndex(List<BookModel> bookList) {
    File f = new File(path);
    IndexWriter iwr = null;
    try {
      Directory dir = FSDirectory.open(f.toPath());
      Analyzer analyzer = new IKAnalyzer();

      IndexWriterConfig conf = new IndexWriterConfig(analyzer);
      iwr = new IndexWriter(dir, conf);// 建立IndexWriter。固定套路
      for (BookModel book : bookList) {
        iwr.commit();
        Document doc = getDocument(book, dir);
        if (doc == null)
          continue;
        // iwr =
        iwr.addDocument(doc);
      }
      // iwr.addDocument(doc);//添加doc，Lucene的检索是以document为基本单位

    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      iwr.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public Document getDocument(BookModel book, Directory dir) {

    Document doc = null;
    if (search("id", book.getBookID()) == false) {
      doc = new Document();
      Field idField = new TextField("id", book.getBookID(), Field.Store.YES);
      Field nameField = new TextField("name", book.getBookName(), Field.Store.YES);
      Field authorField = new TextField("author", book.getBookAuthor(), Field.Store.YES);
      Field categoryFeild = new TextField("category", book.getBookCategory(), Field.Store.YES);
      Field publisherField = new TextField("publisher", book.getBookPublisher(), Field.Store.YES);
      Field pictureField = new TextField("picture", book.getBookPicture(), Field.Store.YES);
      Field commendField = new TextField("commend", book.getBookCommend(), Field.Store.YES);
      Field contentField = new TextField("content", book.getBookContent(), Field.Store.YES);
      Field authorInfoField = new TextField("authorinfo", book.getBookAuthorInfo(), Field.Store.YES);
      Field catalogField = new TextField("catalog", book.getBookCatalog(), Field.Store.YES);
      Field priceField = new TextField("price", book.getBookPrice(), Field.Store.YES);

      doc.add(idField);
      doc.add(nameField);
      doc.add(authorField);
      doc.add(categoryFeild);
      doc.add(publisherField);
      doc.add(pictureField);
      doc.add(commendField);
      doc.add(contentField);
      doc.add(authorInfoField);
      doc.add(catalogField);
      doc.add(priceField);
    }

    // docs.add(doc);
    return doc;

  }

  public boolean search(String attr, String str) {
    // List<String> result = new ArrayList<String>();
    File f = new File(path);
    try {
      IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(f.toPath())));

      Analyzer analyzer = new IKAnalyzer();
      // 指定field为“name”，Lucene会按照关键词搜索每个doc中的name。
      QueryParser parser = new QueryParser(attr, analyzer);

      Query query = parser.parse(str);
      int cnt = searcher.count(query);
      if (cnt <= 0)
        return false;
      TopDocs hits = searcher.search(query, cnt);// 前面几行代码也是固定套路，使用时直接改field和关键词即可

      for (ScoreDoc doc : hits.scoreDocs) {
        Document d = searcher.doc(doc.doc);
        // result.add(d.toString());
        if(attr.equals("id")) break;
        System.out.println("\033[43;34;4m"+"--------------------------------------------------------------" + "\033[0m");
        System.out.println("id: " + d.get("id"));
        System.out.println("name: " + d.get("name"));
        System.out.println("author: " + d.get("author"));
        System.out.println("category: " + d.get("category"));
        System.out.println("publisher: " + d.get("publisher"));
        System.out.println("picture: " + d.get("picture"));
        System.out.println("commend: " + d.get("commend"));
        System.out.println("content: " + d.get("content"));
        System.out.println("authorinfo: " + d.get("authorinfo"));
        System.out.println("catalog: " + d.get("catalog"));
        System.out.println("price: " + d.get("price"));

      }
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }
    return true;
  }
}
