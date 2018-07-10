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
import com.tinybullet.game.view.GameScreen;
import com.tinybullet.game.view.MenuScreen;

public class TinyBulletClient implements Disposable {

	private final MenuScreen menuScreen;
	private final GameScreen gameScreen;
	private WebSocket socket;

	public TinyBulletClient(MenuScreen menuScreen, GameScreen gameScreen) {
		this.menuScreen = menuScreen;
		this.gameScreen = gameScreen;
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
					if(gameScreen.getState() == PartyState.INIT) {
						synchronized (gameScreen.getPlayer()) {
							Player player = gameScreen.getPlayer();
							player.setPosition((PlayerInfoJson)packet);
						}
					}
				}
				else if(packet instanceof PartyStateJson) {
					synchronized (gameScreen.getState()) {
						gameScreen.setState(((PartyStateJson)packet).partyState);
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
