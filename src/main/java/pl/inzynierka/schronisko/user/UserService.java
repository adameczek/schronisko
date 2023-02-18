package pl.inzynierka.schronisko.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public User createUser(User user) throws UserServiceException {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setJoined(LocalDate.now());
		user.setPassword(encodedPassword);

		try {
			return userRepository.save(user);
		}
		catch (Exception e)
		{
			throw new UserServiceException(e);
		}

	}

	public Page<User> getUsers(Pageable pageable) {
		return userRepository.findAll(pageable);
	}


}
