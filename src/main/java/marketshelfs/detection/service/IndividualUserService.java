package marketshelfs.detection.service;

import marketshelfs.detection.beans.AuthenticationResponse;
import marketshelfs.detection.dtos.IndividualUserDto;

import java.util.List;

public interface IndividualUserService {

    List<IndividualUserDto> getAll();
    IndividualUserDto getIndividualUserById(Long id);
    AuthenticationResponse addIndividualUser(IndividualUserDto individualUserDto);
    String updateIndividualUser(Long id, IndividualUserDto individualUserDto);
    String deleteIndividualUser(Long id);
}
