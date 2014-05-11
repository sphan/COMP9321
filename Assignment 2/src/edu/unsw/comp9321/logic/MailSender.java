package edu.unsw.comp9321.logic;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServletRequest;

public class MailSender {
	private String host;
	private String recipient;
	private Session session;
	private String username;
	private String password;
	
	public MailSender() {
		this.host = "smtp.gmail.com";
		this.recipient = "";
		this.username = "com9321.test@gmail.com";
		this.password = "admintest";
		
		Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", host);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
        "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");
		
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
		   }
		};
		
		session = Session.getDefaultInstance(props, auth);
		session.setDebug(true);
	}
	
	public void sendMail(String recipient, String custFirstName, String bookingCode, HttpServletRequest request) {
		setRecipient(recipient);
		
		try {
			String url = request.getRequestURL().toString();
			String trimmed_url = "";
			if (null != url && url.length() > 0 )
			{
			    int endIndex = url.lastIndexOf("/");
			    if (endIndex != -1)  
			    {
			        trimmed_url = url.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
			    }
			} 
			trimmed_url += "/URL/" + bookingCode;
			
			String emailContent = "Dear " + custFirstName + " ,\n\n" +
					"\tThank you for booking with us.\n\n" +
					"\tPlease visit " + trimmed_url + " for your booking details.\n\n" +
					"\tPlease take note that, you are allowed to visit the page unlimited number of times " +
					"48 hours prior to your booking date. You are free to add any bookings " +
					"but you are not allowed to remove any bookings.";
			
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(username));
			
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO,
			                          new InternetAddress(recipient));
			
			// Set Subject: header field
			message.setSubject("Your Booking Receipt at Hotel");
			
			// Now set the actual message
			message.setText(emailContent);
			
			Transport t = session.getTransport("smtp");
			
//			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (Exception e) {
			
		}
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getHost() {
		return host;
	}
}
