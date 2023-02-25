package pl.inzynierka.schronisko.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.inzynierka.schronisko.configurations.appsettings.UserServiceSettings;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserServiceSettings settings;
	private final MongoTemplate mongoTemplate;
	private final FindAndModifyOptions findAndModifyOptions;

	public User createUser(final User user) throws UserServiceException {
		final String encodedPassword = this.passwordEncoder.encode(user.getPassword());
		user.setJoined(LocalDate.now());
		user.setPassword(encodedPassword);

		try {
			return this.userRepository.save(user);
		} catch (final DuplicateKeyException e) {
			throw new UserServiceException("User with that username or email already exists!");
		} catch (final Exception e) {
			throw new UserServiceException(e);
		}

	}

	public Page<User> getUsers(final Pageable pageable) {
		return this.userRepository.findAll(pageable);
	}

	public Optional<User> findByUsername(final String username) {
		return this.userRepository.findByUsername(username);
	}

	public User updateUser(final String username, final User user, final User userDetails) throws UserServiceException {
		if (!username.equals(userDetails.getUsername()) && !userDetails.getRoles().contains(Role.ADMIN))
			throw new UserServiceException("Insufficient permissions to edit this user!");

		final ObjectMapper ob = new ObjectMapper();
		final Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username));
		final Update update = new Update();

		final Map<String, Object> userFieldsMap = ob.convertValue(user, Map.class);
		userFieldsMap.entrySet().stream()
				.filter(keyValue -> this.settings.getEditableFields().contains(keyValue.getKey()))
				.filter(keyValue -> Objects.nonNull(keyValue.getValue()))
				.forEach(keyValue -> update.set(keyValue.getKey(), keyValue.getValue()));

		final User modifiedUser = this.mongoTemplate.findAndModify(query, update, this.findAndModifyOptions, User.class);

		if (null == modifiedUser)
			throw new UserServiceException("User for modifying has not been found!");

		return modifiedUser;
	}
}
