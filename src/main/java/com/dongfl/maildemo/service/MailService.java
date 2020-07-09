package com.dongfl.maildemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

@Service
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    private static final String SENDER = "fei1224long@163.com";

    private static final String CCENDER = "dfl6971195@163.com";

    private static final String BCCENDER = "dongfeilong@orientech.cc";

    /**
     * 发送普通邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendSimpleMailMessge(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("发送简单邮件时发生异常!", e);
        }
    }

    /**
     * 发送普通邮件
     * cc 抄送，bcc 暗送
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendSimpleMailMessge2(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER);
        message.setTo(to);
        String[] ccarr=new String[]{"dfl6971195@163.com","dongfeilong@orientech.cc"};
        message.setCc(ccarr);
//        message.setBcc(BCCENDER);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("发送简单邮件时发生异常!", e);
        }
    }

    /**
     * 发送 HTML 邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendMimeMessge(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(SENDER);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("发送MimeMessge时发生异常！", e);
        }
    }

    /**
     * 发送带附件的邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件路径
     */
    public void sendMimeMessge(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(SENDER);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = file.getFilename();
            helper.addAttachment(fileName, file);

            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("发送带附件的MimeMessge时发生异常！", e);
        }
    }

    /**
     * 发送带静态文件的邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param rscIdMap 需要替换的静态文件
     */
    public void sendMimeMessge(String to, String subject, String content, Map<String, String> rscIdMap) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(SENDER);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            for (Map.Entry<String, String> entry : rscIdMap.entrySet()) {
                FileSystemResource file = new FileSystemResource(new File(entry.getValue()));
                helper.addInline(entry.getKey(), file);
            }
            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("发送带静态文件的MimeMessge时发生异常！", e);
        }
    }


    /*
     * 发送多个附件邮件
     * to：发送给谁
     * subject：发送的主题（邮件主题）
     * content：发送的内容
     * filePath：附件路径
     * */
    public void sendAttachmentsMails(String to, String subject, String content, ArrayList filePaths) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content);

        //获取文件路径以及文件名称

        for (Object filepath:filePaths) {
            FileSystemResource fileSystemResource = new FileSystemResource(new File((String) filepath));
            String fileName = fileSystemResource.getFilename();

            mimeMessageHelper.addAttachment(fileName,fileSystemResource);
        }
        mimeMessageHelper.setFrom(SENDER);

        try {
            mailSender.send(mimeMessage);
            logger.info("附件邮件发送成功！");
        } catch (MailException e) {
            logger.error("附件邮件发送失败！",e);
        }
    }


    /*
     * 发送图片件
     * to：发送给谁
     * subject：发送的主题（邮件主题）
     * content：发送的内容
     * imagePath：图片路径
     * imageId：图片ID
     * */
    public void sendImage(String to, String subject, String content, String imagePath, String imageId) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content,true);

        FileSystemResource fileSystemResource = new FileSystemResource(new File(imagePath));

        mimeMessageHelper.addInline(imageId,fileSystemResource);
        mimeMessageHelper.setFrom(SENDER);

        try {
            logger.info("图片邮件发送成功!");
            mailSender.send(mimeMessage);
        } catch (MailException e) {
            logger.error("图片邮件发送失败！",e);
        }
    }

    /*
     * 发送HTML邮件
     * to：发送给谁
     * subject：发送的主题（邮件主题）
     * content：发送的内容
     * */
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {

        MimeMessage mimeMailMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage,true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content,true);
        mimeMessageHelper.setFrom(SENDER);

        try {
            mailSender.send(mimeMailMessage);
            logger.info("HTML邮件发送成功！");
        } catch (MailException e) {
            logger.error("HTML邮件发送失败！",e);
        }
    }
}
