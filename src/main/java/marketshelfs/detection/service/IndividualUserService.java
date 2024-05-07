package marketshelfs.detection.service;

import marketshelfs.detection.dtos.CorporateUserDto;
import marketshelfs.detection.dtos.IndividualUserDto;

import java.util.List;

public interface IndividualUserService {

    List<IndividualUserDto> getAll();
    IndividualUserDto getIndividualUserById(Long id);
    String addIndividualUser(IndividualUserDto individualUserDto);
    String updateIndividualUser(Long id, IndividualUserDto individualUserDto);
    String deleteIndividyalUser(Long id);
}
