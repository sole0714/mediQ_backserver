package org.example.mediqback.Bookmark;

import lombok.RequiredArgsConstructor;
import org.example.mediqback.Bookmark.model.Bookmark;
import org.example.mediqback.Bookmark.model.BookmarkDto;
import org.example.mediqback.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    public BookmarkDto.Res register(User user, BookmarkDto.Reg dto) {
        Bookmark entity = bookmarkRepository.save(dto.toEntity(user));
        return BookmarkDto.Res.from(entity);
    }

    public BookmarkDto.Res read(Long idx) {
        Bookmark bookmark = bookmarkRepository.findById(idx).orElseThrow(
                () -> new RuntimeException()
        );
        return BookmarkDto.Res.from(bookmark);
    }

}
