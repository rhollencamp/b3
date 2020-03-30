package net.thewaffleshop.b3.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import net.thewaffleshop.b3.game.Engine;

@Component
public class PingWebSocketHandler extends TextWebSocketHandler {

    private final Logger log = LoggerFactory.getLogger(PingWebSocketHandler.class);

    @Autowired
    private Engine engine;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (message.getPayload().contains("U")) {
            engine.setPlayerVy(-1);
        } else if (message.getPayload().contains("D")) {
            engine.setPlayerVy(1);
        } else {
            engine.setPlayerVy(0);
        }

        if (message.getPayload().contains("L")) {
            engine.setPlayerVx(-1);
        } else if (message.getPayload().contains("R")) {
            engine.setPlayerVx(1);
        } else {
            engine.setPlayerVx(0);
        }

        session.sendMessage(new TextMessage("{\"x\":" + engine.getPlayerX() + ",\"y\":" + engine.getPlayerY() + "}"));
    }
}
