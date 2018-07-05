package com.tinybullet.game;

import com.github.czyzby.websocket.serialization.Serializer;
import com.github.czyzby.websocket.serialization.impl.JsonSerializer;
import com.tinybullet.game.network.MyJsonMessage;
import com.tinybullet.game.network.PlayerPosition;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

import java.util.concurrent.atomic.AtomicInteger;

public class TinyBulletServer {
	private final Vertx vertx = Vertx.vertx();
	private final AtomicInteger idCounter = new AtomicInteger();
	private final Serializer serializer = new JsonSerializer();

	private void launch() {
		System.out.println("Launching web socket server...");
		final HttpServer server = vertx.createHttpServer();
		server.websocketHandler(webSocket -> {
			webSocket.frameHandler(frame -> handleFrame(webSocket, frame));
		}).listen(Constants.PORT);
	}

	private void handleFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
		final Object request = serializer.deserialize(frame.binaryData().getBytes());
		if(request instanceof PlayerPosition) {
			PlayerPosition pp = (PlayerPosition)request;
			System.out.println("Position: "+pp.x+" - "+pp.y);
		}
	}

	public static void main (String[] arg) throws Exception {
		new TinyBulletServer().launch();
	}
}
