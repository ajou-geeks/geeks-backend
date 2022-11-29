package com.geeks.geeksbackend.util;

public class CommonUtils {

    private static final String FILE_EXTENSION_SEPARATOR = ".";

    public static String buildFileName(String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String now = String.valueOf(System.currentTimeMillis());
        System.out.println(fileName + now + fileExtension);

        return fileName + now + fileExtension;
    }
}
