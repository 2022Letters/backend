package com.bouquet.api.icon.dto;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class IconResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetIcon {
        private Long id;
        private String iconUrl;

        public static GetIcon build(Icon icon) {
            return GetIcon.builder()
                    .id(icon.getId())
                    .iconUrl(icon.getIconUrl())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetIcons {
        private List<GetIcon> icons;

        public static GetIcons build(List<Icon> icons) {
            return GetIcons.builder()
                    .icons(icons.stream().map(GetIcon::build).collect(Collectors.toList()))
                    .build();
        }
    }
}
