package com.bouquet.api.post.service;

import com.bouquet.api.message.dto.Message;
import com.bouquet.api.message.repository.MessageRepository;
import com.bouquet.api.post.dto.Post;
import com.bouquet.api.post.dto.PostRequest;
import com.bouquet.api.post.dto.PostResponse;
import com.bouquet.api.post.exception.PostNotFoundException;
import com.bouquet.api.post.repository.PostRepository;
import com.bouquet.api.user.dto.User;
import com.bouquet.api.user.exception.UserNotFoundException;
import com.bouquet.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    // 게시글 정보와 유저를 받아 게시글 생성 후 게시글 아이디 리턴
    @Transactional
    public PostResponse.OnlyId create(PostRequest.Create request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(UserNotFoundException::new);
        Post post = Post.create(request, user);
        Post savedPost = postRepository.save(post);
        return PostResponse.OnlyId.build(savedPost);
    }

    // 게시글 아이디를 받아 메시지를 포함하지 않는 게시글 상세 정보 리턴
    public PostResponse.GetPostSet getPostSet(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        return PostResponse.GetPostSet.build(post);
    }

    // 게시글 아이디를 받아 메시지를 포함한 게시글 상세 정보 리턴
    public PostResponse.GetPost getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        int count = messageRepository.countAllByPost(post);
        List<Message> messages = messageRepository.findAllByPost(post);
        return PostResponse.GetPost.build(post, count, messages);
    }

    // 유저 아이디, 목록 모드, 키워드, 카테고리 아이디를 받아 해당 유저의 게시글 목록 리턴(mode = 1 : 전체목록, mode = 2 : 키워드 제목 포함 목록, mode = 3 : 해당 카테고리 목록)
    public PostResponse.GetPosts getPosts(Long userId, int mode, String keyword, int categoryId) {
        List<Post> posts;

        if (mode == 1)
            posts = postRepository.findAllByUserIdOrderByDateDesc(userId);
        else if (mode == 2)
            posts = postRepository.findAllByUserIdAndTitleIsContainingOrderByDateDesc(userId, keyword);
        else
            posts = postRepository.findAllByUserIdAndCategoryIdOrderByDateDesc(userId, categoryId);

        return PostResponse.GetPosts.build(posts);
    }

    // 게시글 아이디와 내용을 받아 해당 게시글 수정 후 아이디 리턴
    @Transactional
    public PostResponse.OnlyId update(Long id, PostRequest.Create request) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        post.setDate(request.getDate());
        post.setTitle(request.getTitle());
        post.setVisibility(request.isVisibility());

        Post savedPost = postRepository.save(post);
        return PostResponse.OnlyId.build(savedPost);
    }

    // 게시글 아이디를 받아 해당 게시글 삭제 후 아이디 리턴
    @Transactional
    public PostResponse.OnlyId delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.deleteById(id);
        return PostResponse.OnlyId.build(post);
    }

}
