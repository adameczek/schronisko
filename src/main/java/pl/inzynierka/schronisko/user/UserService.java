package pl.inzynierka.schronisko.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceSettings settings;
    private final MongoTemplate mongoTemplate;
    private final FindAndModifyOptions findAndModifyOptions;

    public User createUser(User user) throws UserServiceException {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setJoined(LocalDate.now());
        user.setPassword(encodedPassword);

        try {
            return userRepository.save(user);
        } catch (DuplicateKeyException e) {
            throw new UserServiceException(
                    "User with that username or email already exists!");
        } catch (Exception e) {
            throw new UserServiceException(e);
        }

    }

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> findByUsername(String username) {
    return userRepository.findFirstByUsername(username);
    }

    public User updateUser(String username, User user, User userDetails) throws
            UserServiceException {
        if (!username.equals(userDetails.getUsername()) && !userDetails.getRoles()
                .contains(Role.ADMIN))
            throw new UserServiceException(
                    "Insufficient permissions to edit this user!");

        ObjectMapper ob = new ObjectMapper();
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        Update update = new Update();

        Map<String, Object> userFieldsMap = ob.convertValue(user, Map.class);
        userFieldsMap.entrySet().stream()
                .filter(keyValue -> settings.getEditableFields()
                        .contains(keyValue.getKey()))
                .filter(keyValue -> Objects.nonNull(keyValue.getValue()))
                .forEach(keyValue -> update.set(keyValue.getKey(),
                                                keyValue.getValue()));

        User modifiedUser = mongoTemplate.findAndModify(query,
                                                        update,
                                                        findAndModifyOptions,
                                                        User.class);

        if (null == modifiedUser)
            throw new UserServiceException(
                    "User for modifying has not been found!");

        return modifiedUser;
    }
}
