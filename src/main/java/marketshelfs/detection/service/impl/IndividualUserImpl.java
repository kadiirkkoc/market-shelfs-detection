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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IndividualUserImpl implements IndividualUserService{

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
        return individualUserRepository.findAll().stream()
                .map(user -> IndividualUserDto.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phone(user.getPhone())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public IndividualUserDto getIndividualUserById(Long id) {
        Optional<IndividualUser> user = individualUserRepository.findById(id);
        return user.map(x -> IndividualUserDto.builder()
                        .firstName(x.getFirstName())
                        .lastName(x.getLastName())
                        .phone(x.getPhone())
                        .build())
                .orElseGet(() -> {
                    logger.log(IndividualUserMessage.NOT_FOUND + id, HttpStatus.BAD_REQUEST);
                    return null;
                });
    }

    @Override
    @Transactional
    public AuthenticationResponse addIndividualUser(IndividualUserDto individualUserDto) {

        String username = individualUserDto.getUsername();
        String password = passwordEncoder.encode(individualUserDto.getPassword());

        User user = User.builder()
                .username(username)
                .password(password)
                .userRole(UserRole.INDIVIDUAL)
                .dailyLimit(3)
                .uuid(UUID.randomUUID().toString())
                .build();


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

        user.setIndividualUser(newIndividualUser);

        userRepository.save(user);

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(Optional.ofNullable(user)))
                .build();
    }

    @Override
    public String updateIndividualUser(Long id, IndividualUserDto individualUserDto) {
        Optional<IndividualUser> user = individualUserRepository.findById(id);
        if (!user.isPresent()) {
            return logger.log(IndividualUserMessage.NOT_FOUND + id, HttpStatus.BAD_REQUEST);
        }
        user.get().setFirstName(individualUserDto.getFirstName());
        user.get().setLastName(individualUserDto.getLastName());
        user.get().setPhone(individualUserDto.getPhone());
        user.get().setUsername(individualUserDto.getUsername());
        individualUserRepository.save(user.get());
        return IndividualUserMessage.UPDATE + user.get().getId();
    }

    @Override
    public String deleteIndividualUser(Long id) {
        individualUserRepository.deleteById(id);
        return IndividualUserMessage.DELETE + id;
    }
}
