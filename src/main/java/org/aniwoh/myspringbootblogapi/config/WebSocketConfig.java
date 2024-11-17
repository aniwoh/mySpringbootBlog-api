package org.aniwoh.myspringbootblogapi.config;

import jakarta.annotation.Resource;
import org.aniwoh.myspringbootblogapi.controller.ReadingProgressWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MyWebSocketHandler myWebSocketHandler;
    @Autowired
    private ReadingProgressWebSocketHandler readingProgressWebSocketHandler;

    public WebSocketConfig(MyWebSocketHandler myWebSocketHandler) {
        this.myWebSocketHandler = myWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册 WebSocket 的路径
        registry.addHandler(readingProgressWebSocketHandler, "/websocket")
                .setAllowedOrigins("*"); // 允许跨域
    }
}
