package marketshelfs.detection.controller;


import marketshelfs.detection.beans.AuthenticationResponse;
import marketshelfs.detection.dtos.CorporateUserDto;
import marketshelfs.detection.service.CorporateUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/corporate/user")
public class CorporateUserController {

    private final CorporateUserService corporateUserService;

    public CorporateUserController(CorporateUserService corporateUserService) {
        this.corporateUserService = corporateUserService;
    }

    @GetMapping
    public ResponseEntity<List<CorporateUserDto>> getAllCorporateUsers() {
        return new ResponseEntity<>(corporateUserService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CorporateUserDto> getCorporateUserById(@PathVariable Long id) {
        return new ResponseEntity<>(corporateUserService.getCorporateUserById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<AuthenticationResponse> addCorporateUser(@RequestBody CorporateUserDto corporateUserDto) {
        return new ResponseEntity<>(corporateUserService.addCorporateUser(corporateUserDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCorporateUser(@PathVariable Long id, @RequestBody CorporateUserDto corporateUserDto) {
        return new ResponseEntity<>(corporateUserService.updateCorporateUser(id, corporateUserDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCorporateUser(@PathVariable Long id) {
        return new ResponseEntity<>(corporateUserService.deleteCorporateUser(id), HttpStatus.OK);
    }
}

