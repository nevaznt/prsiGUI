package game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.ArrayList;

public class AssetManager {
    private final ArrayList<BufferedImage> assets = new ArrayList<>();

    public final int cardWidth;
    public final int cardHeight;
    public final int arrowWidth;
    public final int arrowHeight;
    public final int symbolWidth;
    public final int symbolHeight;
    public final int stoneWidth;
    public final int stoneHeight;

    public AssetManager(double scale, double otherElements) {

        String[] colors = {"red", "green", "yellow", "brown"};
        try {
            for(int j = 0; j < 4; j++) for(int i = 7; i <= 14; i++) assets.add(ImageIO.read(new FileInputStream("img/"+colors[j]+"/"+colors[j]+i+".png")));
            assets.add(ImageIO.read(new FileInputStream("img/otherside.png")));
            assets.add(ImageIO.read(new FileInputStream("img/arrow.png")));
            assets.add(ImageIO.read(new FileInputStream("img/red/redsymbol.png")));
            assets.add(ImageIO.read(new FileInputStream("img/green/greensymbol.png")));
            assets.add(ImageIO.read(new FileInputStream("img/yellow/yellowsymbol.png")));
            assets.add(ImageIO.read(new FileInputStream("img/brown/brownsymbol.png")));
            assets.add(ImageIO.read(new FileInputStream("img/stone.png")));
        } catch(Exception e) {
            e.printStackTrace();
        }

        cardWidth = (int)(assets.getFirst().getWidth() * scale);
        cardHeight = (int)(assets.getFirst().getHeight() * scale);
        arrowWidth = (int)(assets.get(33).getWidth() * otherElements);
        arrowHeight = (int)(assets.get(33).getHeight() * otherElements);
        symbolWidth = (int)(assets.get(34).getWidth() * (otherElements/2));
        symbolHeight = (int)(assets.get(34).getHeight() * (otherElements/2));
        stoneWidth = (int)(assets.get(38).getWidth() * (otherElements/2));
        stoneHeight = (int)(assets.get(38).getHeight() * (otherElements/2));
    }

    public BufferedImage getAsset(String name){
        switch (name) {
            case "otherside" -> { return assets.get(32); }
            case "arrow" -> { return assets.get(33); }
            case "redsymbol" -> { return assets.get(34); }
            case "greensymbol" -> { return assets.get(35); }
            case "yellowsymbol" -> { return assets.get(36); }
            case "brownsymbol" -> { return assets.get(37); }
            case "stone" -> { return assets.get(38); }
        }

        int n = 0;
        if(name.contains("green")) n = 8;
        else if(name.contains("yellow")) n = 16;
        else if(name.contains("brown")) n = 24;

        if(name.contains("8")) n += 1;
        else if(name.contains("9")) n += 2;
        else if(name.contains("10")) n += 3;
        else if(name.contains("11")) n += 4;
        else if(name.contains("12")) n += 5;
        else if(name.contains("13")) n += 6;
        else if(name.contains("14")) n += 7;

        return assets.get(n);
    }
}
