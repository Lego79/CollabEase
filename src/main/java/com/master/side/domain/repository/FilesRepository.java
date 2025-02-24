package com.master.side.domain.repository;

import com.master.side.domain.model.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FilesRepository extends JpaRepository<FilesEntity, UUID> {
}
