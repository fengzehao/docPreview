package com.github.docpreview.config;

import java.io.File;

public final class ConfigUtils {

    private static String fileFolderPath;

    public static String getFilePath() {
        if (fileFolderPath == null) {
            String dir = System.getenv("FILE_FOLDER");
            //noinspection ReplaceNullCheck
            if (dir == null) {
                fileFolderPath = System.getProperty("user.dir") + File.separator + "file" + File.separator;
            } else {
                fileFolderPath = dir;
            }
        }
        return fileFolderPath;
    }
}
