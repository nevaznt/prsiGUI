package main;

import game.*;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    final int baseWidth = 16;
    final int baseHeight = 9;
    public final int scale = 120;
    public final double cardScale = 0.5;
    public final double otherElementsScale = 0.75;
    public final int screenWidth = baseWidth * scale;
    public final int screenHeight = baseHeight * scale;
    private final int noOfCardOnStart = 6;
    public boolean game = true;
    public int playerWinsCounter;
    public boolean introAnimation;
    public int anmStage;
    public int anmPlayerHand;
    public int anmBotHand;

    public final short FPS = 60;

    KeyHandler keyH = new KeyHandler(this);
    public Thread thread;

    Table table;
    Player player;
    Bot bot;
    AssetManager ast;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        ast = new AssetManager(cardScale, otherElementsScale);
        init();
    }

    private void init(){
        table = new Table(this, ast);
        player = new Player(this, keyH, ast);
        bot = new Bot(this, ast);
        game = true;
        introAnimation = true;
        anmStage = 4;
        anmPlayerHand = noOfCardOnStart;
        anmBotHand = noOfCardOnStart;

        for(int i = 0; i < noOfCardOnStart; i++) {
            player.reciveCard(table.takeOutOfDeck(0));
            bot.reciveCard(table.takeOutOfDeck(0));
        }
    }

    private void animation(){
        if(anmStage == 4) anmPlayerHand--;
        else if(anmStage == 3) anmBotHand--;

        if(anmStage==4 && anmPlayerHand == 0) anmStage = 3;
        else if(anmStage==3 && anmBotHand == 0) anmStage = 2;
        else if(anmStage==2) anmStage = 1;
        else if(anmStage==1) anmStage = 0;

        if(anmStage==0) introAnimation = false;
        else table.delay = 5;
    }

    public void startGameLoop(){
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        double drawInterval = (double)1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(thread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }

        System.exit(0);
    }

    public void update(){

        if(introAnimation && table.delay == 0) animation();

        if(game && table.delay == 0 && !introAnimation) {
            if (table.playerMove) {
                if (player.update(table)) {
                    table.playerMove = false;
                    table.botSkip = 0;
                }
            } else if (bot.update(table)) table.playerMove = true;

            table.update(player, bot);
        }
        else if(!introAnimation){
            if(keyH.enterPressed) {
                init();
                keyH.enterPressed = false;
            }
        }

        if(table.delay > 0) table.delay--;
        if(table.botSkip > 0) table.botSkip--;
        if(keyH.escPressed) thread = null;

    }

    private void setFont(Graphics2D g2, Color c, double scale){
        int fontSize = (int)(scale*this.scale);
        g2.setColor(c);
        g2.setFont(new Font("Arial", Font.BOLD, fontSize));
    }

    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        // background
        for(int i = 0; i < screenHeight; i+=(scale/2)) for(int j = 0; j < screenWidth; j += (scale/2)) {
            if(i % scale == 0 && j % scale == 0) g2.setColor(Color.decode("#008800"));
            else if(i % scale == 0 && j % scale != 0) g2.setColor(Color.decode("#009200"));
            else if(i % scale != 0 && j % scale != 0) g2.setColor(Color.decode("#008800"));
            else if(i % scale != 0 && j % scale == 0) g2.setColor(Color.decode("#009200"));
            g2.fillRect(j, i, scale, scale);
        }
        g2.setStroke(new BasicStroke((float)(scale*0.25)));
        g2.setColor(Color.decode("#008200"));
        g2.drawRect(0, 0, screenWidth, screenHeight);

        player.paint(g2, table);
        bot.paint(g2);
        table.paint(g2);

        if(table.delay > 0 && !introAnimation){
            setFont(g2, Color.WHITE, 0.5);
            g2.drawString("STOJÍŠ", screenWidth/8, ((screenHeight/2)+(ast.cardHeight/2)));
        }
        else if(table.botSkip > 0 && !introAnimation){
            setFont(g2, Color.WHITE, 0.5);
            g2.drawString("STOJÍM", screenWidth/8, (screenHeight/2)-(ast.cardHeight/2)+((int)(0.5*scale)/2));
        }

        if(!game) {
            g2.setColor(new Color(0, 0, 0, 90));
            g2.fillRect(0, 0, screenWidth, screenHeight);
            g2.setColor(Color.WHITE);
            int fontSize = (int)(0.5*scale);
            g2.setFont(new Font("Arial", Font.BOLD, fontSize));
            if(player.hand.isEmpty()) g2.drawString("VYHRÁLI JSTE", (screenWidth/2)-((12*(fontSize/2))/2), screenHeight/2+screenHeight/3);
            else g2.drawString("SOUPEŘ VYHRÁL", (screenWidth/2)-((15*(fontSize/2))/2), screenHeight/3-fontSize);
            g2.drawString("[ENTER] DALŠÍ HRA", scale*2, (screenHeight/2));
        }

        g2.dispose();
    }
}
