package org.example.mediqback.user;

import lombok.RequiredArgsConstructor;
import org.example.mediqback.user.model.User;
import org.example.mediqback.user.model.UserDto;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service

public class UserService {
    private final UserRepository userRepository;

    public UserDto.SignupRes signup(UserDto.SignupReq dto) {
        User user = dto.toEntity();
        User savedUser = userRepository.save(user);
        return UserDto.SignupRes.from(savedUser);
    }

    public UserDto.SignupRes findBy(Long idx) {
        User entity = userRepository.findById(idx).orElseThrow();
        return UserDto.SignupRes.from(entity);
    }

    public UserDto.LoginRes login(UserDto.LoginReq dto) {
        User entity = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));
        if (!entity.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return UserDto.LoginRes.from(entity);
    }

}
