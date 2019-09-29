package com.flowdaq.app.controller;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowdaq.app.model.ResetPasswordRequest;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.request.ForgotPassword;
import com.flowdaq.app.model.request.PasswordResetRequest;
import com.flowdaq.app.model.response.Response;
import com.flowdaq.app.model.response.Response.ResponseStatusEnum;
import com.flowdaq.app.model.response.UserItem;
import com.flowdaq.app.service.resetpassword.ResetPasswordRequestService;
import com.flowdaq.app.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public")
public class ForgotPasswordController {

	public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+.)+[a-zA-Z]{2,7}$";

	@Autowired
	private UserService userService;

	@Autowired
	private ResetPasswordRequestService requestService;

	private static Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

	@PostMapping(value = "/forgotpassword", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response forgotPassword(@RequestBody ForgotPassword forgotPassword, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Response resp = new Response();
		final String userInput = forgotPassword.getEmail();

		try {
			Optional<User> user = null;
			// Email entered
			if (emailPattern.matcher(userInput).find()) {
				user = userService.findByEmailAddress(userInput);
			} else { // User ID entered
				user = userService.findByUsername(userInput);
			}
			
			if (!user.isPresent()) {
				validateEmailAddress(userInput);
			}

			if (!user.isPresent()) {
				log.error(MessageFormat.format("No account for this user/email address : %s", userInput));
				String responseMessage = "No account for this email address.";
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage(responseMessage);
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return resp;
			} else {
				requestService.createResetPasswordRequest(user.get());
			}

		} catch (AddressException ae) {
			log.error(ae.getMessage());
			String responseMessage = "Input is an invalid email address";
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(responseMessage);
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return resp;
		}
		resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
		resp.setMessage("Reset Password link sent to " + userInput);

		return resp;
	}

	@GetMapping(value = "/resetpassword/{code}")
	public Response validateRequestPasswordCode(@PathVariable("code") String code, HttpServletRequest request,
			HttpServletResponse response) {
		Response resp = new Response();

		if (!StringUtils.isBlank(code)) {

			ResetPasswordRequest resetPasswordRequest = requestService.getResetPasswordRequest(code);

			if (resetPasswordRequest != null) {
				Optional<User> user = userService.findByUsername(resetPasswordRequest.getUsername());
				if (!user.isPresent()) {
					log.info(" no user found ");
					String responseMessage = "No user found";
					resp.setOperationStatus(ResponseStatusEnum.ERROR);
					resp.setMessage(responseMessage);
					response.setStatus(HttpServletResponse.SC_CONFLICT);

				} else {
					UserItem respItem = new UserItem();

					respItem.setUserId(user.get().getUsername());

					resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
					resp.setMessage("Reset code valid");
					resp.setItem(respItem);
					response.setStatus(HttpServletResponse.SC_OK);
				}
			} else {
				log.info("Reset password code is invalid");
				String responseMessage = "Reset password code is invalid";
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage(responseMessage);
				response.setStatus(HttpServletResponse.SC_CONFLICT);
			}
		}
		return resp;
	}

	@PostMapping(value = "/resetpassword/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response validatePassword(@RequestBody PasswordResetRequest passwordResetRequest, HttpServletRequest request,
			HttpServletResponse response) {
		Response resp = new Response();
		UserItem respItem = new UserItem();
		try {
			Optional<User> user = userService.findByUsername(passwordResetRequest.getUsername());
			if (!user.isPresent()) {
				log.info("No user found");
				String responseMessage = "No user found";
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage(responseMessage);
				response.setStatus(HttpServletResponse.SC_CONFLICT);

			} else {

				if (!userService.isValidPassword(user.get(), passwordResetRequest.getPassword())) {
					log.info("Password must be between 8 and 20 characters. It must contain lower and upper case letters and either numeric or special characters.");
					respItem.setUserId(user.get().getUsername());

					resp.setOperationStatus(ResponseStatusEnum.ERROR);
					resp.setMessage("Password must be between 8 and 20 characters. It must contain lower and upper case letters and either numeric or special characters.");
					resp.setItem(respItem);
					response.setStatus(HttpServletResponse.SC_CONFLICT);
				} else if (userService.isPasswordTheSame(user.get(), passwordResetRequest.getPassword())) {
					log.info("Password should be different than your current one.");
					respItem.setUserId(user.get().getUsername());

					resp.setOperationStatus(ResponseStatusEnum.ERROR);
					resp.setMessage("Password should be different than your current one.");
					resp.setItem(respItem);
					response.setStatus(HttpServletResponse.SC_CONFLICT);
				} else {
					user.get().setPassword(passwordResetRequest.getPassword());
					userService.save(user.get());
					log.info("Password is valid");

				    requestService.delete(passwordResetRequest.getRequestCode());
					respItem.setUserId(user.get().getUsername());

					resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
					resp.setMessage("Password is valid");
					resp.setItem(respItem);
					response.setStatus(HttpServletResponse.SC_OK);
				}
			}
		} catch (Exception e) {
			log.error(MessageFormat.format("Cannot reset password for user {0}.", passwordResetRequest.getUsername()), e);

			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage("Cannot reset password at the moment, try again later.");
			resp.setItem(respItem);
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
		return resp;
	}
	
	public void validateEmailAddress(String email) throws AddressException {
		InternetAddress emailAddr = new InternetAddress(email);
		emailAddr.validate();
	}
}
