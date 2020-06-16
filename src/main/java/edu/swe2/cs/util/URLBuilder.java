package edu.swe2.cs.util;

import edu.swe2.cs.config.ConfigProperties;

public class URLBuilder {

    private static final String FILE_PREFIX = "file:";

    /**
     * Used to build path
     *
     * @param pathElements Array of path elements
     * @return Complete path
     */
    public static String buildURLString(String[] pathElements) {
        StringBuilder stringBuilder = new StringBuilder(SystemProperties.USER_DIR);
        for (String elem : pathElements) {
            stringBuilder.append(SystemProperties.FILE_SEPARATOR);
            stringBuilder.append(elem);
        }
        return stringBuilder.toString();
    }

    /**
     * Get image path from
     *
     * @param imgName Name of the image in directory
     * @return Image Path
     */
    public static String getImgPath(String imgName) {
        return buildURLString(new String[]{"src", "main", "resources", ConfigProperties.getProperty("folderName")}) +
                SystemProperties.FILE_SEPARATOR + imgName;
    }

    /**
     * Get prepared image path - used for JavaFX with prefix "file:"
     *
     * @param imgName Name of the image in directory
     * @return Prepared image path
     */
    public static String getPreparedImgPath(String imgName) {
        return FILE_PREFIX + SystemProperties.FILE_SEPARATOR + getImgPath(imgName);
    }
}
