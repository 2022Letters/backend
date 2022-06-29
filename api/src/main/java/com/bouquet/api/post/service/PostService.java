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

    @Transactional
    public PostResponse.OnlyId create(PostRequest.Create request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(UserNotFoundException::new);
        Post post = Post.create(request, user);
        Post savedPost = postRepository.save(post);
        return PostResponse.OnlyId.build(savedPost);
    }

    public PostResponse.GetPost getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        int count = messageRepository.countAllByPost(post);
        List<Message> messages = messageRepository.findAllByPost(post);
        return PostResponse.GetPost.build(post, count, messages);
    }

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

    @Transactional
    public PostResponse.OnlyId update(Long id, PostRequest.Create request) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        // TODO: 유저 확인 필요 if(request.getUserId() != post.getUserId()) 유저 다르다는 오류
        post.setDate(request.getDate());
        post.setTitle(request.getTitle());
        post.setVisibility(request.isVisibility());

        Post savedPost = postRepository.save(post);
        return PostResponse.OnlyId.build(savedPost);
    }

    @Transactional
    public PostResponse.OnlyId delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        // TODO: 유저 확인 필요 if(request.getUserId() != post.getUserId()) 유저 다르다는 오류
        postRepository.deleteById(id);
        return PostResponse.OnlyId.build(post);
    }

}
