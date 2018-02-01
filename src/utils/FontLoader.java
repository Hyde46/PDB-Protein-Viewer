package utils;

import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FontLoader {

    public static Font loadCustomFont(String path, int size){
        try {
            // load a custom font from a specific location (change path!)
            // 12 is the size to use
            return Font.loadFont(new FileInputStream(new File(path)), size);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
