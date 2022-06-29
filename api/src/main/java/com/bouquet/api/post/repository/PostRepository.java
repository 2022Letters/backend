package com.bouquet.api.post.repository;

import com.bouquet.api.post.dto.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    //전체 조회
    List<Post> findAllByUserIdOrderByDateDesc(Long userId);

    // 제목 조회
    List<Post> findAllByUserIdAndTitleIsContainingOrderByDateDesc(Long userId, String title);

    // 카테고리 조회
    List<Post> findAllByUserIdAndCategoryIdOrderByDateDesc(Long userId, int categoryId);
}
