package marketshelfs.detection.service.impl;

import marketshelfs.detection.dtos.CorporateUserDto;
import marketshelfs.detection.repository.CorporateUserRepository;
import marketshelfs.detection.service.CorporateUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CorporateUserImpl implements CorporateUserService{

    private final CorporateUserRepository corporateUserRepository;

    public CorporateUserImpl(CorporateUserRepository corporateUserRepository) {
        this.corporateUserRepository = corporateUserRepository;
    }


    @Override
    public List<CorporateUserDto> getAll() {
        return List.of();
    }

    @Override
    public CorporateUserDto getCorporateUserById(Long id) {
        return null;
    }

    @Override
    public String addCorporateUser(CorporateUserDto corporateUserDto) {
        return "";
    }

    @Override
    public String updateCorporateUser(Long id, CorporateUserDto corporateUserDto) {
        return "";
    }

    @Override
    public String deleteCorporateUser(Long id) {
        return "";
    }
}
