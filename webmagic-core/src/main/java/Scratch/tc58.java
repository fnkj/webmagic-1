package Scratch;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import Model.AssortDocument;
import Model.CurrenModel;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.UrlUtils;

public class tc58 extends CurrenModel implements PageProcessor {

	private static final String URL_LIST = "http://luzhou.58.com/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

	private static final String Domain ="luzhou.58.com";
		
	private static final String Directory ="C:/Users/WangJing/Desktop/aaaaaaaaaaaa/";

	public tc58() {
		super("assort");
		// TODO Auto-generated constructor stub
	}

	    private Site site = Site
	            .me()
	            .setDomain("http://luzhou.58.com")
	            .setRetryTimes(3)
	            .setSleepTime(10000)
	            .setUserAgent(
	                    "Mozilla/5.0");


	    @Override
	    public void process(Page page) {
	    	System.out.println("pageurl="+page.getUrl());
	      
	    	//列表页 两种前端页面
	    	page.addTargetRequests(page.getHtml().xpath("//*[@id=\"list_con\"]/li").links().regex(URL_LIST).all(),10);
	            page.addTargetRequests(page.getHtml().xpath("//*[@id=\"infolist\"]/dl/dt").links().regex(URL_LIST).all(),10);

              //文章页
	            page.putField("title", page.getHtml().xpath("//*[@class=\"pos_title\"]/outerHtml()").toString());
	            page.putField("content1", page.getHtml().xpath("//*[@class=\"subitem_con company_baseInfo\"]/outerHtml()").toString());	       	 
	            page.putField("content2", page.getHtml().xpath("//*[@class=\"item_con pos_info\"]/outerHtml()").toString());
	                 //    page.putField("date",
	          //                      page.getHtml().xpath("//em[contains(@id,'authorposton')]/text()").toString().substring(3,page.getHtml().xpath("//em[contains(@id,'authorposton')]/text()").toString().length()));

	            try {
				
                AssortDocument assortDocument=new AssortDocument();
	            
                String title=page.getHtml().xpath("//*[@class=\"pos_title\"]/outerHtml()").toString();
             
             if(title==null){
                	page.setSkip(true);
               }else{
	            assortDocument.setTitle(title);
	            assortDocument.setDate(new Date());
	            assortDocument.setPath(Directory+Domain+"/"+DigestUtils.md5Hex(page.getUrl().get())+".html");
	            page.getUrl();
	            collection.insertOne(assortDocument.getDocument());
	                    System.out.println("文档插入成功");  
                }  	
			} catch (Exception e) {
					// TODO: handle exception
					System.err.println( e.getClass().getName() + ": " + e.getMessage() ); 
			}
				
	    }


	    @Override
	    public Site getSite() {
	    	site.setDomain(Domain);
	        return site;
	    }


	    public void start(String url) {
	    	 Spider.create(new tc58()).addUrl(url)
		        .addPipeline(new FilePipeline("C:\\Users\\WangJing\\Desktop\\aaaaaaaaaaaa")).thread(3).start();
		}
	    
	    public static void main(String[] args) {
	        Spider.create(new tc58()).addUrl("http://luzhou.58.com/zplvyoujiudian/")
	        .addPipeline(new FilePipeline(Directory)).thread(3).start();
	    }
}
