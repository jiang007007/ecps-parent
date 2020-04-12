package com.nike.listener;

import com.nike.utils.ESClient;
import com.nike.utils.ElasticsearchUtils;
import com.nike.utils.FreeMarkerUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;

/**
 * 项目启动时初始化监听器
 */
@WebListener("/*")
public class ProjectListener implements ServletContextListener {

    /**
     *  当项目启动的时间，我们就根据模板生成我们的index页面
     *  与Elasticsearch连接  如果index不存在则创建 index（Table）
     * @param sce
     */
    public void contextInitialized(ServletContextEvent sce) {
        try {
            FreeMarkerUtils freeMarkerUtils = new FreeMarkerUtils();
        //    freeMarkerUtils.outputFile("index.ftl","index.html",null);
            TransportClient esInstanse = ESClient.getEsInstanse();
            System.out.println(esInstanse);
            if (!ElasticsearchUtils.isIndexExists(esInstanse,ESClient.INDEX_NAME)){
                ElasticsearchUtils.createIndex(esInstanse,ESClient.INDEX_NAME,ESClient.TYPE_NAME,1,0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }

}
