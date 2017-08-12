package com.hkfs.fundamental.common.assist.file;

import com.hkfs.fundamental.common.utils.FileUtils;
import com.hkfs.fundamental.common.utils.IOUtils;
import com.hkfs.fundamental.common.utils.StrUtils;

import java.io.BufferedReader;
import java.io.File;

/**
 * 文本文档一行一行的读取并进行处理
 * Created by zhoubing on 2016/12/14.
 */
public class FileLineReader {
    private String filePath;
    private String charset = StrUtils.UTF_8;
    private FileLineProcessor lineProcessor;

    public FileLineReader(File file, FileLineProcessor lineProcessor) {
        this.filePath = file.getAbsolutePath();
        this.lineProcessor = lineProcessor;
    }
    public FileLineReader(String filePath, FileLineProcessor lineProcessor) {
        this.filePath = filePath;
        this.lineProcessor = lineProcessor;
    }
    public FileLineReader setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public void read() {
        BufferedReader br = null;
        try {
            br = FileUtils.getBufferedReader(filePath, charset);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (lineProcessor != null && !lineProcessor.process(line)) {
                    break;
                }
            }
        }
        catch (Exception e) {
            if (lineProcessor != null) {
                lineProcessor.onException(e);
            }
        }
        finally {
            IOUtils.close(br);
        }
    }
}
