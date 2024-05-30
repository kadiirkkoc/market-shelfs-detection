package marketshelfs.detection.service.impl;

import marketshelfs.detection.enums.UserRole;
import marketshelfs.detection.model.User;
import marketshelfs.detection.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserCredentialsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserCredentialsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve the user by username from the repository
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        // Map user roles to authorities and return a UserDetails object
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(),
                user.get().getPassword(), mapRolesToAuthorities(Collections.singleton(user.get().getUserRole())));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<UserRole> roles) {
        // Convert user roles to GrantedAuthority objects
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }
}
