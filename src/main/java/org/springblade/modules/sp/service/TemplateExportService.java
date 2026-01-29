package org.springblade.modules.sp.service;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

@Service
public class TemplateExportService {

    private final Configuration freemarkerConfig;

    public TemplateExportService() throws IOException, URISyntaxException {
        // 初始化 FreeMarker 配置
        this.freemarkerConfig = new Configuration(Configuration.VERSION_2_3_32);

        // 使用 ClassTemplateLoader 从类路径加载模板
        this.freemarkerConfig.setTemplateLoader(new ClassTemplateLoader(getClass(), "/templates"));

        this.freemarkerConfig.setDefaultEncoding("UTF-8");
    }


    /**
     * 导出数据为文件
     *
     * @param templateName 模板文件名（需存放在 resources/templates 下）
     * @param dataModel    数据模型（key-value 形式，供模板渲染使用）
     * @param outputFileName 导出的文件名
     * @return 生成的文件
     * @throws Exception 导出异常
     */
    public File exportToFile(String templateName, Map<String, Object> dataModel, String outputFileName) throws Exception {
        // 加载模板
        Template template = freemarkerConfig.getTemplate(templateName);

        // 创建临时文件
        File outputFile = File.createTempFile(outputFileName, ".xls");

        // 渲染模板并写入文件
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8")) {
            template.process(dataModel, writer);
        }

        return outputFile;
    }
}
