package org.example.mediqback.Bookmark;

import org.example.mediqback.Bookmark.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
