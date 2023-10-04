package com.sunil.utils;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender javaMailSender;
	
	
	public boolean sendEmail(File file) {
		boolean status= false;
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			
			helper.setTo("sunildutt6@hotmail.com");
			helper.setSubject("Report");
			helper.setText("<h2>Your report</h2>", true);
			helper.addAttachment(file.getName(), file);
			javaMailSender.send(msg);
			
			
			status=true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return status;
	}
}
