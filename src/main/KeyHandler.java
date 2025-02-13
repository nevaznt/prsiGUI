package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean escPressed, enterPressed;
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    GamePanel gp;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            escPressed = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gp.game && e.getKeyCode() == KeyEvent.VK_ENTER) enterPressed = true;

        if(gp.table.delay > 0 || !gp.table.playerMove) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                escPressed = false;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                upPressed = true;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                downPressed = true;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = true;
                break;
        }
    }
}
