//package com.master.side.presentation.controller;
//
//import com.master.side.application.service.FileStorageService;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//
//@RestController
//@RequestMapping("/api/board")
//public class FileController {
//
//    private final FileStorageService fileStorageService;
//
//    public FileController(FileStorageService fileStorageService) {
//        this.fileStorageService = fileStorageService;
//    }
//
//    /**
//     * GET /api/board/file?fileUrl=... 형태로 요청이 들어오면,
//     * 해당 fileUrl을 가진 파일을 찾아서 다운로드 응답으로 보냄.
//     */
//    @GetMapping("/file")
//    public ResponseEntity<Resource> downloadFile(@RequestParam("fileUrl") String fileUrl) throws IOException {
//        // 1) 서비스에서 파일 Resource 가져오기
//        Resource resource = fileStorageService.downloadFile(fileUrl);
//        if (resource == null || !resource.exists()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // 2) Content Type 추론 (없으면 application/octet-stream)
//        String contentType = Files.probeContentType(resource.getFile().toPath());
//        if (!StringUtils.hasText(contentType)) {
//            contentType = "application/octet-stream";
//        }
//
//        // 3) 다운로드 될 때 표시할 파일명 (resource.getFilename() 사용)
//        String filename = resource.getFilename();
//        if (!StringUtils.hasText(filename)) {
//            filename = "unknown_file";
//        }
//
//        // 4) 한글/특수문자 파일명 깨짐 방지를 위해 URLEncoder 사용
//        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
//
//        // 5) Content-Disposition 헤더 설정
//        String contentDisposition = "attachment; filename=\"" + encodedFilename + "\"";
//
//        // 6) ResponseEntity 생성
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
//                .body(resource);
//    }
//}
