package com.tinybullet.game;

import com.github.czyzby.websocket.serialization.Serializer;
import com.github.czyzby.websocket.serialization.impl.JsonSerializer;
import com.tinybullet.game.network.MyJsonMessage;
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
			vertx.setTimer(5000L, id -> webSocket.close());
		}).listen(Constants.PORT);
	}

	private void handleFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
		final Object request = serializer.deserialize(frame.binaryData().getBytes());
		if(request instanceof MyJsonMessage) {
			System.out.println("Receive message: "+((MyJsonMessage) request).text);
		}

		final MyJsonMessage response = new MyJsonMessage();
		response.id = idCounter.getAndIncrement();
		response.text = "Hello client";
		vertx.setTimer(1000L, id -> webSocket.writeFinalBinaryFrame(Buffer.buffer(serializer.serialize(response))));
	}

	public static void main (String[] arg) throws Exception {
		new TinyBulletServer().launch();
	}
}
