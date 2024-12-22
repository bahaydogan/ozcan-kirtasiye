package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    JavaMailSenderImpl mailSender;

    //bunlar configrutaion ile yapılabilirdi.
    @Value("${ozcan-kirtasiye.email.username}")
    String username;

    @Value("${ozcan-kirtasiye.email.host}")
    String host;

    @Value("${ozcan-kirtasiye.email.port}")
    int port;

    @Value("${ozcan-kirtasiye.client.host}")
    String clientHost;

    @Value("${ozcan-kirtasiye.email.password}")
    String password;

    @Value("${ozcan-kirtasiye.email.from}")
    String from;


    @PostConstruct
    public void init() {
        this.mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.starttls.enable", "true");
    }

    public void sendActivationEmail(String email, String activationToken) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);

        message.setTo(email);
        message.setSubject("Activation email");
        message.setText(clientHost + "/activation/" +activationToken);//react app açılcak

        this.mailSender.send(message);
    }


}
