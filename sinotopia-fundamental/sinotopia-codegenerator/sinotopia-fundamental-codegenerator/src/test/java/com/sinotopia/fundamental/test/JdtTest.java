package com.hkfs.fundamental.test;

import com.hkfs.fundamental.codegenerator.basis.data.Clazz;
import com.hkfs.fundamental.codegenerator.output.MultiCodeOutputer;
import com.hkfs.fundamental.codegenerator.recovery.JdtProcessor;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhoubing on 2016/4/28.
 */
public class JdtTest {
    public static void main(String[] args) throws Exception {
        testFile();
    }

    private static void testFile() {
        JdtProcessor processor = JdtProcessor.newInstance();

        String path = "C:\\Users\\john\\Desktop\\IdentityAuthController.java";

        Clazz cls = null;
        cls = processor.process(path);
        System.out.println(cls);
    }

    private static void testFolder() {
        String root = "F:\\temp1";
        String originRoot = "F:\\works\\dingjidai\\djd-v2-git\\djd-business\\djd-business-creditloan\\djd-business-creditloan-impl\\src\\main\\java";

        JdtProcessor processor = JdtProcessor.newInstance();

        List<String> fileList = new LinkedList<String>();
        getFileList(fileList, new File(originRoot), "java");
        Clazz cls = null;
        for (String path : fileList) {
            cls = processor.process(path);
            MultiCodeOutputer.newInstance(root, cls.getPackageName()).setGiveupOutputIfExists(false).setClearFolderBeforeOutput(false).output(cls);
        }
    }

    private static void getFileList(List<String> fileList, File folder, String fileExtension) {
        if (!folder.isDirectory()) {
            return;
        }
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                getFileList(fileList, file, fileExtension);
                continue;
            }
            if (!file.isFile() || !file.getName().endsWith(fileExtension)) {
                continue;
            }
            fileList.add(file.getAbsolutePath());
        }
    }



}
