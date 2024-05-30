package marketshelfs.detection.service.impl;

import marketshelfs.detection.beans.AuthenticationResponse;
import marketshelfs.detection.dtos.IndividualUserDto;
import marketshelfs.detection.enums.UserRole;
import marketshelfs.detection.loggers.MainLogger;
import marketshelfs.detection.loggers.messages.IndividualUserMessage;
import marketshelfs.detection.model.IndividualUser;
import marketshelfs.detection.model.User;
import marketshelfs.detection.repository.IndividualUserRepository;
import marketshelfs.detection.repository.UserRepository;
import marketshelfs.detection.security.JwtService;
import marketshelfs.detection.service.IndividualUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IndividualUserImpl implements IndividualUserService {

    private final IndividualUserRepository individualUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final MainLogger logger = new MainLogger(IndividualUserImpl.class);

    public IndividualUserImpl(IndividualUserRepository individualUserRepository,
                              BCryptPasswordEncoder passwordEncoder,
                              UserRepository userRepository,
                              JwtService jwtService) {
        this.individualUserRepository = individualUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public List<IndividualUserDto> getAll() {
        // Retrieve all individual users and convert them to DTOs
        return individualUserRepository.findAll().stream()
                .map(user -> IndividualUserDto.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phone(user.getPhone())
                        .dailyLimit(user.getDailyLimit())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public IndividualUserDto getIndividualUserById(Long id) {
        // Retrieve individual user by ID and convert to DTO
        Optional<IndividualUser> user = individualUserRepository.findById(id);
        return user.map(x -> IndividualUserDto.builder()
                        .firstName(x.getFirstName())
                        .lastName(x.getLastName())
                        .phone(x.getPhone())
                        .dailyLimit(x.getDailyLimit())
                        .build())
                .orElseGet(() -> {
                    // Log an error message if user not found
                    logger.log(IndividualUserMessage.NOT_FOUND + id, HttpStatus.BAD_REQUEST);
                    return null;
                });
    }

    @Override
    @Transactional
    public AuthenticationResponse addIndividualUser(IndividualUserDto individualUserDto) {
        // Encode the user's password
        String username = individualUserDto.getUsername();
        String password = passwordEncoder.encode(individualUserDto.getPassword());

        // Create a new User object
        User user = User.builder()
                .username(username)
                .password(password)
                .userRole(UserRole.INDIVIDUAL)
                .dailyLimit(3)
                .uuid(UUID.randomUUID().toString())
                .build();

        // Create a new IndividualUser object
        IndividualUser newIndividualUser = IndividualUser.builder()
                .firstName(individualUserDto.getFirstName())
                .lastName(individualUserDto.getLastName())
                .username(username)
                .password(password)
                .phone(individualUserDto.getPhone())
                .dailyLimit(3)
                .userRole(UserRole.INDIVIDUAL)
                .user(user)
                .uuid(UUID.randomUUID().toString())
                .build();

        // Set the individual user for the User object
        user.setIndividualUser(newIndividualUser);

        // Save the User object, which also saves the IndividualUser object
        userRepository.save(user);

        // Return an authentication response with the generated token
        return AuthenticationResponse.builder()
                .userRole(newIndividualUser.getUserRole())
                .token(jwtService.generateToken(Optional.ofNullable(user)))
                .dailyLimit(newIndividualUser.getDailyLimit())
                .build();
    }

    @Override
    public String updateIndividualUser(Long id, IndividualUserDto individualUserDto) {
        // Retrieve individual user by ID
        Optional<IndividualUser> user = individualUserRepository.findById(id);
        if (!user.isPresent()) {
            // Log an error message if user not found
            return logger.log(IndividualUserMessage.NOT_FOUND + id, HttpStatus.BAD_REQUEST);
        }
        // Update user details
        user.get().setFirstName(individualUserDto.getFirstName());
        user.get().setLastName(individualUserDto.getLastName());
        user.get().setPhone(individualUserDto.getPhone());
        user.get().setUsername(individualUserDto.getUsername());
        individualUserRepository.save(user.get());
        // Return success message
        return IndividualUserMessage.UPDATE + user.get().getId();
    }

    @Override
    public String deleteIndividualUser(Long id) {
        // Delete individual user by ID
        individualUserRepository.deleteById(id);
        // Return success message
        return IndividualUserMessage.DELETE + id;
    }
}
