package org.aniwoh.myspringbootblogapi.controller;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.Repository.ReadingProcessRepository;
import org.aniwoh.myspringbootblogapi.entity.ReadingProcess;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ReadingProgressWebSocketHandler extends TextWebSocketHandler {

    // 存储用户的 WebSocket 会话
    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Resource
    private ReadingProcessRepository readingProcessRepository; // MongoDB repository

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 连接建立后保存会话
        log.info("新建了websocket连接{}", session.getId());
        sessions.put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 解析客户端发送的进度数据
        String payload = message.getPayload();
        log.info("返回值{}",payload);
        ReadingProcess progress = JSON.parseObject(payload, ReadingProcess.class);

        // 设置更新时间
        progress.setUpdateDate(LocalDateTime.now());

        // 保存到数据库
        ReadingProcess savedProcess =  readingProcessRepository.save(progress);
        session.sendMessage(new TextMessage(JSON.toJSONString(savedProcess)));
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // 连接关闭后移除会话
        sessions.remove(session.getId());
        System.out.println("Disconnected: " + session.getId());
        log.info("当前连接池{}",sessions);
    }
}

