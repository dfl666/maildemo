package com.dongfl.maildemo;

import com.dongfl.maildemo.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest
class MaildemoApplicationTests {

    @Autowired
    private MailService mailService;

    @Resource
    TemplateEngine templateEngine;

    private static final String TO = "1075608781@qq.com";
    private static final String SUBJECT = "测试邮件";
    private static final String CONTENT = "test content";

    /**
     * 测试发送普通邮件
     */
    @Test
    public void sendSimpleMailMessage() {
        mailService.sendSimpleMailMessge(TO, SUBJECT, CONTENT);
    }

    /**
     * 测试发送普通邮件
     */
    @Test
    public void sendSimpleMailMessage2() {
        mailService.sendSimpleMailMessge2(TO, SUBJECT, CONTENT);
    }

    /**
     * 测试发送html邮件
     */
    @Test
    public void sendHtmlMessage() {
        String htmlStr = "<h1>Test</h1>";
        mailService.sendMimeMessge(TO, SUBJECT, htmlStr);
    }

    /**
     * 测试发送带附件的邮件
     * @throws FileNotFoundException
     */
    @Test
    public void sendAttachmentMessage() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:test.txt");
        String filePath = file.getAbsolutePath();
        mailService.sendMimeMessge(TO, SUBJECT, CONTENT, filePath);
    }

    /**
     * 测试发送带附件的邮件
     * @throws FileNotFoundException
     */
    @Test
    public void sendPicMessage() throws FileNotFoundException {
        String htmlStr = "<html><body>测试：图片1 <br> <img src=\'cid:pic1\'/> <br>图片2 <br> <img src=\'cid:pic2\'/></body></html>";
        Map<String, String> rscIdMap = new HashMap<>(2);
        rscIdMap.put("pic1", ResourceUtils.getFile("classpath:pic01.jpg").getAbsolutePath());
        rscIdMap.put("pic2", ResourceUtils.getFile("classpath:pic02.jpg").getAbsolutePath());
        mailService.sendMimeMessge(TO, SUBJECT, htmlStr, rscIdMap);
    }


    //发送多个附件邮件
    @Test
    public void sendAttachmentsMails() throws MessagingException {

        String filePath = "src\\main\\resources\\pic01.jpg";
        String filePath2 = "src\\main\\resources\\pic01.jpg";

        ArrayList<String> filePaths = new ArrayList();
        filePaths.add(filePath);
        filePaths.add(filePath2);

        mailService.sendAttachmentsMails(TO,"多个附件邮件测试","附件邮件",filePaths);
    }


    @Test
    public void sendImage() throws MessagingException {

        String imagePath = "src\\main\\resources\\pic01.jpg";
        String imageId = "123";
        StringBuilder content = new StringBuilder();
        content.append("<!DOCTYPE html>\n").append("<body>\n").append("<img src=\"").append(imagePath).append("\">\n").append("</body>\n").append("</html>");
        String conStr=content.toString();
        System.out.println(conStr);
        mailService.sendImage(TO,"图片邮件",conStr,imagePath,imageId);
    }


    /**
     * mailTemplate.html这个模板必须放到templates文件下
     * @throws MessagingException
     */
    @Test
    public void templateMail() throws MessagingException {
        Context context = new Context();
        context.setVariable("id","123");

        String emailContent = templateEngine.process("mailTemplate",context);
        mailService.sendHtmlMail(TO,"模板邮件",emailContent);
    }

}
