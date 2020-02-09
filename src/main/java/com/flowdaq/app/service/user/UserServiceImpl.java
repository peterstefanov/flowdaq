package com.flowdaq.app.service.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flowdaq.app.model.Address;
import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.response.AddressItem;
import com.flowdaq.app.model.response.UserItem;
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
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userEntity = userRepository.findOneByUsername(username);
        		
		if (!userEntity.isPresent()) {
            throw new UsernameNotFoundException(username);
        }				
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

	@Override
	public List<UserItem> findAllDistributors() {		
		return processResult(userRepository.findAllByRole(Role.distributor));
	} 
	
	private List<UserItem> processResult(List<User> list) {
		
		List<UserItem> result = new ArrayList<>();
		for (User item : list) {
			UserItem resultItem = new UserItem();
			
			resultItem.setFirstName(item.getFirstName());
			resultItem.setLastName(item.getLastName());
			resultItem.setUserId(item.getUsername());
			resultItem.setEmail(item.getEmailAddress());
			resultItem.setDistributorName(item.getDistributor().getDistributorName());
			resultItem.setDistributorId(item.getDistributorId());

			Address address = item.getDistributor().getDeliveryAddress();
			if (address != null) {
				AddressItem addressItem = AddressItem.builder()
						.id(address.getId())
						.addressLine1(address.getAddressLine1())
						.addressLine2(address.getAddressLine2())
						.addressLine3(address.getAddressLine3())
						.city(address.getCity())
						.state(address.getState())
						.country(address.getCountry())
						.postalCode(address.getPostalCode())
						.build();
				resultItem.setAddress(addressItem);
			}
			result.add(resultItem);
		}		
		return result;
	}
}
