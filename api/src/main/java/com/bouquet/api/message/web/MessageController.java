package com.bouquet.api.message.web;

import com.bouquet.api.message.dto.MessageRequest;
import com.bouquet.api.message.dto.MessageResponse;
import com.bouquet.api.message.service.MessageService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "메시지 생성", notes = "{postId : 게시글 식별자, iconId : 아이콘 식별자, nickname : 메시지 작성자 닉네임, content : 내용, x : x좌표, y : y좌표 } 를 전달하면, 생성된 메시지의 식별자를 반환합니다.")
    @PostMapping("/msg")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MessageResponse.OnlyId> create(@RequestBody MessageRequest.Create request) {
        MessageResponse.OnlyId response = messageService.create(request);
        return ResponseEntity.created(URI.create("/api/msg/" + response.getId())).body(response);
    }

    @ApiOperation(value = "메시지 상세 조회", notes = "msgId : 메시지 식별자 를 전달하면, 메시지 상세 정보가 반환됩니다.")
    @GetMapping("/msg/{msgId}")
    public ResponseEntity<MessageResponse.GetMessage> getMessage(@PathVariable Long msgId) {
        MessageResponse.GetMessage response = messageService.getMessage(msgId);
        return ResponseEntity.ok().body(response);
    }

    @ApiOperation(value = "메시지 삭제", notes = "msgId : 메시지 식별자를 전달하면, 메시지가 삭제되고 삭제된 메시지의 식별자가 반환됩니다.")
    @DeleteMapping("/msg/{msgId}")
    public ResponseEntity<MessageResponse.OnlyId> delete(@PathVariable Long msgId) {
        MessageResponse.OnlyId response = messageService.delete(msgId);
        return ResponseEntity.ok().body(response);
    }
}
