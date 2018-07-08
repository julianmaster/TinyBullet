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
import com.tinybullet.game.model.Player;
import com.tinybullet.game.network.json.PartyStateJson;
import com.tinybullet.game.network.json.PlayerInfoJson;
import com.tinybullet.game.view.MainScreen;

public class TinyBulletClient implements Disposable {

	private final MainScreen screen;
	private WebSocket socket;

	public TinyBulletClient(MainScreen screen) {
		this.screen = screen;
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
				if(packet instanceof PlayerInfoJson) {
					if(screen.getState() == PartyState.INIT) {
						synchronized (screen.getPlayer()) {
							Player player = screen.getPlayer();
							player.setPosition((PlayerInfoJson)packet);
						}
					}
				}
				else if(packet instanceof PartyStateJson) {
					synchronized (screen.getState()) {
						screen.setState(((PartyStateJson)packet).partyState);
					}
				}
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
