package game;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.util.Objects;

public class Player extends Entity{

    KeyHandler keyH;

    private int cursor = 0;
    private boolean chossingcolor = false;
    private final int inventoryHeight;

    public Player(GamePanel gp, KeyHandler keyH, AssetManager ast) {

        super(gp, ast);
        this.keyH = keyH;

        inventoryHeight = gp.screenHeight-ast.cardHeight-ast.arrowHeight;
    }

    public boolean update(Table t){
        if(chossingcolor) return chooseColor(t);

        if(keyH.upPressed && cursor < hand.size()) cursor = hand.size();
        else if(keyH.downPressed && cursor == hand.size()) cursor = hand.size()-1;

        else if(keyH.leftPressed && cursor > 0) cursor--;
        else if(keyH.rightPressed && cursor < hand.size()) cursor++;

        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){
            keyH.upPressed = false;
            keyH.downPressed = false;
            keyH.leftPressed = false;
            keyH.rightPressed = false;
        }

        if(!keyH.enterPressed) return false;
        else keyH.enterPressed = false;

        // get another card
        if(cursor == hand.size() && t.sedmActive==0 && !t.esoActive) {
            if(t.checkDeck()) reciveCard(t.takeOutOfDeck(0));
            cursor = 0;
            return true;
        }

        // eso exception
        if(cursor != hand.size()){
            if(t.esoActive && hand.get(cursor).getNumber() == 14){
                t.midDeck.add(placeCard(cursor));
                cursor = 0;
                return true;
            }
            else if(t.esoActive && hand.get(cursor).getNumber() != 14) return false;
        }

        // sedma exception
        if(cursor != hand.size()){
            if(t.sedmActive>1 && hand.get(cursor).getNumber() == 7){
                t.midDeck.add(placeCard(cursor));
                cursor = 0;
                t.sedmActive++;
                return true;
            }
            else if(t.sedmActive>0 && hand.get(cursor).getNumber() != 7) return false;
        }
        if(t.sedmActive>0 && cursor == hand.size()){
            for(int i = 0; i < 4*t.sedmActive; i++) if(t.checkDeck()) reciveCard(t.takeOutOfDeck(0));
            t.sedmActive = 0;
            cursor = 0;
            return true;
        }

        // svrsek exception
        if(t.sedmActive==0 && !t.esoActive && hand.get(cursor).getNumber() == 12) {
            t.midDeck.add(placeCard(cursor));
            chossingcolor = true;
            cursor = 0;
            return false;
        }

        // place card
        if(cursor >= 0 && cursor < hand.size() && (Objects.equals(t.colorChange, "") || (!Objects.equals(t.colorChange, "") && Objects.equals(hand.get(cursor).getColor(), t.colorChange)))) {
            if(!t.applyrules(hand.get(cursor))) return false;
            t.midDeck.add(placeCard(cursor));
            if(t.midDeck.getLast().getNumber() == 7) t.sedmActive++;
            else if(t.midDeck.getLast().getNumber() == 14) t.esoActive = true;
            t.colorChange = "";
            cursor = 0;
            return true;
        }
        else if(cursor >= 0 && cursor < hand.size() && !Objects.equals(t.colorChange, "") && !Objects.equals(hand.get(cursor).getColor(), t.colorChange)) return false;

        return false;
    }

    public boolean chooseColor(Table t){
        if(keyH.leftPressed && cursor > 0) cursor--;
        else if(keyH.rightPressed && cursor < 3) cursor++;

        if(keyH.leftPressed || keyH.rightPressed){
            keyH.leftPressed = false;
            keyH.rightPressed = false;
        }

        if(!keyH.enterPressed) return false;
        else keyH.enterPressed = false;

        switch(cursor){
            case 0: t.colorChange = "red";
                    break;
            case 1: t.colorChange = "green";
                    break;
            case 2: t.colorChange = "yellow";
                    break;
            case 3: t.colorChange = "brown";
                    break;
        }

        cursor = 0;
        chossingcolor = false;
        return true;
    }


    public void paint(Graphics2D g2, Table t){
        int spacing = 0;
        if(hand.size()*ast.cardWidth > (gp.screenWidth)) spacing = (((gp.screenWidth-(ast.cardWidth*2))-(hand.size()*ast.cardWidth))/hand.size());

        drawHand(g2, spacing, inventoryHeight, true, gp.anmPlayerHand);

        if(chossingcolor && gp.game && !gp.introAnimation){
            String[] colors = {"red", "green", "yellow", "brown"};
            for(int i = 0; i < 4; i++) {
                g2.drawImage(ast.getAsset(colors[i]+"symbol"), (gp.screenWidth/2)-(ast.cardWidth/2)-gp.scale-(ast.symbolWidth*4)+(ast.symbolWidth*i), (gp.screenHeight/2)-(ast.symbolHeight/2), ast.symbolWidth, ast.symbolHeight, null);
                if(cursor==i && t.delay == 0 && t.playerMove) g2.drawImage(ast.getAsset("arrow"), (gp.screenWidth/2)-(ast.cardWidth/2)-gp.scale-(ast.symbolWidth*4)+(ast.symbolWidth*i)+((ast.symbolWidth/2)-(ast.arrowWidth/2)), (gp.screenHeight/2)+(ast.symbolHeight/2), ast.arrowWidth, ast.arrowHeight, null);
            }
        }
        else {
            if(cursor == hand.size() && gp.game && t.delay == 0 && !gp.introAnimation && t.playerMove) g2.drawImage(ast.getAsset("arrow"), ((gp.screenWidth/2)+ast.cardWidth + gp.scale)+((ast.cardWidth/2)-(ast.arrowWidth/2)), ((gp.screenHeight/2)+(ast.cardHeight/2))+(ast.arrowHeight/4), ast.arrowWidth, ast.arrowHeight, null);
            else if(gp.game && t.delay == 0 && !gp.introAnimation && t.playerMove){
                g2.drawImage(ast.getAsset(hand.get(cursor).getColor() + hand.get(cursor).getNumber()), (gp.screenWidth/2)-((hand.size()*ast.cardWidth+hand.size()*spacing)/2)+(cursor*(ast.cardWidth+spacing))+(spacing/2), inventoryHeight, ast.cardWidth, ast.cardHeight, null);
                g2.drawImage(ast.getAsset("arrow"), (gp.screenWidth/2)-((hand.size()*ast.cardWidth+hand.size()*spacing)/2)+(cursor*(ast.cardWidth+spacing))+(ast.cardWidth/2-ast.arrowWidth/2)+(spacing/2), gp.screenHeight-ast.arrowWidth, ast.arrowWidth, ast.arrowHeight, null);
            }
        }

        return;
    }

}
