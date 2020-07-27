package com.game.api;

import java.util.concurrent.BlockingQueue;

/**
 * Abstract players class which implemented in {@link com.game.PlayerImpl}
 * this extends {@link Thread} and implements {@link Message}
 */
public abstract class Player extends Thread implements Message {

    protected Enum playerType;
    protected final BlockingQueue<String> sent;
    protected final BlockingQueue<String> received;
    protected Integer counter;


    public Player(PlayerType playerType, BlockingQueue<String> sent, BlockingQueue<String> received, int counter) {
        this.playerType = playerType;
        this.sent = sent;
        this.received = received;
        this.counter = counter;
    }
}
