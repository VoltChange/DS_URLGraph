package org.volt.urlgraph.utils.net;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlFetcher {

    /**
     * 根据一个域名解析html获取其页面中的相关URL
     * @param presentURL
     * @param max 返回的数组最大项数
     * @throws IOException
     */
    public static LinkedList<String> getRelatedURL(String presentURL ,int max){
        String Url="http://"+presentURL;
        //String Url=presentURL;
        LinkedList<String> results = new LinkedList<>();
        Document document = null;
        try {
            document = Jsoup.parse(new URL(Url),100);
        } catch (IOException e) {
            return new LinkedList<>();
        }
        Elements outerUrls = document.getElementsByTag("a");
        for( Element outerUrl:outerUrls)
        {
            String link=outerUrl.attr("href");
            if(URLValidator(Url,link))
            {
                results.add(link.replaceFirst("^(\\w+:\\/\\/)",""));
            }
        }
        HashSet<String> hashSet= new LinkedHashSet<>(results);
        LinkedList<String> finalResults =new LinkedList<>(hashSet);
        while(finalResults.size()>max)
        {
            finalResults.removeLast();
        }
        return finalResults;
    }
    public static LinkedList<String> getRelatedDomain(String presentDomain ,int max){
        String Url="http://"+presentDomain;
        LinkedList<String> results = new LinkedList<>();
        Document document = null;
        try {
            document = Jsoup.parse(new URL(Url),1000);
        } catch (IOException e) {
            return new LinkedList<>();
        }
        Elements outerUrls = document.getElementsByTag("a");
        for( Element outerUrl:outerUrls)
        {
            String link=outerUrl.attr("href");
            if(URLValidator(Url,link))
            {
                String domain = getDomainName(link);
                results.add(domain);
            }
        }
        HashSet<String> hashSet= new LinkedHashSet<>(results);
        LinkedList<String> finalResults =new LinkedList<>(hashSet);
        while(finalResults.size()>max)
        {
            finalResults.removeLast();
        }
        return finalResults;
    }

    /**
     * 判断外部链接是否可行
     * @param presentUrl
     * @param outerUrl
     */
    private static boolean URLValidator(String presentUrl,String outerUrl)
    {
        if(!outerUrl.matches("^https?.*"))//判断开头
            return false;
        if(fromSameOrganization(getDomainName(presentUrl),getDomainName(outerUrl)))
            return false;
        return true;
    }

    /**
     * 从URL中提取域名
     * @param Url
     */
    private static String getDomainName(String Url)
    {
        /*
        ^(\w+:\/\/) 去除头部
        (?=\/).*    去除尾部
        */
        String domainName = Url.replaceFirst("^(\\w+:\\/\\/)","").replaceAll("(?=\\/).*","");
        return domainName;
    }

    /**
     * 判断两个域名是否来自同一个组织
     * 来自同一组织的要求:域名url最右后缀至多有1个或2个相同的子域名
     * @param domainNameA
     * @param domainNameB
     */
    public static boolean fromSameOrganization(String domainNameA,String domainNameB)
    {
        // [^.\s]*\.[^.]*(?:\.[^.]*)?$
        Pattern p = Pattern.compile("[^.\\s]*\\.[^.]*(?:\\.[^.]*)?$");
        Matcher m ;
        String thirdLevelDomainA="";
        String thirdLevelDomainB="";
        //提取A的三级域名
        m=p.matcher(domainNameA);
        if(m.find())
        {
            thirdLevelDomainA = domainNameA.substring(m.start(), m.end());
        }
        //提取B的三级域名
        m=p.matcher(domainNameB);
        if(m.find())
        {
            thirdLevelDomainB = domainNameB.substring(m.start(), m.end());
        }
        if(!(thirdLevelDomainA.equals("")||thirdLevelDomainB.equals("")))
        {
            return  thirdLevelDomainA.equals(thirdLevelDomainB);
        }else {
            return false;
        }
    }
}
