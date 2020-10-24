package com.flowdaq.app.service.resetpassword;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.flowdaq.app.model.ResetPasswordRequest;
import com.flowdaq.app.model.User;
import com.flowdaq.app.repository.ResetPasswordRequestRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResetPasswordRequestServiceImpl implements ResetPasswordRequestService{

    private Integer validForSeconds = 60 * 60 * 3;
    
    private final JavaMailSender emailSender;
    private final ResetPasswordRequestRepository repository;
    private String port;
    private String noreplyEmail;
    
	public ResetPasswordRequestServiceImpl(ResetPasswordRequestRepository repository, JavaMailSender emailSender, @Value("${server.server.port}") final String port, @Value("${spring.mail.username}") final String noreplyEmail) {
		this.repository = repository;
		this.emailSender = emailSender;
		this.port = port;
		this.noreplyEmail = noreplyEmail;
	}

	@Transactional
    public void delete(String requestCode, User user) {
    	try {
    		repository.deleteByRequestCode(requestCode);
    		log.info(MessageFormat.format("Deleted reset password request for requesCode : {0}", requestCode));
    		
    		sendConfirmationResetPasswordMail(user);
    		log.info(MessageFormat.format("Password reset confirmation email sent to : {0}", user.getEmailAddress()));
    	} catch (Exception e) {
    		log.error(MessageFormat.format("Error deleting reset password request for requesCode : {0}", requestCode), e);
    	}        
    }

	@Override
	public ResetPasswordRequest getResetPasswordRequest(String requestCode) {

		ResetPasswordRequest resetPasswordRequest = repository.findOneByRequestCode(requestCode);
		if (resetPasswordRequest != null) {
			Date validUntil = DateUtils.addSeconds(resetPasswordRequest.getCreatedAt(), validForSeconds);

			if (resetPasswordRequest.getCreatedAt().before(validUntil))
				return resetPasswordRequest;
		}
		return null;
	}
    
	@Override
	public void createResetPasswordRequest(User user) {
		String url = getURL(user);
		final String messageBody = "\nYou can use the following link to reset your password: \n\n" + url + "\n\nIf you don’t use this link within 3 hours, it will expire. To get a new password reset link, visit ... \n\nPlease do not reply to this email. \n\nThanks,\nThe Flowdaq Team";
		
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom(this.noreplyEmail);
        message.setTo(user.getEmailAddress()); 
        message.setSubject("Flowdaq  Please reset your password"); 
        message.setText(messageBody);
        
		log.info(MessageFormat.format("Email to be send: {0}", message));

        emailSender.send(message);
	}
	
	private void sendConfirmationResetPasswordMail(User user) {
		final String messageBody = "Hello " + user.getUsername() + ",\n\nWe wanted to let you know that your Flowdaq password was reset. \n\nPlease do not reply to this email with your password. We will never ask for your password, and we strongly discourage you from sharing it with anyone.\n\nThanks,\nThe Flowdaq Team";
		
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom(this.noreplyEmail);
        message.setTo(user.getEmailAddress()); 
        message.setSubject("Flowdaq Your password was reset"); 
        message.setText(messageBody);
        
		log.info(MessageFormat.format("Confirmation Reset password email to be send: {0}", message));

        emailSender.send(message);
	}
	
    private String getURL(User user) {
 
    	HttpServletRequest request = getCurrentHttpRequest();
    	
    	String requestCode = createRequestCode(user.getUsername());
    	
        StringBuilder url = new StringBuilder();

        url.append(request.getScheme());
        url.append("://");
        url.append(request.getServerName());
        url.append(":");
        url.append(this.port);
        url.append("/#/resetpassword/");                
        
        try {
            url.append(URLEncoder.encode(requestCode, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return url.toString();
    }
    
    private String createRequestCode(final String userId) {

        final Date date = new Date();
        final String requestCode = generateCode();

        final ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setCreatedAt(date);
        resetPasswordRequest.setUsername(userId);
        resetPasswordRequest.setRequestCode(requestCode);

    	ResetPasswordRequest result = repository.saveAndFlush(resetPasswordRequest);
    	
    	if (result != null) {
    		log.info(MessageFormat.format("Reset password request created for user : {0}", result != null ? result.getUsername() : "unknown"));
    	} else {
    		log.info(MessageFormat.format("Failed to create reset password request for user : {0}", result != null ? result.getUsername() : "unknown"));
    	}

        return requestCode;
    }
    
    private String generateCode() {
        final Random random = new Random();
        final int rand = random.nextInt();
        final long now = System.currentTimeMillis();
        long randLong = now * rand;

        if (randLong < 0) {
            randLong = randLong * -1;
        }

        return Long.toString(randLong);
    }
    
    public static HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            return request;
        }
        log.info("Not called in the context of an HTTP request");
        return null;
    }
    
}
