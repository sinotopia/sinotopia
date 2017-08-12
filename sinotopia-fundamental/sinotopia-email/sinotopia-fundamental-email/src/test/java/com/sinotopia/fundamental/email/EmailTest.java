package com.hkfs.fundamental.email;

import com.hkfs.fundamental.email.bean.EmailAttachment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2016/4/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:applicationContext-*.xml"})
public class EmailTest {

    @Autowired
    private EmailSender emailSender;
    @Test
    public void testSend(){

        EmailAttachment attachment = new EmailAttachment("鼎及贷测试.txt",new File("G:\\test.txt"));
        EmailAttachment attachment1 = new EmailAttachment("鼎及贷测试.jpg",new File("G:\\20160316103609.jpg"));
        final List<EmailAttachment> attachmentList = new ArrayList<EmailAttachment>();
        attachmentList.add(attachment);
        attachmentList.add(attachment1);
        emailSender.send("封装测试","278646564@qq.com","daizhirui<daizr123@126.com>","<p>这是鼎及贷测试的html</p>",attachmentList);
    }

}
