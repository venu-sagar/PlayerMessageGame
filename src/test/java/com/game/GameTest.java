package com.game;

import com.game.api.Player;
import com.game.api.PlayerType;
import com.game.constants.GameConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/**
 * Unit tests
 */
class UnitTests {
    private Player initiator;
    private Player player2;
    private Assertions Assert;

    BlockingQueue<String> initiatorToReceiver = new ArrayBlockingQueue<String>(GameConstants.MAX_MESSAGES_IN_QUEUE);
    BlockingQueue<String> receiverToInitiator = new ArrayBlockingQueue<String>(GameConstants.MAX_MESSAGES_IN_QUEUE);
    int counter = 0;

    /**
     * Create two players, initiator and player2
     */
    @BeforeEach
    public void initialization() {

        Player initiator = new PlayerImpl(PlayerType.INITIATOR, initiatorToReceiver, receiverToInitiator, counter);
        Player receiver = new PlayerImpl(PlayerType.RECEIVER, receiverToInitiator, initiatorToReceiver, counter);
    }

    /**
     * This test case just counts the number of threads created and exucuted
     * and asserts to 0, because we are calling System.exit if the max size is reached 10
     * killing both the threads.
     *
     * @throws InterruptedException
     */
    @Test
    public void testCase2Threads() throws InterruptedException {
        final ArrayList<Integer> threadsCompleted = new ArrayList<Integer>();
        Thread runningT2 = new Thread();
        Thread runningT1 = new Thread();

        for (int i = 0; i >= 10; i++) {
            runningT1 = new PlayerImpl(PlayerType.INITIATOR, initiatorToReceiver, receiverToInitiator, counter) {
                @Override
                public void run() {
                    Assert.fail();
                    threadsCompleted.add(1);
                }
            };

            runningT2 = new PlayerImpl(PlayerType.RECEIVER, receiverToInitiator, initiatorToReceiver, counter) {
                @Override
                public void run() {
                    Assert.fail();
                    threadsCompleted.add(2);
                }
            };
        }

        Thread t1 = new Thread(runningT1);
        Thread t2 = new Thread(runningT2);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Threads completed: " + threadsCompleted);
        Assert.assertEquals(0, threadsCompleted.size());
    }
}