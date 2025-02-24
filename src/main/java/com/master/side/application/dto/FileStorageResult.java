package com.master.side.application.dto;

public class FileStorageResult {

    private String fileUrl;   // 파일 다운로드 URL (또는 접근 URL)
    private String filePath;  // 서버 내 실제 저장 경로

    public FileStorageResult(String fileUrl, String filePath) {
        this.fileUrl = fileUrl;
        this.filePath = filePath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getFilePath() {
        return filePath;
    }
}