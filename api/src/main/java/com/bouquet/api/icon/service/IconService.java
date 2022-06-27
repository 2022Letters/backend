package com.bouquet.api.icon.service;

import com.bouquet.api.icon.dto.Icon;
import com.bouquet.api.icon.dto.IconResponse;
import com.bouquet.api.icon.exception.IconNotFoundException;
import com.bouquet.api.icon.repository.IconRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IconService {
    private final IconRepository iconRepository;
    public IconResponse.GetIcon getIcon(Long id) {
        Icon icon = iconRepository.findById(id).orElseThrow(IconNotFoundException::new);
        return IconResponse.GetIcon.build(icon);
    }

    public IconResponse.GetIcons getIcons(Long id) {
        List<Icon> iconList = iconRepository.findAllByIsDefaultIsTrue();
        Icon icon = iconRepository.findById(id).orElseThrow(IconNotFoundException::new);
        iconList.add(icon);
        return IconResponse.GetIcons.build(iconList);
    }
    public IconResponse.GetIcons getIcons() {
        List<Icon> iconList = iconRepository.findAllByIsDefaultIsTrue();
        return IconResponse.GetIcons.build(iconList);
    }
}
