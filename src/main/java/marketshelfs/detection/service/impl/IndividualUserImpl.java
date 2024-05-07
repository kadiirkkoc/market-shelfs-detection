package marketshelfs.detection.service.impl;

import marketshelfs.detection.dtos.IndividualUserDto;
import marketshelfs.detection.loggers.MainLogger;
import marketshelfs.detection.loggers.messages.IndividualUserMessage;
import marketshelfs.detection.model.IndividualUser;
import marketshelfs.detection.repository.IndividualUserRepository;
import marketshelfs.detection.service.IndividualUserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IndividualUserImpl implements IndividualUserService{

    private final IndividualUserRepository individualUserRepository;
    private final MainLogger logger = new MainLogger(IndividualUserImpl.class);

    public IndividualUserImpl(IndividualUserRepository individualUserRepository) {
        this.individualUserRepository = individualUserRepository;
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
    public String addIndividualUser(IndividualUserDto individualUserDto) {
        IndividualUser newIndividualUser = IndividualUser.builder()
                .firstName(individualUserDto.getFirstName())
                .lastName(individualUserDto.getLastName())
                .phone(individualUserDto.getPhone())
                .uuid(UUID.randomUUID().toString())
                .build();
        newIndividualUser = individualUserRepository.save(newIndividualUser);
        return logger.log(IndividualUserMessage.CREATE + newIndividualUser.getId(), HttpStatus.OK);
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
        individualUserRepository.save(user.get());
        return IndividualUserMessage.UPDATE + user.get().getId();
    }

    @Override
    public String deleteIndividualUser(Long id) {
        individualUserRepository.deleteById(id);
        return IndividualUserMessage.DELETE + id;
    }

}
