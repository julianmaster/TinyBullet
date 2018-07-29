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
import com.tinybullet.game.network.json.server.ResponseListPartiesJson;
import com.tinybullet.game.network.json.server.ResponsePartyStateJson;
import com.tinybullet.game.network.json.server.ResponsePlayerStatusPartyJson;
import com.tinybullet.game.network.json.server.ResponsePositionsPlayersPartyJson;
import com.tinybullet.game.view.GameScreen;
import com.tinybullet.game.view.MenuScreen;
import com.tinybullet.game.view.PartyScreen;

import java.util.concurrent.locks.ReentrantLock;

public class TinyBulletClient implements Disposable {

	private final TinyBullet game;
	private final MenuScreen menuScreen;
	private final PartyScreen partyScreen;
	private final GameScreen gameScreen;

	private final ReentrantLock lock = new ReentrantLock();

	private WebSocket socket;

	public TinyBulletClient(TinyBullet game) {
		this.game = game;
		this.menuScreen = game.getMenuScreen();
		this.partyScreen = game.getPartyScreen();
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
				if(packet instanceof ResponseListPartiesJson) {
					menuScreen.getLock().lock();
					menuScreen.setList(((ResponseListPartiesJson)packet).list);
					menuScreen.getLock().unlock();
				}
				else if(packet instanceof ResponsePlayerStatusPartyJson) {
					ResponsePlayerStatusPartyJson responsePlayerStatusPartyJson = (ResponsePlayerStatusPartyJson)packet;
					partyScreen.getLock().lock();
					partyScreen.setPlayers(responsePlayerStatusPartyJson.players);
					partyScreen.setReadies(responsePlayerStatusPartyJson.readies);
					partyScreen.setPlayerColor(responsePlayerStatusPartyJson.playerColor);
					partyScreen.getLock().unlock();
					if(game.getScreen() != partyScreen) {
						game.setScreen(partyScreen);
					}
				}
				else if(packet instanceof ResponsePartyStateJson) {
					gameScreen.getLock().lock();
					gameScreen.setState(((ResponsePartyStateJson)packet).partyState);
					gameScreen.getLock().unlock();
				}
				else if(packet instanceof ResponsePositionsPlayersPartyJson) {
					ResponsePositionsPlayersPartyJson responsePositionsPlayersPartyJson = (ResponsePositionsPlayersPartyJson)packet;
					gameScreen.getLock().lock();
					if(game.getScreen() != gameScreen) {
						for(int i = 0; i < responsePositionsPlayersPartyJson.playerColors.length; i++) {
							if(responsePositionsPlayersPartyJson.playerColors[i] == partyScreen.getPlayerColor()) {
								gameScreen.init(partyScreen.getPlayerColor(), responsePositionsPlayersPartyJson.positions[i]);
								break;
							}
						}
					}
					gameScreen.getLock().unlock();
					gameScreen.update(responsePositionsPlayersPartyJson.playerColors, responsePositionsPlayersPartyJson.positions);
					if(game.getScreen() != gameScreen) {
						game.setScreen(gameScreen);
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
