package com.master.side.domain.repository;

import com.master.side.domain.model.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FilesRepository extends JpaRepository<FilesEntity, UUID> {

    // fileUrl이 유니크하다고 가정할 경우
    Optional<FilesEntity> findByFileUrl(String fileUrl);

}
