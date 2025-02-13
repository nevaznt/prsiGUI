package game;

/*color: red
         green
         yellow
         brown
  number: 7, 8, 9, 10,
          11 - spodek, 12 - svršek, 13 - král, 14 - eso
*/

import java.awt.geom.AffineTransform;

public class Card {
    final private String color;
    final private int number;
    private AffineTransform rotation;
    public boolean rotationAssigned;

    public Card(String color, int number){
        this.color = color;
        this.number = number;
        rotationAssigned = false;
        rotation = new AffineTransform();
    }

    public AffineTransform getRotation(){
        return rotation;
    }

    public void assignRotation(int screenWidth, int screenHeight){
        rotation = new AffineTransform();
        double rotationAngle;

        if(Math.random()>0.5) rotationAngle = Math.toRadians((int)(Math.random() * 10)+1);
        else rotationAngle = Math.toRadians((int)(Math.random() * 10)+350);

        rotation.rotate(rotationAngle, (double)screenWidth/2, (double)screenHeight/2);
        rotationAssigned = true;
    }

    public String getColor(){
        return color;
    }

    public int getNumber(){
        return number;
    }
}

