package com.sinotopia.fundamental.email;

import com.sinotopia.fundamental.email.bean.EmailAttachment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * <p>Email 发送器</p>
 */
public class EmailSender {

    private JavaMailSender javaMailSender;

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * 邮件发送
     *
     * @param subject   标题
     * @param toEmail   收件人
     * @param fromEmail 发件人
     * @param text
     */
    public void send(final String subject, final String toEmail, final String fromEmail, final String text) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "utf-8");
                message.setSubject(subject);
                message.setTo(toEmail);
                message.setFrom(fromEmail);
                message.setText(text, true);
            }
        };
        javaMailSender.send(preparator);
    }

    public void send(final String subject, final String toEmail, final String fromEmail, final String text, final List<EmailAttachment> attachments) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "utf-8");
                message.setSubject(subject);
                message.setTo(toEmail);
                message.setFrom(fromEmail);
                message.setText(text, true);
                if (attachments != null && attachments.size() > 0) {
                    for (EmailAttachment emailAttachment : attachments) {
                        message.addAttachment(emailAttachment.getAttachmentName(), emailAttachment.getAttachmentFile());
                    }
                }
            }
        };
        javaMailSender.send(preparator);
    }

    /**
     * 邮件发送
     *
     * @param preparator
     */
    public void send(MimeMessagePreparator preparator) {
        if (preparator != null) {
            javaMailSender.send(preparator);
        }
    }
}
