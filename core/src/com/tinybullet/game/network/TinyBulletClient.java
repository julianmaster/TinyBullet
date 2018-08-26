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
import com.tinybullet.game.model.OtherPlayer;
import com.tinybullet.game.model.PartyState;
import com.tinybullet.game.model.PlayerColor;
import com.tinybullet.game.network.json.server.*;
import com.tinybullet.game.view.GameScreen;
import com.tinybullet.game.view.MenuScreen;
import com.tinybullet.game.view.PartyScreen;

import java.util.Map;
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
			protected boolean onMessage(WebSocket webSocket, Object response) throws WebSocketException {
				if(response instanceof ResponseListPartiesJson) {
					menuScreen.getLock().lock();
					menuScreen.setList(((ResponseListPartiesJson)response).list);
					menuScreen.setJoinnable(((ResponseListPartiesJson)response).joinnable);
					menuScreen.changeButtons();
					menuScreen.getLock().unlock();
				}
				else if(response instanceof ResponsePlayerStatusPartyJson) {
					ResponsePlayerStatusPartyJson responsePlayerStatusPartyJson = (ResponsePlayerStatusPartyJson)response;
					if(responsePlayerStatusPartyJson.join) {
						partyScreen.getLock().lock();
						partyScreen.setPlayers(responsePlayerStatusPartyJson.players);
						partyScreen.setReadies(responsePlayerStatusPartyJson.readies);
						partyScreen.setPlayerColor(responsePlayerStatusPartyJson.playerColor);
						partyScreen.getLock().unlock();
						if(game.getScreen() != partyScreen) {
							game.setScreen(partyScreen);
						}
					}
				}
				else if(response instanceof ResponsePartyStateJson) {
					gameScreen.getLock().lock();
					gameScreen.setState(((ResponsePartyStateJson)response).partyState);
					gameScreen.getLock().unlock();
				}
				else if(response instanceof ResponsePositionsPlayersPartyJson) {
					ResponsePositionsPlayersPartyJson responsePositionsPlayersPartyJson = (ResponsePositionsPlayersPartyJson)response;
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
				else if(response instanceof ResponseFireBulletJson) {
					ResponseFireBulletJson responseFireBulletJson = (ResponseFireBulletJson)response;
					gameScreen.getLock().lock();
					if(!gameScreen.getBullets().get(responseFireBulletJson.color).isSourceOfFire()) {
						gameScreen.getBullets().get(responseFireBulletJson.color).fire(responseFireBulletJson.position, responseFireBulletJson.angle, responseFireBulletJson.direction, false);
					}
					gameScreen.getBullets().get(responseFireBulletJson.color).setSourceOfFire(false);
					gameScreen.getLock().unlock();
				}
				else if(response instanceof ResponsePickUpBulletJson) {
					ResponsePickUpBulletJson responsePickUpBulletJson = (ResponsePickUpBulletJson)response;
					gameScreen.getLock().lock();
					if(gameScreen.getPlayer().getColor() == responsePickUpBulletJson.playerColor) {
						if(responsePickUpBulletJson.pickUp) {
							gameScreen.getBullets().get(responsePickUpBulletJson.bulletColor).setLock(false);
						}
						else {
							gameScreen.getPlayer().setBullet(null);
						}
					}
					else {
						gameScreen.getBullets().get(responsePickUpBulletJson.bulletColor).pickUp();
					}
					gameScreen.getLock().unlock();
				}
				else if(response instanceof ResponseBulletTouchPlayerJson) {
					// TODO Change bullet to dropped bullet at the position
				}
				else if(response instanceof ResponsePlayerDieJson) {
					ResponsePlayerDieJson responsePlayerDieJson = (ResponsePlayerDieJson)response;
					lock.lock();
					if(gameScreen.getPlayer().getColor() == responsePlayerDieJson.color) {
						gameScreen.getPlayer().die();
					}
					else {
						for(Map.Entry<PlayerColor, OtherPlayer> otherPlayer : gameScreen.getOtherPlayers().entrySet()) {
							if(otherPlayer.getKey() == responsePlayerDieJson.color) {
								otherPlayer.getValue().die();
							}
						}
					}
					lock.unlock();
				}
				else if(response instanceof ResponseScorePartyJson) {
					ResponseScorePartyJson responseScorePartyJson = (ResponseScorePartyJson)response;
					gameScreen.getLock().lock();
					gameScreen.setState(PartyState.SCORE);
					Map<PlayerColor, Integer> scores = gameScreen.getScores();
					scores.clear();
					for(int i = 0; i < responseScorePartyJson.players.length; i++) {
						scores.put(responseScorePartyJson.players[i], responseScorePartyJson.scores[i]);
					}
					gameScreen.getLock().unlock();
				}
				else if(response instanceof ResponsePartyEndJson) {
					ResponsePartyEndJson responsePartyEndJson = (ResponsePartyEndJson)response;
					// TODO write content
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
