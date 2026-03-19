package org.example.mediqback.Bookmark;

import org.example.mediqback.Bookmark.model.Bookmark;
import org.example.mediqback.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUser(User user);
}
