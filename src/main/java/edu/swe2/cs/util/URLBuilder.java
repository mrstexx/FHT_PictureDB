package edu.swe2.cs.util;

public class URLBuilder {

    public static String buildURLString(String[] pathElements) {
        StringBuilder stringBuilder = new StringBuilder(SystemProperties.userDir);
        for (String elem : pathElements) {
            stringBuilder.append(SystemProperties.fileSeparator);
            stringBuilder.append(elem);
        }
        return stringBuilder.toString();
    }
}
