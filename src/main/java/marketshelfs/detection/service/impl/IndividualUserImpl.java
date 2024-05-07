package marketshelfs.detection.service.impl;

import marketshelfs.detection.dtos.IndividualUserDto;
import marketshelfs.detection.repository.IndividualUserRepository;
import marketshelfs.detection.service.IndividualUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndividualUserImpl implements IndividualUserService{

    private final IndividualUserRepository individualUserRepository;

    public IndividualUserImpl(IndividualUserRepository individualUserRepository) {
        this.individualUserRepository = individualUserRepository;
    }

    @Override
    public List<IndividualUserDto> getAll() {
        return List.of();
    }

    @Override
    public IndividualUserDto getIndividualUserById(Long id) {
        return null;
    }

    @Override
    public String addIndividualUser(IndividualUserDto individualUserDto) {
        return "";
    }

    @Override
    public String updateIndividualUser(Long id, IndividualUserDto individualUserDto) {
        return "";
    }

    @Override
    public String deleteIndividyalUser(Long id) {
        return "";
    }
}
