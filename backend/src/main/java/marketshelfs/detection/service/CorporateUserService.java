package marketshelfs.detection.service;

import marketshelfs.detection.beans.AuthenticationResponse;
import marketshelfs.detection.dtos.CorporateUserDto;

import java.util.List;

public interface CorporateUserService {

    List<CorporateUserDto> getAll();
    CorporateUserDto getCorporateUserById(Long id);
    AuthenticationResponse addCorporateUser(CorporateUserDto corporateUserDto);
    String updateCorporateUser(Long id, CorporateUserDto corporateUserDto);
    String deleteCorporateUser(Long id);
}
