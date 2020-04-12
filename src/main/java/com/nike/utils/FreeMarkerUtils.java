package com.nike.utils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.*;
import java.util.Map;
/**
 * FreeMarker工具类  生成HTML or HTML数据
 */
public class FreeMarkerUtils  implements AutoCloseable{

    /**
     *
     * @param ftlName: 模板名
     * @param fileName: 生成的html名字
     * @param map： 数据库中的数据
     */
    public void outputFile(String ftlName,String fileName,Map<String,Object> map) throws IOException, TemplateException {
        //创建fm的配置
        Configuration configuration = new Configuration();

        //指定默认编码格式
        configuration.setDefaultEncoding("UTF-8");

        //设置模板的包路径   resources/ftl
        configuration.setClassForTemplateLoading(this.getClass(),"/ftl");

        //获得包得模板
        Template template = configuration.getTemplate(ftlName);

        //定义输出流，注意得必须指定编码，输出到根项目中
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                new File(UtilsPath.getRootPath() + File.separator + fileName)
        ), "UTF-8"));
        //生成模板
        template.process(map,writer);
    }

    /**
     *
     * @param fltName 模板的名称
     * @param map     M模板中需要的数据
     * @return        返回HTML页面的内容
     */
    public String retrnText(String fltName,Map<String,Object> map) throws IOException, TemplateException {
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(this.getClass(),"/ftl");
        //获取包的模板
        Template template = configuration.getTemplate(fltName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template,map);
    }

    public void close() throws Exception {

    }
}
