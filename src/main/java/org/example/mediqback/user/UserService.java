package org.example.mediqback.user;

import org.example.mediqback.common.exception.BaseException;
import org.example.mediqback.user.model.AuthUserDetails;
import org.example.mediqback.user.model.EmailVerify;
import org.example.mediqback.user.model.User;
import org.example.mediqback.user.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.example.mediqback.common.model.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService { // 시큐리티 인터페이스
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 도구
    private final EmailService emailService;
    private final EmailVerifyRepository emailVerifyRepository;

    public UserDto.SignupRes signup(UserDto.SignupReq dto) {
        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw BaseException.from(SIGNUP_DUPLICATE_EMAIL);
        }

        User user = dto.toEntity();
        // 비밀번호 암호화해서 저장
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);

        // 이메일 전송
        String uuid = UUID.randomUUID().toString();
        //emailService.sendWelcomeMail(uuid, dto.getEmail());

        EmailVerify emailVerify = EmailVerify.builder().email(dto.getEmail()).uuid(uuid).build();
        emailVerifyRepository.save(emailVerify);

        return UserDto.SignupRes.from(user);
    }

    // 시큐리티가 로그인할 때 DB에서 회원을 찾는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> BaseException.from(LOGIN_INVALID_USERINFO)
        );
        return AuthUserDetails.from(user);
    }

    public void verify(String uuid) {
        EmailVerify emailVerify = emailVerifyRepository.findByUuid(uuid).orElseThrow(
                () -> BaseException.from(SIGNUP_INVALID_UUID)
        );
        User user = userRepository.findByEmail(emailVerify.getEmail()).orElseThrow();
        user.setEnable(true);
        userRepository.save(user);
    }
}