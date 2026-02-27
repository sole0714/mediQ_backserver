package org.example.mediqback.user;



import lombok.RequiredArgsConstructor;
import org.example.mediqback.user.model.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @GetMapping("/verify")
    public ResponseEntity verify(String uuid) {

        userService.verify(uuid);

        return ResponseEntity.ok("asd");
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserDto.SignupReq dto) {
        userService.signup(dto);

        return ResponseEntity.ok("성공");
    }

}