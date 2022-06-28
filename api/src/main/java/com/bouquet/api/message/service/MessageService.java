package com.bouquet.api.message.service;

import com.bouquet.api.icon.dto.Icon;
import com.bouquet.api.icon.exception.IconNotFoundException;
import com.bouquet.api.icon.repository.IconRepository;
import com.bouquet.api.message.dto.Message;
import com.bouquet.api.message.dto.MessageRequest;
import com.bouquet.api.message.dto.MessageResponse;
import com.bouquet.api.message.exception.MessageNotFoundException;
import com.bouquet.api.message.repository.MessageRepository;
import com.bouquet.api.post.dto.Post;
import com.bouquet.api.post.exception.PostNotFoundException;
import com.bouquet.api.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final PostRepository postRepository;
    private final IconRepository iconRepository;
    public MessageResponse.OnlyId create(MessageRequest.Create request) {
        Post post = postRepository.findById(request.getPostId()).orElseThrow(PostNotFoundException::new);
        Icon icon = iconRepository.findById(request.getIconId()).orElseThrow(IconNotFoundException::new);
        Message message = Message.create(request, post, icon);
        Message savedMessage = messageRepository.save(message);
        return MessageResponse.OnlyId.build(savedMessage);
    }
    // TODO: 로그인한 사용자만 볼 수 있도록 수정 필요
    public MessageResponse.GetMessage getMessage(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);
        return MessageResponse.GetMessage.build(message);
    }
    public MessageResponse.GetMessages getMessages(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        // TODO: 아래 두 줄 GetPost에 추가
        int count = messageRepository.countAllByPost(post);
        List<Message> messages = messageRepository.findAllByPost(post);
        return MessageResponse.GetMessages.build(count, messages);
    }
}
