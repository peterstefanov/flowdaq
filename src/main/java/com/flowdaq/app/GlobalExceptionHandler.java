package com.flowdaq.app;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowdaq.app.model.response.Response;
import com.flowdaq.app.model.response.Response.ResponseStatusEnum;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BadCredentialsException.class)
    public Response handleBadCredentialsException(BadCredentialsException exception, HttpServletResponse response) throws Exception{
    	
        Response resp = new Response();
        resp.setOperationStatus(ResponseStatusEnum.ERROR);
        resp.setMessage(exception.getMessage());
               
		ObjectMapper ow = new ObjectMapper();	
		
		log.error(exception.getMessage());
		
        String jsonRespString = ow.writeValueAsString(resp);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonRespString);
        response.getWriter().flush();
        response.getWriter().close();
        
        return resp;
    }
	
    @ExceptionHandler(value = ExpiredJwtException.class)
    public Response handleExpiredJwtException(ExpiredJwtException exception, HttpServletResponse response) throws Exception{
    	
        Response resp = new Response();
        resp.setOperationStatus(ResponseStatusEnum.ERROR);
        resp.setMessage(exception.getMessage());
               
		ObjectMapper ow = new ObjectMapper();	
		
		log.error(exception.getMessage());
		
        String jsonRespString = ow.writeValueAsString(resp);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(jsonRespString);
        response.getWriter().flush();
        response.getWriter().close();
        
        return resp;
    }
    
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad request") 
    //@ExceptionHandler(value = Exception.class)
    public Response handleBaseException(Exception exception){
        Response resp = new Response();
        resp.setOperationStatus(ResponseStatusEnum.ERROR);
        resp.setMessage(exception.getMessage());
        return resp;
    }
}