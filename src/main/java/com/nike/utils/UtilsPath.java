package com.nike.utils;

import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * 静态化路径工具类
 */
public class UtilsPath {
    /**
     * 获取到classes目录
     * return path
     */
    public static String getClassPath(){
        String systemName = System.getProperty("os.name");
        //判断当前环境，如果是Windows 要截取路劲的第一个 '/'
        if (!StringUtils.isBlank(systemName) && systemName.contains("Windows")){
            return UtilsPath.class.getResource(File.separator).getFile().substring(1);
        }else {
            return UtilsPath.class.getResource(File.separator).getFile();
        }
    }

    /**
     * 获取 root 目录
     */
    public static String getRootPath(){
        return getWeb_INF().replace("WEB-INF/","");
    }


    /**
     * 获取 web-inf目录
     */
    public static String getWeb_INF(){
        return getClassPath().replace("classes/","");
    }


    //获取模板文件夹路径
    public static String getFreePath(){
        return getWeb_INF() + "ftl";
    }
}
