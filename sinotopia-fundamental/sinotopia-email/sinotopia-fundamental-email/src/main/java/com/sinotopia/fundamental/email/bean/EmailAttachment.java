package com.sinotopia.fundamental.email.bean;

import java.io.File;
import java.io.Serializable;

/**
 * <p>邮件附件</p>
 */
public class EmailAttachment implements Serializable {

    private String attachmentName;

    private File attachmentFile;

    public EmailAttachment() {
    }

    public EmailAttachment(String attachmentName, File attachmentFile) {
        this.attachmentName = attachmentName;
        this.attachmentFile = attachmentFile;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public File getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(File attachmentFile) {
        this.attachmentFile = attachmentFile;
    }
}
