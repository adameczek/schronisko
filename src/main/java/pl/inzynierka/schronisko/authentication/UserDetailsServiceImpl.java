package pl.inzynierka.schronisko.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.inzynierka.schronisko.configurations.security.UserPrincipal;
import pl.inzynierka.schronisko.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws
          UsernameNotFoundException {
      return userRepository
              .findFirstByEmail(email)
              .map(UserPrincipal::new)
              .orElseThrow(() -> new UsernameNotFoundException(email));
  }
}
