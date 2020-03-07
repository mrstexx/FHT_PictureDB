package edu.swe2.cs.util;

public class URLBuilder {

    public static String buildURLString(String[] pathElements) {
        StringBuilder stringBuilder = new StringBuilder(SystemProperties.USER_DIR);
        for (String elem : pathElements) {
            stringBuilder.append(SystemProperties.FILE_SEPARATOR);
            stringBuilder.append(elem);
        }
        return stringBuilder.toString();
    }
}
