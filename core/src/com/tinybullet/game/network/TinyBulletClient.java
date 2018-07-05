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

	public TinyBulletClient() {
		socket = ExtendedNet.getNet().newWebSocket("localhost", Constants.PORT);
		synchronized (socket) {
			socket.addListener(getListener());
			socket.connect();
		}
	}

	private WebSocketListener getListener() {
		return new AbstractWebSocketListener() {

			@Override
			public boolean onOpen(WebSocket webSocket) {
				return FULLY_HANDLED;
			}

			@Override
			protected boolean onMessage(WebSocket webSocket, Object packet) throws WebSocketException {
//				if(packet instanceof  MyJsonMessage) {
//					final MyJsonMessage jsonMessage = (MyJsonMessage) packet;
//					synchronized (message) {
//						message = jsonMessage.text + jsonMessage.id + "!";
//					}
//				}
				return FULLY_HANDLED;
			}

			@Override
			public boolean onClose(WebSocket webSocket, WebSocketCloseCode code, String reason) {
				return FULLY_HANDLED;
			}
		};
	}

	public void send(Object packet) {
		synchronized (socket) {
			if(socket.isOpen()) {
				socket.send(packet);
			}
		}
	}

	@Override
	public void dispose() {
		WebSockets.closeGracefully(socket);
	}
}
