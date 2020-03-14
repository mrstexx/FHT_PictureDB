package edu.swe2.cs.util;

import edu.swe2.cs.config.ConfigProperties;

public class URLBuilder {

    public static final String FILE_PREFIX = "file:";

    public static String buildURLString(String[] pathElements) {
        StringBuilder stringBuilder = new StringBuilder(SystemProperties.USER_DIR);
        for (String elem : pathElements) {
            stringBuilder.append(SystemProperties.FILE_SEPARATOR);
            stringBuilder.append(elem);
        }
        return stringBuilder.toString();
    }

    public static String getImgPath(String imgName) {
        return buildURLString(new String[]{"src", "main", "resources", ConfigProperties.getProperty("folderName")}) +
                SystemProperties.FILE_SEPARATOR + imgName;
    }

    public static String getPreparedImgPath(String imgName) {
        return FILE_PREFIX + SystemProperties.FILE_SEPARATOR + getImgPath(imgName);
    }
}
