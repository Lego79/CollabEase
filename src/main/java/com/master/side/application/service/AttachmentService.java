package com.master.side.application.service;

import com.master.side.domain.model.Attachment;
import com.master.side.domain.model.Board;
import com.master.side.domain.repository.AttachmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttachmentService   {

    private final AttachmentRepository attachmentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    public List<Attachment> saveAttachments(Board board, List<MultipartFile> files) {
        List<Attachment> savedAttachments = new ArrayList<>();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                // 파일 저장 로직 (예: 파일 시스템, 클라우드 스토리지 등) 후 저장 경로(filePath) 획득
                // 아래는 예시로, 실제 저장 로직에 따라 구현 필요
                String filePath = "/uploads/" + file.getOriginalFilename();

                Attachment attachment = Attachment.builder()
                        .board(board)
                        .fileName(file.getOriginalFilename())
                        .filePath(filePath)
                        .build();

                attachmentRepository.save(attachment);
                savedAttachments.add(attachment);
            }
        }
        return savedAttachments;
    }
}