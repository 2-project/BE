package com.github.backendpart.repository;

import com.github.backendpart.web.entity.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<OptionEntity, Integer> {
}
