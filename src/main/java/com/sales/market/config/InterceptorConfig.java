package com.sales.market.config;

import com.sales.market.interceptors.ItemInventoryEntryInterceptor;
import com.sales.market.repository.ItemInventoryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    ItemInventoryEntryRepository itemInventoryEntryRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Interceptor solo para movimientos del inventario
        registry.addInterceptor(new ItemInventoryEntryInterceptor(itemInventoryEntryRepository))
                .addPathPatterns("/iteminventoryentry");
    }
}
