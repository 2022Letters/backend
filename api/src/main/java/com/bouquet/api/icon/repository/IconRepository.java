package com.bouquet.api.icon.repository;

import com.bouquet.api.icon.dto.Icon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IconRepository extends JpaRepository<Icon, Long> {
    // 기본 아이콘 찾기
    List<Icon> findAllByIsDefaultIsTrue();
}
