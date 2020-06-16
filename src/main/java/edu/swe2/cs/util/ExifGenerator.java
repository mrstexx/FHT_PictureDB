package edu.swe2.cs.util;

import edu.swe2.cs.model.Exif;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ExifGenerator {

    private static final List<String> CAMERA_LIST = Arrays.asList("Canon EOS", "Panasonic Lumix DC-GH", "Sony Alpha R", "Nikon D750", "Fujifilm GFX");
    private static final List<String> LENS_LIST = Arrays.asList("Carl Zeiss Apo Sonnar", "Nikon AF-S", "Sigma Art", "Tokina Firin 2", "Sony GM");

    /**
     * Create random exif data: timestamp, camera type, lens type
     *
     * @return Random Exif Data
     */
    public static Exif getExif() {
        Random random = new Random();
        Date captureDate = new Timestamp(System.currentTimeMillis());
        String camera = CAMERA_LIST.get(random.nextInt(CAMERA_LIST.size()));
        String lens = LENS_LIST.get(random.nextInt(LENS_LIST.size()));
        return new Exif(camera, lens, captureDate);
    }
}
