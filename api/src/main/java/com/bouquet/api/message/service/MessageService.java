package com.bouquet.api.message.service;

import com.bouquet.api.message.dto.Message;
import com.bouquet.api.message.dto.MessageRequest;
import com.bouquet.api.message.dto.MessageResponse;
import com.bouquet.api.message.repository.MessageRepository;
import com.bouquet.api.post.dto.Post;
import com.bouquet.api.post.exception.PostNotFoundException;
import com.bouquet.api.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final PostRepository postRepository;
    public MessageResponse.OnlyId create(MessageRequest.Create request) {
        Post post = postRepository.findById(request.getPostId()).orElseThrow(PostNotFoundException::new);
        Message message = Message.create(request, post);
        Message savedMessage = messageRepository.save(message);
        return MessageResponse.OnlyId.build(savedMessage);
    }
}
