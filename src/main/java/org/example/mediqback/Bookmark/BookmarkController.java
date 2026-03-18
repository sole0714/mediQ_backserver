package org.example.mediqback.Bookmark;

import lombok.RequiredArgsConstructor;
import org.example.mediqback.Bookmark.model.BookmarkDto;
import org.example.mediqback.user.model.AuthUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)
@RequestMapping("/bookmark")
@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;


    @PostMapping("/register")
    public ResponseEntity<BookmarkDto.Res> register(
            @AuthenticationPrincipal AuthUserDetails userDetails,
            @RequestBody BookmarkDto.Reg dto) {
        BookmarkDto.Res response = bookmarkService.register(userDetails.toEntity(), dto);
        return ResponseEntity.ok(response);
    }

}
