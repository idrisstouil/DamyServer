package com.damy.app.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import com.damy.app.services.EmailService;

import com.damy.app.shared.dto.UserDto;


@Component
public class EmailServiceImpl implements EmailService {

	
    @Autowired
	 private JavaMailSender javaMailSender;
	    
	 public void sendEmail(UserDto user) {

	        SimpleMailMessage msg = new SimpleMailMessage();
	        msg.setTo(user.getEmail());

	        msg.setSubject("Bienvenue chez Damy");
	        msg.setText("Bonjour "+user.getLastName()+" \n"
	        		+ "Merci pour ton inscription, démarre l’expérience dès maintenant"+" \n"
	        		+ "Ton Email :"+user.getEmail()+"\n"
	        		+ "Ton mot de pass : ***********\n"
	        		+ "Visiter nous sur https://oussama08.github.io/test/");

	        javaMailSender.send(msg);

	        
	        SimpleMailMessage msg2 = new SimpleMailMessage();
	        
	        msg2.setTo("damysolution@gmail.com");

	        msg2.setSubject(user.getFirstName()+" "+user.getLastName() +"a cree un compte");
	        msg2.setText(user.getFirstName()+" "+user.getLastName() +"a cree un compte"
	        		+ "\n Nom:"+user.getFirstName()+""
	        		+ "\n Prenom:"+user.getLastName()+""
	        		+ "\n Ville:"+user.getCity()+""
	        		+ "\n Email:"+user.getEmail()+""
	        		+ "\n Type de compte:"+user.getRole()+""
	        		+ "\n Telephone:"+user.getTelephone()+"");

	        javaMailSender.send(msg2);

	    }	

	
	
}
