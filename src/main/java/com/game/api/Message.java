package com.game.api;

/**
 * A player is able to send and receive messages with other players
 */
public interface Message {
    /**
     * Sends a message to another player
     */
    void sendMessage(String Message) throws InterruptedException;

    /**
     * Receives a message from another player.
     * @return  returns a reply to be sent to the sender
     */
    String receiveMessage() throws InterruptedException;
}
