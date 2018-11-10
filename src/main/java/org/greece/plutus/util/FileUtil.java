package org.greece.plutus.util;

import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


/**
 * Created by hujiahua on 2018/11/10.
 */
public class FileUtil {

    private static Logger log = LoggerFactory.getLogger(FileUtil.class);
    
    public static boolean mkdirIfNotExist(String dir) {
        try {
            File f = new File(dir);
            if (!f.exists()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    public static boolean containFile(String filePath) {
        try {
            File f = new File(filePath);
            if (f.exists() && f.isFile()) {
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public static boolean containDirectory(String dirPath) {
        try {
            File f = new File(dirPath);
            if (f.exists() && f.isDirectory()) {
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public static boolean removeDirIfExists(String dir) {
        if (containDirectory(dir)) {
            return removeDir(dir);
        }
        return true;
    }

    public static boolean removeDir(String dir) {
        try {
            File dirFile = new File(dir);
            File[] subFiles;
            if (dirFile.isDirectory() && (subFiles = dirFile.listFiles()) != null) {
                for (File file : subFiles) {
                    if (file.isFile()) {
                        if (!file.delete()) {
                            return false;
                        } else {
                            log.warn("delete file:" + file.getAbsolutePath());
                        }
                    } else {
                        return false;
                    }
                }
                log.warn("delete dir:" + dirFile.getAbsolutePath());
                return dirFile.delete();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public static boolean removeFileIfExists(String path) {
        if (containFile(path)) {
            return removeFile(path);
        }
        return true;
    }

    public static boolean removeFile(String path) {
        try {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                if (!file.delete()) {
                    return false;
                } else {
                    log.warn("delete file:" + file.getAbsolutePath());
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }


    public static String stripEmailDomain(String email) {
        int sepIndex = email.lastIndexOf("@");
        if (sepIndex > 0) {
            return email.substring(0, sepIndex);
        } else {
            return email;
        }
    }

    /**
     * 保存文件到本地
     * 一般用于暂存文件
     *
     * @param path
     * @param fileName
     * @param sw
     * @throws IOException
     */
    public static void saveFile(String path, String fileName, StringWriter sw) throws IOException {
        FileWriter fw = null;
        try {
            fw = new FileWriter(path + fileName);
            fw.write(sw.toString());
        } catch (Throwable throwable) {

        } finally {
            if (fw != null) {
                fw.flush();
                fw.close();
            }
        }
    }

    /**
     * 文件流保存的到本地
     *
     * @param fileName
     * @param dir
     * @param is
     * @return
     */
    public static boolean copyToDir(String fileName, String dir, InputStream is) {
        try {
            Path path = Paths.get(dir, fileName);
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Failed to copy inputStream to local file", e);
            return false;
        }
        return true;
    }

    public static void copyFile(final File srcFile, final File destFile) throws IOException {
        org.apache.commons.io.FileUtils.copyFile(srcFile,destFile);
    }


    /**
     * 删除文件
     *
     * @param absPathFile
     * @return
     */
    public static boolean deleteFile(String absPathFile) {
        try {
            File file = new File(absPathFile);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception ex) {
            log.error("Failed to deleteFile ", ex);
            return false;
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @param fileName
     * @param dir
     * @return
     */
    public static boolean deleteFile(String fileName, String dir) {
        try {
            Path path = Paths.get(dir, fileName);
            Files.delete(path);
        } catch (IOException e) {
            log.error("Failed to delete file ", e);
            return false;
        }
        return true;
    }

    /**
     * 创建文件,如果路径不存在则创建路径
     *
     * @param fullPathFile
     * @return
     */
    public static File createFileIfNotExist(String fullPathFile) {
        File file = null;
        try {
            file = new File(fullPathFile);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            log.error("Create file failed!!", e);
            return null;
        }
        return file;
    }

    /**
     * 文件/文件件是否存在
     *
     * @param fullPathFile
     * @return
     */
    public static boolean isFileExist(String fullPathFile) {
        try {
            File file = new File(fullPathFile);
            return file.exists();
        } catch (Exception e) {
            log.error("Check file exist failed!! ", e);
        }
        return false;
    }

    /**
     * 包装apache的FileUtils
     *
     * @param file
     * @param data
     * @param append
     */
    public static void writeStringToFile(File file, String data, boolean append) throws IOException {
        org.apache.commons.io.FileUtils.writeStringToFile(file, data, "UTF-8", append);
    }

    public static void writeStringToFile(File file, String data) throws IOException {
        writeStringToFile(file, data, false);
    }

    public static String readFileToString(String filePath) {
        if (isFileExist(filePath)) {
            try {
                return org.apache.commons.io.FileUtils.readFileToString(new File(filePath), Charsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * @param fullPathFile
     * @return
     */
    public static boolean tryOpenFile(String fullPathFile) {
        RandomAccessFile stream = null;
        try {
            stream = new RandomAccessFile(new File(fullPathFile), "rw");
            File file = new File(fullPathFile);
            //            System.out.println(file.renameTo(new File("/tmp/upload/test.log")));
            return true;
        } catch (Exception e) {
            log.info("Skipping file [{}] for this iteration due it's not completely written", fullPathFile);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    log.error("Exception during closing file {}", fullPathFile);
                }
            }
        }
        return false;
    }


    public static void main(String[] args) throws InterruptedException {
        String filePath = "/tmp/upload/";

        boolean res = mkdirIfNotExist(filePath);

        System.out.println(res);
    }


}
