package game;

import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;

public class Entity {
    public ArrayList<Card> hand = new ArrayList<>();

    GamePanel gp;
    AssetManager ast;

    public Entity(GamePanel gp, AssetManager ast){
        this.gp = gp;
        this.ast = ast;
    }

    public Card placeCard(int index){
        Card c = new Card(hand.get(index).getColor(), hand.get(index).getNumber());
        hand.remove(index);
        return c;
    }

    protected void drawHand(Graphics g2, int spacing, int inventoryHeight, boolean showCard, int anmVal){
        for(int i = 0; i < hand.size()-anmVal; i++){
            if(showCard) g2.drawImage(ast.getAsset(hand.get(i).getColor() + hand.get(i).getNumber()), (gp.screenWidth/2)-((hand.size()*ast.cardWidth+hand.size()*spacing)/2)+(i*(ast.cardWidth+spacing)), inventoryHeight, ast.cardWidth, ast.cardHeight, null);
            else g2.drawImage(ast.getAsset("otherside"), (gp.screenWidth/2)-((hand.size()*ast.cardWidth+hand.size()*spacing)/2)+(i*(ast.cardWidth+spacing))+(spacing/2), inventoryHeight, ast.cardWidth, ast.cardHeight, null);
        }
    }

    public void reciveCard(Card c){
        hand.add(new Card(c.getColor(), c.getNumber()));
    }
}