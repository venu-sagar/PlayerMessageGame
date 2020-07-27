package com.game;

import com.game.api.Player;
import com.game.api.PlayerType;
import com.game.constants.GameConstants;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * This class starts a new game between two players
 */
public class Game {
    /**
     * Start a new game with two players.
     * initiator thread is started, receiver thread is started.
     *
     * @param args
     */
    public static void main(String[] args) {
        BlockingQueue<String> initiatorToReceiver = new ArrayBlockingQueue<String>(GameConstants.MAX_MESSAGES_IN_QUEUE);
        BlockingQueue<String> receiverToInitiator = new ArrayBlockingQueue<String>(GameConstants.MAX_MESSAGES_IN_QUEUE);
        int counter = 0;

        Player initiator = new PlayerImpl( PlayerType.INITIATOR, initiatorToReceiver, receiverToInitiator, counter);
        Player receiver = new PlayerImpl( PlayerType.RECEIVER, receiverToInitiator, initiatorToReceiver, counter);


        initiator.setName("Initiator");
        initiator.start();
        receiver.setName("Receiver");
        receiver.start();
    }
}
