package edu.unsw.comp9321.logic;

import java.util.Properties;
import java.util.TimerTask;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServletRequest;

public class MailSender extends TimerTask {
	private String host;
	private String recipient;
	private Session session;
	private String username;
	private String password;
	
	private String destEmail;
	private String firstName;
	private String code;
	private int pin;
	private HttpServletRequest request;
	
	
	public MailSender() {
		this.host = "smtp.cse.unsw.edu.au";
		this.recipient = "";
		this.username = "jhua488";
		this.password = "e35a844k";
		
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
	
	public MailSender(String email, String firstName, String code, int pin,
			HttpServletRequest request) {
		this();
		this.destEmail = email;
		this.firstName = firstName;
		this.code = code;
		this.pin = pin;
		this.request = request;
	}

	public void sendMail(String recipient, String custFirstName, String bookingCode, int pin, HttpServletRequest request) {
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
					"Thank you for booking with us.\n\n" +
					"Please visit " + trimmed_url + " for your booking details.\n\n" +
					"You will require a pin number to login.\n\n" +
					"Your pin number is: " + pin + "\n\n" + 
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

	public void run() {
		sendMail(this.destEmail, this.firstName, this.code, this.pin, this.request);
		
	}
}
