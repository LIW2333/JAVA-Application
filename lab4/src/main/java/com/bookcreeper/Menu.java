package com.bookcreeper;

import java.io.IOException;
import java.util.*;

public class Menu {
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    // List<String> result = new ArrayList<>();
    System.out.println("\033[32;1m"+"----------Welcome to java creeper----------"+"\033[0m");
    System.out.println("\033[32;1m"+"----------Start to get data!----------"+"\033[0m");
    List<String> urlList = new ArrayList<>();
    // 测试使用的部分图书信息
    String url = "https://list.jd.com/list.html?cat=1713,3258,3297";
    urlList.add(url);
    url = "https://list.jd.com/list.html?cat=1713,3273,3544";
    urlList.add(url);
    url = "https://list.jd.com/list.html?cat=1713,3258,3320";
    urlList.add(url);
    
    try {
      Parser ps = new Parser("");
      for(String bookurl:urlList){
        ps.setUrl(bookurl);
        ps.parser(bookurl);
      }
      SearchManager mng = new SearchManager();
      // mng.getDocument(ps.bookList);
      mng.createIndex(ps.bookList);
      System.out.println("\033[35;1m" + "----------欢迎来到图书搜索系统----------" + "\033[0m");
      int isRun = 1;
      while(isRun == 1){
        System.out.println("\033[35;1m" + "输入查找的属性序号：" + "\033[0m");
      System.out.println("\033[35;1m" + "1 书名  2 作者  3 出版社  4 分类  0 退出系统" + "\033[0m");
      int state = scan.nextInt();
      scan.nextLine();
      if(state == 0){
        System.out.println("\033[31;1m" + "退出系统！"+"\033[0m");
        isRun = 0;
      }else{
        System.out.println("\033[35;1m" + "输入属性值：" + "\033[0m");
      String key = scan.nextLine();
      if (state == 1) {
        mng.search("name", key);
      } else if (state == 2) {
        mng.search("author", key);
      } else if (state == 3) {
        mng.search("publisher", key);
      } else if(state == 4){
        mng.search("category", key);
      } else {
        System.out.println("\033[31;1m" + "错误输入！"+"\033[0m");
      }
      }
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      scan.close();
    }

  }
}
