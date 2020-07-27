package com.game;

import com.game.api.Player;
import com.game.api.PlayerType;
import com.game.constants.GameConstants;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

/**
 * Main implementation of the game, with threads
 */
public class PlayerImpl extends Player {

    private final Logger log = Logger.getLogger(PlayerImpl.class.getName());

    public PlayerImpl(PlayerType playerType, //
                      BlockingQueue<String> sent, //
                      BlockingQueue<String> received, //
                      int counter) {
        super(playerType, sent, received, counter);
    }

    @Override
    public void run() {
        if (PlayerType.INITIATOR == this.playerType) {
            sendInitMessage();
        }
        while (true) {
            String receivedMessage = receiveMessage();
            sendMessage(receivedMessage);
        }
    }

    /**
     * overrides the {@link Player} abstract class which implements the {@link com.game.api.Message}.
     * Used by both Initiator and Receiver
     * @param message string sent to each other.
     */
    @Override
    public void sendMessage(String message) {
        String reply;
        if (playerType == PlayerType.RECEIVER) {
            this.counter++;
            reply = message.replaceAll("[0123456789]", "") + "  " + this.counter;
            log.info(String.format("Player [%s] sent message [%s].%n", this, reply));

            //terminate condition.
            if (this.counter == GameConstants.MAX_MESSAGE_LIMIT) {
                log.info("Reached the maximum number of Messages, Application terminating. Bye! :)");
                System.exit(0);
            }
        } else {
            reply = message.replaceAll("[0123456789]", "").trim();
            log.info(String.format("Player [%s] sent message [%s].%n", this, reply));
        }

        try {
            // adds message
            sent.put(reply);
            sleep(1000);

        } catch (InterruptedException interrupted) {
            String error = String.format(
                    "Player [%s] failed to receive message on iteration [%d].",
                    this, this.counter);
            throw new IllegalStateException(error, interrupted);
        }
    }

    /**
     * overrides the {@link Player} abstract class which implements the {@link com.game.api.Message}.
     * Used by both Initiator and Receiver
     * @return the message in the queue.
     */
    @Override
    public String receiveMessage() {
        String receivedMessage;
        try {
            receivedMessage = received.take();

        } catch (Exception interrupted) {
            String error = String.format(
                    "Player [%s] failed to receive message on iteration [%d].",
                    this.getName(), this.counter);
            throw new IllegalStateException(error, interrupted);
        }
        return receivedMessage;
    }

    /**
     * Send the Initial message to the queue.
     * It is activated only by when the player is Initiator.
     */
    private void sendInitMessage() {
        try {
            sent.put(GameConstants.INIT_MESSAGE);
            log.info(String.format("Player [%s] sent message [%s].%n", this, GameConstants.INIT_MESSAGE));
        } catch (InterruptedException interrupted) {
            String error = String.format(
                    "Player [%s] failed to sent message [%s].",
                    this.getName(), GameConstants.INIT_MESSAGE);
            throw new IllegalStateException(error, interrupted);
        }
    }
}
