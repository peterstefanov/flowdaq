package com.flowdaq.app.service.user;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flowdaq.app.model.User;
import com.flowdaq.app.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
    public static final int PASSWORD_POLICY_MINIMUM_LENGTH = 8;
    public static final String PASSWORD_POLICY_RULE = "(?=.*[a-z])(?=.*[A-Z])((?=.*[^A-Za-z0-9])|(?=.*[0-9]))";
    private static final int PASSWORD_POLICY_MINIMUM_RULES_TO_PASS = 3;
    
    @Autowired
    private UserRepository userRepository;
    
    private PasswordEncoder passwordEncoder;

	@Override
	public User save(User user) {
		
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
		
        return userRepository.save(user);
	}

	@Override
	public User update(User user) {		
        return userRepository.save(user);
	}
	
	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findOneByUsername(username);
	}

	@Override
	public Optional<User> findByEmailAddress(String email) {		
		return userRepository.findOneByEmailAddress(email);
	}
	
	@Override
	public User findByUsernameAndPassword(String id, String password) {
		final User user = userRepository.findOneByUsernameAndPassword(id, password).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return user;
	}


	@Override
	public Optional<User> findByDistributorId(Long distributorId) {
		return userRepository.findOneByDistributorId(distributorId);
	}
	
	@Override
	public void deleteUser(User user) {
	    userRepository.delete(user);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userEntity = userRepository.findOneByUsername(username);
        		
		if (!userEntity.isPresent()) {
            throw new UsernameNotFoundException(username);
        }		
		
		if (userEntity.isPresent() && !userEntity.get().isEnabled()) {
            throw new DisabledException("User account is disabled");
        }
		
		//check for locked account
        return userEntity.get();
	}

    public boolean isValidPassword(User user, String password) {
        if (password.equals("")) {
            return false;
        }
        
        Pattern policyRule = Pattern.compile(PASSWORD_POLICY_RULE);
        return policyRule.matcher(password).find() && (password.length() >= PASSWORD_POLICY_MINIMUM_LENGTH);
    }
    
    public boolean isPasswordTheSame(User user, String password) {
        boolean samePassword = passwordEncoder.matches(password, user.getPassword());
        return samePassword;
    }

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

}
