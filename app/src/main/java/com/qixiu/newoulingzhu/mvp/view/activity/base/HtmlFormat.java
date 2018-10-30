package com.qixiu.newoulingzhu.mvp.view.activity.base;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlFormat {

    public static String getNewContent(String htmltext){

        Document doc= Jsoup.parse(htmltext);
        Elements elements=doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width","100%").attr("height","auto");
        }

        return doc.toString();
    }
}

//作者：lance_小超
//链接：https://www.jianshu.com/p/1d318c0d3278
//來源：简书
//简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。