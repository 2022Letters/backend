package com.bouquet.api.icon.web;

import com.bouquet.api.icon.dto.IconResponse;
import com.bouquet.api.icon.service.IconService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class IconController {
    private final IconService iconService;

    @GetMapping(value = {"/icons/{iconId}", "/icons"})
    public ResponseEntity<IconResponse.GetIcons> getIcons(@PathVariable("iconId") Optional<Long> id) {
        IconResponse.GetIcons response = iconService.getIcons();
        if (id.isPresent()) {
            response = iconService.getIcons(id.orElse(Long.valueOf(1)));
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/icon/{iconId}")
    public ResponseEntity<IconResponse.GetIcon> getIcon(@PathVariable("iconId") Long id) {
        IconResponse.GetIcon response = iconService.getIcon(id);
        return ResponseEntity.ok().body(response);
    }
}
