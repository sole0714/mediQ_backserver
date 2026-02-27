package org.example.mediqback.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.example.mediqback.user.model.EmailVerify;
import org.example.mediqback.user.model.User;
import org.example.mediqback.user.model.UserDto;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailVerifyRepository emailVerifyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public void signup(UserDto.SignupReq dto) {
        User user = dto.toEntity();
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);

        // 이메일 전송
        MimeMessage message  = mailSender.createMimeMessage();
        try {
            String uuid = UUID.randomUUID().toString();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(dto.getEmail());
            String subject = "[안녕] 환영";
            String htmlContents = "<a href='http://localhost:8080/user/verify?uuid="+uuid+"'>이메일 인증</a>";
            helper.setSubject(subject);
            helper.setText(htmlContents, true);

            mailSender.send(message);

            EmailVerify emailVerify = EmailVerify.builder().email(dto.getEmail()).uuid(uuid).build();
            emailVerifyRepository.save(emailVerify);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송중 오류발생", e);
        }



    }
    public void verify(String uuid) {
        EmailVerify emailVerify = emailVerifyRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 인증 코드입니다."));

        User user = userRepository.findByEmail(emailVerify.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입된 사용자를 찾을 수 없습니다."));

        // 계정 활성화
        user.setEnable(true);
        userRepository.save(user);
    }
}