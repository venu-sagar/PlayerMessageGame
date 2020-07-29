package com.game;

import com.game.api.Message;
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
                      int messageCount) {
        super(playerType, sent, received, messageCount);
    }

    @Override
    public void run() {
        if (PlayerType.INITIATOR == this.playerType) {
            conversationStarter();
        }
        while (true) {
            String receivedMessage = receiveMessage();
            sendMessage(receivedMessage);
        }
    }

    /**
     * overrides the {@link Player} abstract class which implements the {@link Message}.
     * Used by both Initiator and Receiver
     * @param message string sent to each other.
     */
    @Override
    public synchronized void sendMessage(String message) {
        String reply;

        try {
            if (playerType == PlayerType.RECEIVER) {
                this.messageCount++;
                reply = "Hi Initiator, This is receiver. Your Message is" + message.replaceAll("[0123456789]", "") + "  " + this.messageCount;
                this.log.info(String.format("Player [%s] sent message [%s].%n", this, reply));

                //terminate condition.
                if (this.messageCount == GameConstants.MAX_MESSAGE_LIMIT) {
                    this.log.warning("Reached the maximum number of Messages, Application terminating. Bye! :)");
                    System.exit(0);
                }
            } else {
                reply = "Hi Receiver, Thanks, for the message. What is the current counter? ";
                this.log.info(String.format("[%s] sent message [%s].%n", this.getName(), reply));
            }
            // adds message
            this.sent.put(reply);
            sleep(1000);

        } catch (InterruptedException interrupted) {
            String error = String.format(
                    "Player [%s] failed to receive message on iteration [%d].",
                    this, this.messageCount);
            throw new IllegalStateException(error, interrupted);
        }
    }

    /**
     * overrides the {@link Player} abstract class which implements the {@link Message}.
     * Used by both Initiator and Receiver
     * @return the message in the queue.
     */
    @Override
    public synchronized String receiveMessage() {
        String receivedMessage;
        try {
            receivedMessage = this.received.take();

        } catch (Exception interrupted) {
            String error = String.format(
                    "Player [%s] failed to receive message on iteration [%d].",
                    this.getName(), this.messageCount);
            throw new IllegalStateException(error, interrupted);
        }
        return receivedMessage;
    }

    /**
     * Send the Initial message to the queue.
     * It is activated only by when the player is Initiator.
     */
    private synchronized void conversationStarter() {
        try {
            this.sent.put(GameConstants.START_MESSAGE);
            this.log.info(String.format("[%s] sent message [%s].%n", this.getName(), GameConstants.START_MESSAGE));
        } catch (InterruptedException interrupted) {
            String error = String.format(
                    "Player [%s] failed to sent message [%s].",
                    this.getName(), GameConstants.START_MESSAGE);
            throw new IllegalStateException(error, interrupted);
        }
    }
}
