package marketshelfs.detection.controller;


import marketshelfs.detection.beans.AuthenticationResponse;
import marketshelfs.detection.dtos.AuthenticationDto;
import marketshelfs.detection.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserServiceImpl userService;

    public AuthenticationController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationDto request){
        return new ResponseEntity<>(userService.authenticate(request), HttpStatus.CREATED);
    }
}
