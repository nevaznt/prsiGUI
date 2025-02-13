package game;

import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;

public class Bot extends Entity{

    private final int inventoryHeight = 32;
    private int thinkingTime = 0;

    public Bot(GamePanel gp, AssetManager ast) {
        super(gp, ast);

    }

    public boolean update(Table t){
        if(thinkingTime == 0) thinkingTime = (int)(((Math.random())+0.25)*gp.FPS);
        if(thinkingTime > 0) thinkingTime--;
        if(thinkingTime != 0) return false;

        ArrayList<Integer> canUse = new ArrayList<>();
        ArrayList<Integer> twelves = new ArrayList<>();

        for(int i = 0; i < hand.size(); i++) {
            if(t.applyrules(hand.get(i)) && hand.get(i).getNumber() != 12) canUse.add(i);
            else if(t.applyrules(hand.get(i)) && hand.get(i).getNumber() == 12) twelves.add(i);
        }

        if(canUse.size() == 1) {
            t.midDeck.add(placeCard(canUse.getFirst()));
            t.detectException();
            return true;
        }
        else if(canUse.isEmpty() && !twelves.isEmpty()){
            t.midDeck.add(placeCard(twelves.getFirst()));
            t.detectException();

            int[] colorCount = {0, 0, 0, 0};
            for(int i = 0; i < hand.size(); i++){
                switch(hand.get(i).getColor()){
                    case "red" -> colorCount[0]++;
                    case "green" -> colorCount[1]++;
                    case "yellow" -> colorCount[2]++;
                    case "brown" -> colorCount[3]++;
                }
            }

            String c = "";
            if(max(colorCount)==0) c = "red";
            else if(max(colorCount)==1) c = "green";
            else if(max(colorCount)==2) c = "yellow";
            else if(max(colorCount)==3) c = "brown";
            t.colorChange = c;
            return true;
        }
        else if(canUse.size() > 1){
            int[] score = new int[canUse.size()];
            for(int i = 0; i < canUse.size(); i++){
                for(int j = 0; j < hand.size(); j++){
                    if(hand.get(j) == hand.get(canUse.get(i))) continue;
                    if(hand.get(canUse.get(i)).getColor().equals(hand.get(j).getColor()) && hand.get(j).getNumber() != 12) score[i]++;
                    else if(hand.get(canUse.get(i)).getNumber() == hand.get(j).getNumber()) score[i]++;
                }
            }

            t.midDeck.add(placeCard(canUse.get(max(score))));
            t.detectException();
            return true;
        }
        else if(t.sedmActive>0) {
            for(int i = 0; i < 4 * t.sedmActive; i++) if(t.checkDeck()) reciveCard(t.takeOutOfDeck(0));
            t.sedmActive = 0;
            return true;
        }
        else if(canUse.isEmpty() && twelves.isEmpty() && !t.esoActive) {
            if(t.checkDeck()) reciveCard(t.takeOutOfDeck(0));
            return true;
        }

        return false;
    }

    private int max(int[] arr){
        int max = 0;

        for(int i = 0; i < arr.length; i++){
            if(arr[i] > arr[max]) max = i;
        }

        return max;
    }


    public void paint(Graphics2D g2){
        int spacing = -(int)(ast.cardWidth/1.5);

        drawHand(g2, spacing, inventoryHeight, false, gp.anmBotHand);

    }
}