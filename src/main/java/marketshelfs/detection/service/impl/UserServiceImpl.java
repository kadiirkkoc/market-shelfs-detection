package marketshelfs.detection.service.impl;

import marketshelfs.detection.beans.AuthenticationResponse;
import marketshelfs.detection.dtos.AuthenticationDto;
import marketshelfs.detection.model.User;
import marketshelfs.detection.repository.UserRepository;
import marketshelfs.detection.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse authenticate(AuthenticationDto request){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
            if (!userOptional.isPresent()) {
                throw new UsernameNotFoundException("No user found with email: " + request.getUsername());
            }

            String token = jwtService.generateToken(userOptional);
            return AuthenticationResponse.builder()
                    .token(token)
                    .userRole(userRepository.findByUsername(request.getUsername()).get().getUserRole())
                    .build();

        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Incorrect username or password");
        }
    }
}
