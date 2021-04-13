package com.bookcreeper;

import java.io.*;
import java.util.*;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import net.sf.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {

  String htmlUrl = "";
  String nextPageUrl = "";
  // List<String> hrefList = new ArrayList<String>();
  List<BookModel> bookList = new ArrayList<BookModel>();
  int pageCnt = 1;

  public Parser(String htmlUrl) {
    this.htmlUrl = htmlUrl;
  }

  public void setUrl(String url){
    htmlUrl = url;
  }

  public String getNextPageUrl() {

    return this.nextPageUrl;
  }

  public void parser(String htmlUrl) throws IOException {

  List<String> hrefList = new ArrayList<String>() ;
	Document doc = getPageFromUrl(htmlUrl);
	if(doc == null) return;
	hrefList = getItemUrls(doc);
	int detailCnt = 0;
    for (String string : hrefList) {
		if(detailCnt >5) break;
		++detailCnt;
	  Document conDoc = getPageFromUrl(string);
	  if(conDoc == null) continue;
      parseContent(conDoc);
    }

    // int cnt = 0;
    nextPageUrl = getNextPage(htmlUrl);
    if (nextPageUrl != "" && pageCnt < 5) {
	  // System.out.println("解析下一页------------");
      parser(nextPageUrl);
    }

  }

  public Document getPageFromUrl(String url) throws IOException {
    WebClient webClient = new WebClient(BrowserVersion.CHROME);

    // webClient.getCurrentWindow().setInnerHeight(Integer.MAX_VALUE);

    webClient.getOptions().setThrowExceptionOnScriptError(false);
    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    webClient.getOptions().setActiveXNative(false);
    webClient.getOptions().setCssEnabled(false);
    webClient.getOptions().setJavaScriptEnabled(true);
    webClient.setAjaxController(new NicelyResynchronizingAjaxController());

    HtmlPage page = null;
    try {
      page = webClient.getPage(url);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      webClient.close();
    }

	webClient.waitForBackgroundJavaScript(30000);
	if(page == null){
		return null;
	}

    String pageXml = page.asXml();
    FileOutputStream fos = new FileOutputStream("page.xml",true);
    //true表示在文件末尾追加  
    fos.write(pageXml.getBytes());  
    fos.close();
    Document document = Jsoup.parse(pageXml);// 获取html文档
    // System.out.println(document.getElementById("content").html());

    return document;
  }

  public Document getDetailFromUrl(String url) {
    String content = "";
    Document doc = null;
    try { 
      Response res = Jsoup.connect(url).header("Accept", "*/*").header("Accept-Encoding", "gzip, deflate")
          .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
          .header("Content-Type", "application/json;charset=UTF-8")
          .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
          .timeout(10000).ignoreContentType(true).execute();// .get();
      String body = res.body();
      int jsonBeginIdx = body.indexOf("{");
      if (jsonBeginIdx != 0) {    
        // doc = getPageFromUrl(url);
        // String detailContent = doc.toString();
        // int conIndex = body.indexOf("content", 0);
        int jsonEndIdx = body.lastIndexOf("}");
        body = body.substring(jsonBeginIdx, jsonEndIdx + 1);
        doc = Jsoup.parse(body);
      }
      JSONObject json = JSONObject.fromObject(body);
      content = json.getString("content");
      FileOutputStream fos = new FileOutputStream("data.json",true);
      //在文件末尾追加  
      fos.write(content.getBytes());  
      fos.close();
      doc = Jsoup.parse(content);

      // if(content.isEmpty()){
      // doc = getPageFromUrl(url);
      // String detailContent = doc.toString();
      // int conIndex = detailContent.indexOf("content", 0);
      // int conEndIndex = detailContent.lastIndexOf("\"");
      // detailContent = detailContent.substring(conIndex + 10, conEndIndex);
      // doc = Jsoup.parse(detailContent);
      // }else{
      // doc = Jsoup.parse(content);
      // }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return doc;

  }

  // public String getPriceFromUrl(String url){
  // String result = "";
  // try {
  // URL realURL = new URL(url);
  // URLConnection conn = realURL.openConnection();
  // conn.setRequestProperty("accept", "*/*");
  // conn.setRequestProperty("connection", "Keep-Alive");
  // conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64)
  // AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
  // conn.connect();
  // BufferedReader in = new BufferedReader(new
  // InputStreamReader(conn.getInputStream(), "utf-8"));
  // String line;
  // while ((line = in.readLine()) != null) {
  // result += "\n" + line;
  // }
  // } catch (IOException e) {
  // e.printStackTrace();
  // }
  // return result;
  // }

  public List<String> getItemUrls(Document doc) {
    // 获取页面
    List<String> hrefList = new ArrayList<String>();
    try{
      Element contentEl = doc.select("ul.gl-warp").first();
      Elements lis = contentEl.getElementsByTag("li");
      for (Element li : lis) {

        Element titleEl = li.select("div.p-name").first();
        Element a = titleEl.select("a").first();
        String href = a.attr("href");
        href = "http:" + href;
        hrefList.add(href);
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    return hrefList;

  }

  /**
   * 解析书本的信息
   * 
   * @param doc
   */
  public void parseContent(Document doc) {

    // Element content =
    // String doccontent = doc.toString();
    // System.out.println(doccontent);
    // OutputStream f = new OutputStream("../../file/html");
    BookModel book = new BookModel();


    try {
      Element content = doc.getElementById("parameter2");
      Elements details = content.getElementsByTag("li");
      for (Element e : details) {
        if (e.text().substring(0, 4).equals("ISBN")) {
          book.setBookID(e.text().substring(5));
          break;
        } else if (e.text().substring(0, 3).equals("出版社")) {
          book.setBookPublisher(e.getElementsByTag("a").first().text());
          e.text().substring(0, 3);
        }
        // String str = e.text().substring(0,3);
        // e.text().substring(0,3);
        // ;
      }

      content = doc.getElementById("name");
      content = content.getAllElements().first();
      String name = content.text();
      book.setBookName(name);

      content = doc.getElementsByClass("p-author").first();
      if (!content.text().isEmpty()) {
        content = content.getElementsByTag("a").first();
        book.setBookAuthor(content.text());
      }

      content = doc.getElementsByClass("crumb fl clearfix").first();
      content = content.getAllElements().first();
      Element item1 = content.getElementsByClass("item").get(2).getElementsByTag("a").first();
      Element item2 = content.getElementsByClass("item").get(4).getElementsByTag("a").first();
      book.setBookCategory(item1.text() + "/" + item2.text());

      // book.setBookPublisher(content.getElementsByClass("item").get(2).text());
      content = doc.getElementsByTag("script").get(3);
      int descIdx = content.toString().indexOf("desc: '", 0);
      descIdx = content.toString().indexOf("'", descIdx) + 1;
      int descEndIdx = content.toString().indexOf("'", descIdx);
      String detailUrl = "";
      if(descEndIdx>=0 && descIdx >=0){
        detailUrl = "https:" + content.toString().substring(descIdx, descEndIdx);
      }
      content = doc.getElementsByClass("jqzoom main-img").first();
      content = content.getElementsByTag("img").first();
      String imgUrl = content.attr("src");
      imgUrl = "https:" + imgUrl;
      book.setBookPicture(imgUrl);

      doc = getDetailFromUrl(detailUrl);
      // content = doc.getElementById("J-detail-content");
      content = doc.getElementById("detail-tag-id-2");
      content = content.getElementsByClass("book-detail-content").first();
      Elements contentList = content.getElementsByTag("p");
      book.setBookCommend(contentList.text());

      content = doc.getElementById("detail-tag-id-3");
      content = content.getElementsByClass("book-detail-content").first();
      contentList = content.getElementsByTag("p");
      book.setBookContent(contentList.text());

      content = doc.getElementById("detail-tag-id-6");
      content = content.getElementsByClass("book-detail-content").first();
      contentList = content.getElementsByTag("p");
      book.setBookCatalog(contentList.text());

      content = doc.getElementById("detail-tag-id-4");
      if (content != null ) {
        content = content.getElementsByClass("book-detail-content").first();
        content = content.getElementsByTag("p").first();
        book.setBookAuthorInfo(content.text());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    // String priceUrl = "http://p.3.cn/prices/get" + "?" + book.getBookID();
    // doc = getDetailFromUrl(priceUrl);
    // content.getElementById("jd-price");
    // book.setBookPrice(content.text());
	
    bookList.add(book);
  }

  public String getNextPage(String preUrl) {
    String pageUrl = "";
    pageCnt+=2;
    int idx = preUrl.indexOf("&");
    if(idx>0){
      preUrl = preUrl.substring(0, idx-1);
    }
    
    pageUrl = preUrl + "&page"+pageCnt;
    // Element span = doc.select("span.p-num").first();
    // Elements as = span.getElementsByTag("a");
    // for (Element a : as) {

    //   if (a.attr("class").endsWith("pn-next")) {
    //     String pageHref = a.attr("href");

    //     pageUrl = htmlUrl.substring(0, htmlUrl.lastIndexOf("/")) + pageHref;
    //     System.out.println("下一页地址：" + pageUrl);
    //   }
    // }

    return pageUrl;
  }

}