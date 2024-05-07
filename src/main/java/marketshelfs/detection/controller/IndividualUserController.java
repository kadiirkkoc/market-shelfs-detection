package marketshelfs.detection.controller;

import marketshelfs.detection.dtos.IndividualUserDto;
import marketshelfs.detection.service.IndividualUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/individual/user")
public class IndividualUserController {

    private final IndividualUserService individualUserService;

    public IndividualUserController(IndividualUserService individualUserService) {
        this.individualUserService = individualUserService;
    }

    @GetMapping
    public ResponseEntity<List<IndividualUserDto>> getAllIndividualUsers() {
        return new ResponseEntity<>(individualUserService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndividualUserDto> getIndividualUserById(@PathVariable Long id) {
        return new ResponseEntity<>(individualUserService.getIndividualUserById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> addIndividualUser(@RequestBody IndividualUserDto individualUserDto ) {
        return new ResponseEntity<>(individualUserService.addIndividualUser(individualUserDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateIndividualUser(@PathVariable Long id, @RequestBody IndividualUserDto individualUserDto ) {
        return new ResponseEntity<>(individualUserService.updateIndividualUser(id,individualUserDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIndividualUser(@PathVariable Long id) {
        return new ResponseEntity<>(individualUserService.deleteIndividualUser(id), HttpStatus.OK);
    }
}
