package net.thewaffleshop.b3.game;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class Engine {

    private Thread gameThread;

    private final Player player = new Player();

    @PostConstruct
    public void init() {
        gameThread = new Thread(this::thread);
        gameThread.start();
    }

    private void thread() {
        while (true) {
            if (Thread.interrupted()) {
                break;
            }
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                break;
            }

            synchronized (player) {
                player.setX(player.getX() + player.getVx() * 10);
                player.setY(player.getY() + player.getVy() * 10);
            }
        }
    }

    public int getPlayerX() {
        synchronized (player) {
            return player.getX();
        }
    }

    public int getPlayerY() {
        synchronized (player) {
            return player.getY();
        }
    }

    public void setPlayerVx(int vx) {
        synchronized (player) {
            player.setVx(vx);
        }
    }

    public void setPlayerVy(int vy) {
        synchronized (player) {
            player.setVy(vy);
        }
    }
}
