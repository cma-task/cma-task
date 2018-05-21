package se.cma;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class PingPongGame {
    private static final int COUNT_PLAYERS = 2;

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        CountDownLatch stopThreads = new CountDownLatch(2);
        ExecutorService executor = Executors.newFixedThreadPool(COUNT_PLAYERS);

        List<Player> players = IntStream.rangeClosed(1, COUNT_PLAYERS)
                .mapToObj(i -> {
                    Player player = new Player(Constants.PingPong.namesPingPongs.get(i), Constants.PingPong.MESSAGE_START_PLAYER, lock, Constants.PingPong.HIT_GAME);
                    player.setStopThreads(stopThreads);
                    return player;
                }).collect(Collectors.toList());

        for(int i=0; i < COUNT_PLAYERS - 1; i++)
            players.get(i).setOtherPlayer(players.get(i+1));

        players.get(COUNT_PLAYERS-1).setOtherPlayer(players.get(0));
        players.get(0).setCanPlay(true);

        System.out.println("Start PingPong");

        for(Player player : players)
            executor.submit(player);

        try {
            stopThreads.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdownNow();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("PingPongGame thread interrupted");
        }

        System.out.println("Stop PingPong!");
    }
}
