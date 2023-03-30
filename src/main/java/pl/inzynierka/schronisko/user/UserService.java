package pl.inzynierka.schronisko.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(User user) throws UserServiceException {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setJoined(LocalDateTime.now());
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

    public User updateUser(String email, UserUpdateRequest newUserData) throws
            UserServiceException {
        Optional<User> optionalUser = userRepository.findFirstByEmail(email);

        if (optionalUser.isEmpty()) throw new UserServiceException(
                "Użytkownik z podanym mailem nie istnieje!");

        if (newUserData.getPassword() != null) newUserData.setPassword(
                passwordEncoder.encode(newUserData.getPassword()));

        User updatedUserData = optionalUser.get();

        try {
            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration()
                    .setSkipNullEnabled(true)
                    .setMatchingStrategy(MatchingStrategies.STRICT);

            mapper.map(newUserData, updatedUserData);
            updatedUserData.setUpdated(LocalDateTime.now());

        } catch (Exception e) {
            e.printStackTrace();
            throw new UserServiceException(
                    "Wystąpił błąd przy aktualizowaniu danych użytkownika");
        }

        return userRepository.save(updatedUserData);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }
}
