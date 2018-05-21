package se.cma;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Player implements Runnable {
    private String name;
    private volatile String message;
    private Integer maxHitsForGame;
    private Player otherPlayer;

    /**
     * объект блокировки потока
     */
    private final Lock lock;
    /**
     * условие блокировки потока основного игрока
     */
    private final Condition conditionHit;
    /**
     * условие блокировки 2 игрока
     */
    private Condition otherConditionHit;
    /**
     * флаг начала работы потока, меняем в критической секции
     */
    private volatile boolean canPlay = false;

    private volatile CountDownLatch stopThreads;

    /**
     * Текцщий удар
     */
    private int currentHit;



    public Player(String name, String message, Lock lock, Integer maxHitsForGame) {
        this.message = message;
        this.lock = lock;
        this.maxHitsForGame = maxHitsForGame;
        this.conditionHit = lock.newCondition();
        this.name = name;
    }


    public void run() {
        startPlay();
    }

    public void startPlay() {
        try {
            while (!isMaxHit() && !Thread.interrupted()) {
                lock.lock();
                try {
                    while (!canPlay) {
                        conditionHit.await();
                    }
                    message = TextMessage.getMessagePingPong(message, name, ++currentHit);
                    System.out.println(message);
                    otherPlayer.message = message;
                    this.canPlay = false;
                    otherPlayer.canPlay = true;
                    otherConditionHit.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
        catch (Exception e) {}
        stopThreads.countDown();
    }


    public boolean isMaxHit() {
        return maxHitsForGame != null && (maxHitsForGame - currentHit == 0);
    }

    public void setOtherPlayer(Player otherPlayer) {
        this.otherPlayer = otherPlayer;
        this.otherConditionHit = otherPlayer.conditionHit;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStopThreads(CountDownLatch stopThreads) {
        this.stopThreads = stopThreads;
    }
}
