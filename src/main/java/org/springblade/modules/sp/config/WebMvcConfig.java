package org.springblade.modules.sp.config;

import org.springblade.modules.sp.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
//                        "/**"
                        // 登录注册相关接口
                        "/sp/user/**",
                        //模板接口
                        "/sp/templateData/**",
                        "/sp/templateUpdate/**",
                        "/sp/exp/instances/**",
                        //插件端相关接口
                        "/sp/revitLOD/**",
                        "/sp/objects/**",
                        "/sp/revit/**",
                       "/sp/library/getRevitByName.do" ,
                               "/sp/templateUpdate/getTemplateCustiomDataList.do" ,
                               "/sp/revit/updateIsFinished.do" ,
                               "/sp/elementLibrary/updateRevitFile.do" ,
                               "/sp/revit/uploadFiles.do" ,
                               "/sp/exp/instances/addExpInstances.do" ,
                               "/sp/revitLOD/batchSave.do" ,
                               "/sp/elementLibrary/getElementLibraryWithFiles.do" ,
                               "/sp/elementLibrary/filterElementLibraryList.do" ,
                               "/sp/elementLibrary/getProperties.do" ,
                               "/sp/elementLibrary/getElementLibraryWithFiles.do" ,
                               "/sp/newTemplate/getTemplateListByStreamId.do" ,
                               "/sp/elementLibrary/getElementLibraryListByStreamId.do",
                        // Swagger相关接口
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/**",
                        "/swagger-ui.html/**",
                        "/doc.html/**"
                );
    }
}
