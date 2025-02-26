package com.master.side.application.service;

import com.master.side.application.dto.FileStorageResult;
import com.master.side.domain.model.Board;
import com.master.side.domain.model.FilesEntity;
import com.master.side.domain.model.Member;
import com.master.side.domain.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final FilesRepository filesRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir, FilesRepository filesRepository) {
        // 절대 경로를 사용하여 저장 위치 설정
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.filesRepository = filesRepository;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 디렉토리를 생성하지 못했습니다.", e);
        }
    }

    /**
     * 파일을 저장하고, Files 엔티티를 생성하여 DB에 기록합니다.
     */
    public FileStorageResult storeFile(MultipartFile file, Board board, Member member) {
        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName;
        try {
            // 저장할 파일의 전체 경로 생성
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String fileUrl = "/files/" + fileName; // 정적 리소스 매핑 URL (추후 정적 리소스 매핑 설정 필요)

            // Files 엔티티 생성 및 저장
            FilesEntity fileEntity = FilesEntity.builder()
                    .board(board)
                    .member(member)
                    .fileName(originalFileName)
                    .fileUrl(fileUrl)
                    .size(file.getSize())
                    .contentType(file.getContentType())
                    .build();
            filesRepository.save(fileEntity);

            return new FileStorageResult(fileUrl, targetLocation.toString());
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다: " + fileName, e);
        }
    }

    public Resource downloadFile(String fileUrl) throws FileNotFoundException {
        // 1) DB에서 fileUrl로 파일 엔티티 조회
        Optional<FilesEntity> optionalFile = filesRepository.findAll()
                .stream()
                .filter(file -> fileUrl.equals(file.getFileUrl()))
                .findFirst();

        if (optionalFile.isEmpty()) {
            throw new FileNotFoundException("해당 fileUrl의 파일 정보를 찾을 수 없습니다: " + fileUrl);
        }

        FilesEntity fileEntity = optionalFile.get();
        // 실제 파일 이름 (DB에 저장된 파일명)
        String actualFileName = fileEntity.getFileName();

        // 2) 로컬 디스크 경로 구성
        //    - 만약 fileUrl 자체가 "C:/Users/KentNa/Desktop/file_db/..." 라면 바로 new FileSystemResource(fileUrl) 사용 가능
        //    - 여기서는 uploadDir + 실제 파일명을 결합한다고 가정
        Path path = Paths.get(uploadDir, actualFileName);
        FileSystemResource resource = new FileSystemResource(path);

        if (!resource.exists()) {
            throw new FileNotFoundException("로컬 디스크에서 파일을 찾을 수 없습니다: " + path);
        }

        return resource;
    }
}
