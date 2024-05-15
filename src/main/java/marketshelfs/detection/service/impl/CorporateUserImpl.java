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
public class CorporateUserImpl implements CorporateUserService{

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
        return corporateUserRepository.findAll().stream()
                .map(user -> CorporateUserDto.builder()
                        .marketName(user.getMarketName())
                        .branchNumber(user.getBranchNumber())
                        .password(user.getPassword())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public CorporateUserDto getCorporateUserById(Long id) {
        Optional<CorporateUser> user = corporateUserRepository.findById(id);
        return user.map(x -> CorporateUserDto.builder()
                        .marketName(x.getMarketName())
                        .branchNumber(x.getBranchNumber())
                        .build())
                .orElseGet(()->{
                    logger.log(CorporateUserMessage.NOT_FOUND + id, HttpStatus.BAD_REQUEST);
                    return null;
                });
    }

    @Override
    public AuthenticationResponse addCorporateUser(CorporateUserDto corporateUserDto) {

        String username = corporateUserDto.getMarketName()+corporateUserDto.getBranchNumber();
        String password = passwordEncoder.encode(corporateUserDto.getPassword());

        User user = User.builder()
                .username(username)
                .password(password)
                .userRole(UserRole.CORPORATE)
                .dailyLimit(3)
                .build();

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

        user.setCorporateUser(newCorporateUser);
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(Optional.ofNullable(user)))
                .build();

    }

    @Override
    public String updateCorporateUser(Long id, CorporateUserDto corporateUserDto) {
        Optional<CorporateUser> user = corporateUserRepository.findById(id);
        if (!user.isPresent()) {
            logger.log(CorporateUserMessage.NOT_FOUND + id, HttpStatus.BAD_REQUEST);
        }
        user.get().setMarketName(corporateUserDto.getBranchNumber());
        user.get().setBranchNumber(corporateUserDto.getBranchNumber());
        user.get().setUsername(corporateUserDto.getMarketName()+corporateUserDto.getBranchNumber());
        corporateUserRepository.save(user.get());
        return CorporateUserMessage.UPDATE + user.get().getId();
    }

    @Override
    public String deleteCorporateUser(Long id) {
        corporateUserRepository.deleteById(id);
        return CorporateUserMessage.DELETE + id;
    }
}
