package com.nike.utils;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用TransportClient连接Elasticsearch
 */
public class ESClient {
    private static volatile TransportClient client;
    @Value("${es.transport.address.list}")
    private String addressListString;
    @Value("${es.culster.name}")
    private String cluster;
    @Value("${es.client.transport.sniff}")
    private boolean sniff;//嗅探
    @Value("${es.shield.user}")
    private String shield;
    public static final String INDEX_NAME = "site";
    public static final String TYPE_NAME = "favorites";
    private static  Properties properties;

    //非公平锁
    private static final Lock obj = new ReentrantLock();
    private ESClient(){
        init();
    }
    static {
        InputStream resourceAsStream = ESClient.class.getClassLoader().getResourceAsStream("es.properties");

         properties = new Properties();
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void init(){
        addressListString =properties.getProperty("es.transport.address.list");
        cluster = properties.getProperty("es.culster.name");
        sniff = Boolean.parseBoolean(properties.getProperty("es.client.transport.sniff"));

//        client = TransportClient.buildTemplate();
//                .addPlugin(ShieldPlugin.class)
//                .settings(getSettings()).build();
        client = new PreBuiltTransportClient(getSettings());
        List<TransportAddress> tranList = getAddressList(addressListString);
        for (TransportAddress address: tranList){
            client.addTransportAddresses(address);
        }
    }

    private Settings getSettings(){

        Settings.Builder builder = Settings.builder();
       // Settings.Builder builder = Settings.settingsBuilder();
        builder.put("cluster.name",cluster);
        builder.put("client.transport.sniff",false);
//        builder.put("transport.address.list",addressListString);
        return builder.build();
    }

    private synchronized List<TransportAddress> getAddressList(String addressListString){
         List<TransportAddress> addressList  = new ArrayList<TransportAddress>();
         if (addressListString != null && !addressListString.trim().isEmpty())
        try {
                String[] addressStringArray = addressListString.trim().split(",");
                for (String addressString: addressStringArray){
                    String[] pair = addressString.trim().split(":");
                    String host =pair[0].trim();
                    int port =Integer.parseInt(pair[1].trim());
                    addressList.add(new InetSocketTransportAddress(InetAddress.getByName(host),port));
                }
            }catch (Exception e){
                throw  new IllegalArgumentException("transport.address.list has invalid format");
            }
         return addressList;
    }

    public static  TransportClient getEsInstanse(){
        obj.lock();
        if (client == null){
            new ESClient();
        }
        obj.unlock();
        return client;
    }
    public void setAddressListString(String addressListString) {
        this.addressListString = addressListString;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public void setSniff(boolean sniff) {
        this.sniff = sniff;
    }

    public void setShield(String shield) {
        this.shield = shield;
    }
}
