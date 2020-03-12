package edu.swe2.cs.util;

import edu.swe2.cs.model.Exif;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ExifGenerator {

    private static List<String> cameraList = null;
    private static List<String> lensList = null;

    public static Exif getExif() {
        checkLists();
        Random random = new Random();
        Date captureDate = new Timestamp(System.currentTimeMillis());
        String camera = cameraList.get(random.nextInt(cameraList.size()));
        String lens = lensList.get(random.nextInt(lensList.size()));
        return new Exif(camera, lens, captureDate);
    }

    private static void checkLists() {
        if (cameraList == null && lensList == null) {
            cameraList = Arrays.asList("Canon EOS", "Panasonic Lumix DC-GH", "Sony Alpha R", "Nikon D750", "Fujifilm GFX");
            lensList = Arrays.asList("Carl Zeiss Apo Sonnar", "Nikon AF-S", "Sigma Art", "Tokina Firin 2", "Sony GM");
        }
    }
}
