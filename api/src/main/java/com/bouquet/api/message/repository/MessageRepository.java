package com.bouquet.api.message.repository;

import com.bouquet.api.message.dto.Message;
import com.bouquet.api.post.dto.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // 메시지 개수
    Integer countAllByPost(Post post);
    // 메세지 목록 출력
    List<Message> findAllByPost(Post post);
    // 메시지 상세 보기
    Optional<Message> findByPost(Post post);
}
