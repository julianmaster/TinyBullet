package com.tinybullet.game.network;

import com.badlogic.gdx.utils.Disposable;
import com.github.czyzby.websocket.AbstractWebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.net.ExtendedNet;
import com.tinybullet.game.Constants;

public class TinyBulletClient implements Disposable {

	private WebSocket socket;
	private String message = "Connecting...";

	public TinyBulletClient() {
		socket = ExtendedNet.getNet().newWebSocket("localhost", Constants.PORT);
		socket.addListener(getListener());
		socket.connect();
	}

	private WebSocketListener getListener() {
		return new AbstractWebSocketListener() {

			@Override
			public boolean onOpen(WebSocket webSocket) {
				synchronized (message) {
					message = "Connected!";
				}
				final MyJsonMessage myMessage = new MyJsonMessage();
				myMessage.text = "Hello server!";
				webSocket.send(myMessage);
				return FULLY_HANDLED;
			}

			@Override
			public boolean onClose(WebSocket webSocket, WebSocketCloseCode code, String reason) {
				synchronized (message) {
					message = "Disconnectd!";
				}
				return FULLY_HANDLED;
			}

			@Override
			protected boolean onMessage(WebSocket webSocket, Object packet) throws WebSocketException {
				if(packet instanceof  MyJsonMessage) {
					final MyJsonMessage jsonMessage = (MyJsonMessage) packet;
					synchronized (message) {
						message = jsonMessage.text + jsonMessage.id + "!";
					}
				}
				return FULLY_HANDLED;
			}
		};
	}

	public String getMessage() {
		return message;
	}

	@Override
	public void dispose() {
		WebSockets.closeGracefully(socket);
	}
}
