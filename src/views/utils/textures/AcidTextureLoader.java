package views.utils.textures;

import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;

public class AcidTextureLoader {

    public static float[] getAcidTexCoordinates(){
        float[] acidTexCoordinates = new float[75*2];
        float puffer = 0.05f;
        int counter = 0;
        for(int y = 0; y < 5; ++y){
            for(int x = 0;x < 5 ; ++x){
                // lower left x
                acidTexCoordinates[counter] = x * 0.2f + puffer;
                // lower left y
                acidTexCoordinates[counter+1] = y * 0.2f + puffer;
                //lower right x
                acidTexCoordinates[counter+2] = (x+1)* 0.2f - puffer;
                //lower right y
                acidTexCoordinates[counter+3] = y * 0.2f + puffer;
                //upper right x
                acidTexCoordinates[counter+4] = (x+1) * 0.2f - puffer;
                //uppper right y
                acidTexCoordinates[counter+5] = (y+1) * 0.2f - puffer;
                counter += 6;
            }
        }
        return acidTexCoordinates;
    }
    public static int[] getAcidTextureStartId(String acidType){
        switch (acidType){
            case "K":
            case "R":
                return new int[]{0,1,2};
            case "Q":
            case "N":
                return new int[]{3,4,5};
            case "D":
            case "E":
                return new int[]{6,7,8};
            case "C":
            case "M":
                return new int[]{9,10,11};
            case "G":
                return new int[]{12,13,14};
            case "H":
                return new int[]{15,16,17};
            case "V":
            case "L":
            case "I":
                return new int[]{18,19,20};
            case "F":
            case "Y":
                return new int[]{21,22,23};
            case "P":
                return new int[]{24,25,26};
            case "T":
            case "S":
                return new int[]{27,28,29};
            case "W":
                return new int[]{30,31,32};
            case "A":
                return new int[]{33,34,35};
            case "*":
                return new int[]{36,37,38};
            case "-":
                return new int[]{60,61,62};
            case "Helix":
                return new int[]{63,64,65};
            case "Sheet":
                return new int[]{66,67,68};
            default:
                    return new int[]{0,1,2};
        }
    }

    public static PhongMaterial loadAcidTexture(){
        PhongMaterial m = new PhongMaterial();
        m.setDiffuseMap(new Image("file:./res/textures/acid_texture.png"));
        return m;
    }
}
