package com.bouquet.api.message.web;

import com.bouquet.api.message.dto.MessageRequest;
import com.bouquet.api.message.dto.MessageResponse;
import com.bouquet.api.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/msg")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MessageResponse.OnlyId> create(@RequestBody MessageRequest.Create request) {
        MessageResponse.OnlyId response = messageService.create(request);
        return ResponseEntity.created(URI.create("/api/msg/" + response.getId())).body(response);
    }
    @GetMapping("/msg/{msgId}")
    public ResponseEntity<MessageResponse.GetMessage> getMessage(@PathVariable Long msgId) {
        MessageResponse.GetMessage response = messageService.getMessage(msgId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/msgs/post/{postId}")
    public ResponseEntity<MessageResponse.GetMessages> getMessages(@PathVariable Long postId) {
        MessageResponse.GetMessages response = messageService.getMessages(postId);
        return ResponseEntity.ok().body(response);
    }
}
