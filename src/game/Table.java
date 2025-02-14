package game;

import main.GamePanel;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Collections;

public class Table {
    public final ArrayList<Card> deck = new ArrayList<>();
    public final ArrayList<Card> midDeck = new ArrayList<>();
    public boolean playerMove;
    public boolean esoActive;
    public int sedmActive;
    public String colorChange;
    public int delay;
    public int botSkip = 0;
    private final double delayTime = 1.5;
    AssetManager ast;
    GamePanel gp;

    public Table(GamePanel gp, AssetManager ast) {
        String[] colors = {"red", "green", "yellow", "brown"};
        for(int j = 0; j < 4; j++) for(int i = 7; i <= 14; i++) deck.add(new Card(colors[j], i));

        Collections.shuffle(deck);

        midDeck.add(takeOutOfDeck(0));
        //midDeck.getFirst().rotationAssigned = true;
        midDeck.getFirst().offsetAssingned = true;

        this.gp = gp;
        this.ast = ast;

        playerMove = true;
        esoActive = false;
        sedmActive = 0;
        colorChange = "";
    }

    public boolean applyrules(Card c){ // returns true if the move is valid
        return !((esoActive && c.getNumber() != 14) || (sedmActive>0 && c.getNumber() != 7) || (Objects.equals(colorChange, "") && !Objects.equals(midDeck.getLast().getColor(), c.getColor()) || (!Objects.equals(colorChange, "") && !Objects.equals(c.getColor(), colorChange))) && midDeck.getLast().getNumber() != c.getNumber());
    }

    public boolean applyrules(Card c, Card compare){ // returns true if the move is valid
        return !((esoActive && c.getNumber() != 14) || (sedmActive>0 && c.getNumber() != 7) || (Objects.equals(colorChange, "") && !Objects.equals(compare.getColor(), c.getColor()) || (!Objects.equals(colorChange, "") && !Objects.equals(c.getColor(), colorChange))) && compare.getNumber() != c.getNumber());
    }

    public boolean checkDeck(){
        return !deck.isEmpty() || midDeck.size() > 1;
    }

    public void detectException(){
        colorChange = "";
        if(midDeck.getLast().getNumber() == 7) sedmActive++;
        else if(midDeck.getLast().getNumber() == 14) esoActive = true;
    }

    public Card takeOutOfDeck(int index){
        if(midDeck.size() > 1 && deck.isEmpty()){
            int midDeckSize = midDeck.size()-1;
            for(int i = 0; i < midDeckSize; i++) {
                deck.add(new Card(midDeck.getFirst().getColor(), midDeck.getFirst().getNumber()));
                midDeck.removeFirst();
            }
        }

        Card c = new Card(deck.get(index).getColor(), deck.get(index).getNumber());
        deck.remove(index);
        return c;
    }

    public void update(Player p, Bot b){
        if(p.hand.isEmpty()){
            gp.game = false;
            gp.playerWinsCounter++;
        }
        else if(b.hand.isEmpty()) gp.game = false;

        if(midDeck.getLast().getNumber() == 14 && playerMove && esoActive){
            for(int i = 0; i < p.hand.size(); i++) {
                if(p.hand.get(i).getNumber() == 14) break;
                else if(i == p.hand.size()-1){
                    playerMove = false;
                    esoActive = false;
                    delay = (int)(delayTime*gp.FPS);
                    return;
                }
            }
        }
        else if(midDeck.getLast().getNumber() == 14 && !playerMove && esoActive){
            for(int i = 0; i < b.hand.size(); i++) {
                if(b.hand.get(i).getNumber() == 14) break;
                else if(i == b.hand.size()-1){
                    playerMove = true;
                    esoActive = false;
                    botSkip = (int)(delayTime*gp.FPS);
                    return;
                }
            }
        }

        return;
    }

    public void paint(Graphics2D g2){
        if(gp.anmStage < 2){
            for(int i = 0; i < midDeck.size(); i++){
                //if(!midDeck.get(i).rotationAssigned) midDeck.get(i).assignRotation(gp.screenWidth, gp.screenHeight);
                //g2.setTransform(midDeck.get(i).getRotation());
                if(!midDeck.get(i).offsetAssingned) midDeck.get(i).assignOffset();
                g2.drawImage(ast.getAsset(midDeck.getLast().getColor()+midDeck.getLast().getNumber()), (gp.screenWidth/2)-(ast.cardWidth/2)+midDeck.get(i).rofsx, (gp.screenHeight/2)-(ast.cardHeight/2)+midDeck.get(i).rofsy, ast.cardWidth, ast.cardHeight, null);
            }
            //g2.setTransform(new AffineTransform());
        }

        if(gp.anmStage == 0) {
            if(deck.isEmpty()) g2.drawImage(ast.getAsset("shadow"), (gp.screenWidth / 2) + ast.cardWidth + gp.scale, (gp.screenHeight / 2) - (ast.cardHeight / 2), ast.cardWidth, ast.cardHeight, null);

            int offsetMultiplier = 1;
            for (int i = 0; i < deck.size(); i++)
                g2.drawImage(ast.getAsset("otherside"), (gp.screenWidth / 2) + ast.cardWidth + gp.scale, (gp.screenHeight / 2) - (ast.cardHeight / 2) - (i * offsetMultiplier), ast.cardWidth, ast.cardHeight, null);
        }

        int stoneY;
        if(playerMove) stoneY = (gp.screenHeight/2)+(ast.cardHeight/2)-ast.stoneHeight;
        else stoneY = (gp.screenHeight/2)-(ast.cardHeight/2);

        g2.drawImage(ast.getAsset("stone"), (gp.screenWidth/2)+ast.cardWidth, stoneY, ast.stoneWidth, ast.stoneHeight, null);
        if(!colorChange.isEmpty()) g2.drawImage(ast.getAsset(colorChange+"symbol"), (gp.screenWidth/2)+ast.cardWidth+(ast.stoneWidth/12), stoneY+(ast.stoneHeight/8), ast.symbolWidth, ast.symbolHeight, null);

        int fontSize = (int)(0.25 * gp.scale);
        g2.setFont(new Font("Arial", Font.BOLD, fontSize));
        g2.setColor(Color.white);
        g2.drawString("SCORE: " + gp.playerWinsCounter, fontSize, fontSize*2);

        return;
    }

}
