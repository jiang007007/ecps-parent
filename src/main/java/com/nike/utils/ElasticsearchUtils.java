package com.nike.utils;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ElasticsearchUtils {

    public static boolean isIndexExists(TransportClient client,String idnex){
        boolean flag =false;
        IndicesExistsRequest indicesExistsRequest = new IndicesExistsRequest(idnex);
        IndicesExistsResponse indicesExistsResponse = client.admin().indices().exists(indicesExistsRequest).actionGet();
        if (indicesExistsResponse.isExists()){
            flag =true;
        }else
            flag =false;
        return flag;
    }

    /**
     *
     * @param client
     * @param indexName
     * @param type
     * @param shards  分片
     * @param replicas  幅片
     */
    public static void createIndex(TransportClient client, String indexName, String type,int shards,int replicas) {
        //创建索引
        Settings settings = Settings.builder()
                .put("index.number_of_shards", shards)
                .put("index.number_of_replicas", replicas)
                .build();
        CreateIndexResponse response = client.admin().indices().prepareCreate(indexName).setSettings(settings).execute().actionGet();
        if (response.isAcknowledged()){
            System.out.println("Create index successfully!!!");
            //设置mapping
            try {
                XContentBuilder xContentBuilder =
                        XContentFactory.jsonBuilder()
                                .startObject()
                                //这是类型
                                .startObject(type)
                                //这是属性-----------
                                .startObject("properties")
                                    .startObject("userId")
                                    .field("type", "text")
                                .endObject()
                                .startObject("webSiteName")
                                  .field("type", "text")
                                .endObject()
                                .startObject("webSiteAddr")
                                    .field("type", "text")
                                .endObject()
                                //自动补全属性--------
                                .startObject("suggestName")
                                    .field("type", "completion")
                                    .field("analyzer", "standard")
//                                    .field("flag","true")
                                .endObject()
                                //自动补全属性结束--------
                                .endObject()
                                //属性结束------
                                .endObject()
                                //类型结束-------
                                .endObject();
                PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(type).source(xContentBuilder);
                client.admin().indices().putMapping(mapping).get();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                if (client!= null){
                    client.close();
                }
            }
        }else {
            System.out.println("Fail to create index !");
        }
    }
}
