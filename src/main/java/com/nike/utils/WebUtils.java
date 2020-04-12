package com.nike.utils;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 1  往浏览器输出中文数据
 * 2  校验码是否正确
 * 3  设置属性到session中
 */
public class WebUtils {
    /**
     * 向浏览器输出JSON数据
     * @param result
     * @param response
     */
    public static void printCNJSON(String result, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.write(result);
            writer.flush();
            writer.close();
        }catch (IOException var){
            var.printStackTrace();
        }
    }

    /**
     *  检验验证码是否正确
     * @param request
     * @return
     */
    public static boolean validateCaptcha(HttpServletRequest request){
        //在这里进行验证码的校验
        String valiadateCode = (String) request.getSession().getAttribute("captcha");
        String randomcode= request.getParameter("captcha");
        if (randomcode != null && valiadateCode != null && randomcode.equalsIgnoreCase(valiadateCode)){
            return true;
        }else
            return false;
    }

    /**
     * 针对Elasticsearch string转json
     * @param strings
     * @return
     */

    public static String string2JSON(String... strings) throws IOException {
        String suggestName =strings[2];

        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .field("userId", strings[0])
                .field("webSiteAddr", strings[1])
                .field("webSiteName", strings[2])
                //自动补全
                .field("suggestName", strings[2])
                .endObject();
        return builder.toString();
    }
}
