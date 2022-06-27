package com.bouquet.api.message.service;

import com.bouquet.api.message.dto.Message;
import com.bouquet.api.message.dto.MessageRequest;
import com.bouquet.api.message.dto.MessageResponse;
import com.bouquet.api.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageResponse.OnlyId create(MessageRequest.Create request) {
        Message message = Message.create(request);
        Message savedMessage = messageRepository.save(message);
        return MessageResponse.OnlyId.build(savedMessage);
    }
}
