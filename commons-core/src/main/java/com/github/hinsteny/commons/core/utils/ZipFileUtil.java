package com.github.hinsteny.commons.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * zip file operate method utils
 *
 * @author Hinsteny
 * @version $ID: ZipFileUtils 2019-03-18 15:38 All rights reserved.$
 */
public class ZipFileUtil {

    private static final Logger LOGGER = LogManager.getLogger(ZipFileUtil.class);

    private static final String ZIP = ".zip";

    /**
     * 把一个文件夹中的文件添加到一个指定的压缩文件中
     * @param fileFolder 需要被压缩的文件夹
     * @param destZipFilePath 压缩后的压缩文件路径
     * @throws IOException
     */
    public static void zipFile(String fileFolder, String destZipFilePath) throws IOException {
        File fileToZip = new File(fileFolder);
        if (!destZipFilePath.endsWith(ZIP)) {
            destZipFilePath = destZipFilePath.concat(File.separator).concat(fileToZip.getName()).concat(ZIP);
        }
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(destZipFilePath))){
            zipFile(fileToZip, fileToZip.getName(), zipOut);
        } catch (IOException e) {
            LOGGER.warn("Unzip file exception", e);
            throw e;
        }
    }

    /**
     * 压缩文件
     * @param fileToZip 目标压缩文件
     * @param fileName
     * @param zipOut
     * @throws IOException
     */
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (judgeIsFolder(fileName)) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + File.separator));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + File.separator + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    /**
     * 解压文件到指定目录 解压后的文件名，和之前一致
     *
     * @param zipFilePath 待解压的zip文件
     * @param destDir 指定目录
     * @param inheritFolder 是否解压到子目录
     */
    public static String unZipFiles(String zipFilePath, String destDir, boolean inheritFolder) throws IOException {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists()) {
            dir.mkdirs();
        }
        ZipInputStream zis = null;
        File pathFile;
        try {
            File zip = new File(zipFilePath);
            // inherit zip file name folder
            if (inheritFolder) {
                destDir += zip.getName().substring(zip.getName().lastIndexOf('\\') + 1, zip.getName().lastIndexOf('.'));
            }
            pathFile = new File(destDir);
            // make sure the unzip files folder is exist
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            zis = new ZipInputStream(new FileInputStream(zip));
            //buffer for read and write data to file
            byte[] buffer = new byte[1024];
            // do loop to deal zip child files
            while (true) {
                ZipEntry ze = zis.getNextEntry();
                if (ze == null) {
                    break;
                }
                String fileName = ze.getName();
                File newFile = new File(pathFile + File.separator + fileName);
                if (judgeIsFolder(fileName)) {
                    //create directories for sub directories in zip
                    newFile.mkdirs();
                    continue;
                }
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                } finally {
                    //close this ZipEntry
                    zis.closeEntry();
                }
            }
        } catch (IOException e) {
            LOGGER.warn("Unzip file exception", e);
            throw e;
        } finally {
            try {
                //close inputStream
                if (null != zis) {
                    zis.close();
                }
            } catch (IOException e) {
                LOGGER.warn("Close zis stream exception", e);
            }
        }
        return pathFile.getPath();
    }

    /**
     * 判断文件名是否为文件夹
     * @param fileName
     * @return
     */
    private static boolean judgeIsFolder(String fileName) {
        return fileName.endsWith(File.separator);
    }

}
