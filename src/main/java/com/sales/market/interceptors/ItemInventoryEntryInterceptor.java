package com.sales.market.interceptors;

import com.sales.market.repository.ItemInventoryEntryRepository;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ItemInventoryEntryInterceptor implements HandlerInterceptor {
    private final ItemInventoryEntryRepository itemInventoryEntryRepository;

    public ItemInventoryEntryInterceptor(ItemInventoryEntryRepository itemInventoryEntryRepository) {
        this.itemInventoryEntryRepository = itemInventoryEntryRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Asumir un post y extraer el id del ItemInventory desde el ItemInventoryEntry
        // Buscar el ItemInventory y preguntar si el stock está por encima o por debajo del limite
        // En cualquiera de los dos casos enviar un email
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("Interceptor-postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("Interceptor-afterCompletion");
    }
}
