package org.aniwoh.myspringbootblogapi.config;

import jakarta.annotation.Resource;
import org.aniwoh.myspringbootblogapi.controller.ReadingProgressWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private ReadingProgressWebSocketHandler readingProgressWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册 WebSocket 的路径
        registry.addHandler(readingProgressWebSocketHandler, "/websocket")
                .setAllowedOrigins("*"); // 允许跨域
    }
}
