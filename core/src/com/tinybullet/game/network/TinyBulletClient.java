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
import com.tinybullet.game.TinyBullet;
import com.tinybullet.game.model.Player;
import com.tinybullet.game.network.json.server.ResponseJoinPartyJson;
import com.tinybullet.game.network.json.server.ListPartiesJson;
import com.tinybullet.game.network.json.server.PartyStateJson;
import com.tinybullet.game.view.GameScreen;
import com.tinybullet.game.view.MenuScreen;

import java.util.concurrent.locks.ReentrantLock;

public class TinyBulletClient implements Disposable {

	private final TinyBullet game;
	private final MenuScreen menuScreen;
	private final GameScreen gameScreen;

	private final ReentrantLock lock = new ReentrantLock();

	private WebSocket socket;

	public TinyBulletClient(TinyBullet game) {
		this.game = game;
		this.menuScreen = game.getMenuScreen();
		this.gameScreen = game.getGameScreen();
		socket = ExtendedNet.getNet().newWebSocket(Constants.HOST, Constants.PORT);

		lock.lock();
		socket.addListener(getListener());
		socket.connect();
		lock.unlock();
	}

	private WebSocketListener getListener() {
		return new AbstractWebSocketListener() {

			@Override
			public boolean onOpen(WebSocket webSocket) {
				return FULLY_HANDLED;
			}

			@Override
			protected boolean onMessage(WebSocket webSocket, Object packet) throws WebSocketException {
				if(packet instanceof ListPartiesJson) {
					menuScreen.getLock().lock();
					menuScreen.setList(((ListPartiesJson)packet).list);
					menuScreen.getLock().unlock();
				}
				else if(packet instanceof ResponseJoinPartyJson) {
					if(gameScreen.getState() == PartyState.LOBBY) {
						gameScreen.getLock().lock();
						Player player = gameScreen.getPlayer();
						player.setPosition((ResponseJoinPartyJson) packet);
						gameScreen.setState(PartyState.WAIT_START);
						gameScreen.getLock().unlock();
						game.setScreen(gameScreen);
					}
				}
				else if(packet instanceof PartyStateJson) {
					gameScreen.getLock().lock();
					gameScreen.setState(((PartyStateJson)packet).partyState);
					gameScreen.getLock().unlock();
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
		lock.lock();
		if(socket.isOpen()) {
			socket.send(packet);
		}
		lock.unlock();
	}

	@Override
	public void dispose() {
		WebSockets.closeGracefully(socket);
	}
}
