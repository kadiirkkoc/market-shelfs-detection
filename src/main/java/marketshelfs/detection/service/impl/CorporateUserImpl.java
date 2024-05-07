package marketshelfs.detection.service.impl;

import marketshelfs.detection.dtos.CorporateUserDto;
import marketshelfs.detection.loggers.MainLogger;
import marketshelfs.detection.loggers.messages.CorporateUserMessage;
import marketshelfs.detection.model.CorporateUser;
import marketshelfs.detection.repository.CorporateUserRepository;
import marketshelfs.detection.service.CorporateUserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CorporateUserImpl implements CorporateUserService{

    private final CorporateUserRepository corporateUserRepository;
    private final MainLogger logger = new MainLogger(CorporateUserImpl.class);

    public CorporateUserImpl(CorporateUserRepository corporateUserRepository) {
        this.corporateUserRepository = corporateUserRepository;
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
    public String addCorporateUser(CorporateUserDto corporateUserDto) {
        CorporateUser newCorporateUser = CorporateUser.builder()
                .marketName(corporateUserDto.getMarketName())
                .branchNumber(corporateUserDto.getBranchNumber())
                .uuid(UUID.randomUUID().toString())
                .build();
        newCorporateUser = corporateUserRepository.save(newCorporateUser);
        return logger.log(CorporateUserMessage.CREATE + newCorporateUser.getId(), HttpStatus.OK);

    }

    @Override
    public String updateCorporateUser(Long id, CorporateUserDto corporateUserDto) {
        Optional<CorporateUser> user = corporateUserRepository.findById(id);
        if (!user.isPresent()) {
            logger.log(CorporateUserMessage.NOT_FOUND + id, HttpStatus.BAD_REQUEST);
        }
        user.get().setMarketName(corporateUserDto.getBranchNumber());
        user.get().setBranchNumber(corporateUserDto.getBranchNumber());
        corporateUserRepository.save(user.get());
        return CorporateUserMessage.UPDATE + user.get().getId();
    }

    @Override
    public String deleteCorporateUser(Long id) {
        corporateUserRepository.deleteById(id);
        return CorporateUserMessage.DELETE + id;
    }
}
