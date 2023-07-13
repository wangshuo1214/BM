package com.ws.bm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

// 配置虚拟路径
@Configuration
public class FileAllowConfig extends WebMvcConfigurationSupport {

    /*
     *addResourceHandler:访问映射路径
     *addResourceLocations:资源绝对路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/bmFile/**").addResourceLocations("file:"+BmConfig.getAvatarPath()+"/");
        super.addResourceHandlers(registry);
    }

}
