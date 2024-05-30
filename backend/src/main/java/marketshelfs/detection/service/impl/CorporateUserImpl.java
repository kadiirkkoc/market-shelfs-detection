package marketshelfs.detection.service.impl;

import marketshelfs.detection.beans.AuthenticationResponse;
import marketshelfs.detection.dtos.CorporateUserDto;
import marketshelfs.detection.enums.UserRole;
import marketshelfs.detection.loggers.MainLogger;
import marketshelfs.detection.loggers.messages.CorporateUserMessage;
import marketshelfs.detection.model.CorporateUser;
import marketshelfs.detection.model.User;
import marketshelfs.detection.repository.CorporateUserRepository;
import marketshelfs.detection.repository.UserRepository;
import marketshelfs.detection.security.JwtService;
import marketshelfs.detection.service.CorporateUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CorporateUserImpl implements CorporateUserService {

    private final CorporateUserRepository corporateUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final MainLogger logger = new MainLogger(CorporateUserImpl.class);

    public CorporateUserImpl(CorporateUserRepository corporateUserRepository,
                             BCryptPasswordEncoder passwordEncoder,
                             UserRepository userRepository,
                             JwtService jwtService) {
        this.corporateUserRepository = corporateUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public List<CorporateUserDto> getAll() {
        // Retrieve all corporate users and convert them to DTOs
        return corporateUserRepository.findAll().stream()
                .map(user -> CorporateUserDto.builder()
                        .marketName(user.getMarketName())
                        .branchNumber(user.getBranchNumber())
                        .password(user.getPassword())
                        .dailyLimit(user.getDailyLimit())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public CorporateUserDto getCorporateUserById(Long id) {
        // Retrieve corporate user by ID and convert to DTO
        Optional<CorporateUser> user = corporateUserRepository.findById(id);
        return user.map(x -> CorporateUserDto.builder()
                        .marketName(x.getMarketName())
                        .branchNumber(x.getBranchNumber())
                        .dailyLimit(x.getDailyLimit())
                        .build())
                .orElseGet(() -> {
                    // Log an error message if user not found
                    logger.log(CorporateUserMessage.NOT_FOUND + id, HttpStatus.BAD_REQUEST);
                    return null;
                });
    }

    @Override
    public AuthenticationResponse addCorporateUser(CorporateUserDto corporateUserDto) {
        // Create a unique username by combining market name and branch number
        String username = corporateUserDto.getMarketName() + corporateUserDto.getBranchNumber();
        // Encode the user's password
        String password = passwordEncoder.encode(corporateUserDto.getPassword());

        // Create a new User object
        User user = User.builder()
                .username(username)
                .password(password)
                .userRole(UserRole.CORPORATE)
                .dailyLimit(3)
                .build();

        // Create a new CorporateUser object
        CorporateUser newCorporateUser = CorporateUser.builder()
                .marketName(corporateUserDto.getMarketName())
                .branchNumber(corporateUserDto.getBranchNumber())
                .username(username)
                .password(password)
                .phone(corporateUserDto.getPhone())
                .dailyLimit(10)
                .userRole(UserRole.CORPORATE)
                .user(user)
                .uuid(UUID.randomUUID().toString())
                .build();

        // Set the corporate user for the User object
        user.setCorporateUser(newCorporateUser);
        // Save the User object, which also saves the CorporateUser object
        userRepository.save(user);

        // Return an authentication response with the generated token
        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(Optional.ofNullable(user)))
                .dailyLimit(newCorporateUser.getDailyLimit())
                .build();
    }

    @Override
    public String updateCorporateUser(Long id, CorporateUserDto corporateUserDto) {
        // Retrieve corporate user by ID
        Optional<CorporateUser> user = corporateUserRepository.findById(id);
        if (!user.isPresent()) {
            // Log an error message if user not found
            logger.log(CorporateUserMessage.NOT_FOUND + id, HttpStatus.BAD_REQUEST);
        }
        // Update user details
        user.get().setMarketName(corporateUserDto.getMarketName());
        user.get().setBranchNumber(corporateUserDto.getBranchNumber());
        user.get().setUsername(corporateUserDto.getMarketName() + corporateUserDto.getBranchNumber());
        corporateUserRepository.save(user.get());
        // Return success message
        return CorporateUserMessage.UPDATE + user.get().getId();
    }

    @Override
    public String deleteCorporateUser(Long id) {
        // Delete corporate user by ID
        corporateUserRepository.deleteById(id);
        // Return success message
        return CorporateUserMessage.DELETE + id;
    }
}
