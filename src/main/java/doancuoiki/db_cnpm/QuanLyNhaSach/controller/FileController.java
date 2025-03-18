package doancuoiki.db_cnpm.QuanLyNhaSach.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResUploadFileDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.FileService;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${nqoctai.upload-file.base-uri}")
    private String baseURI;

    @PostMapping("/files")
    public ResponseEntity<ApiResponse<ResUploadFileDTO>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, AppException {
        // validate
        if (file == null || file.isEmpty()) {
            throw new AppException("File is empty. Please upload a file.");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.endsWith(item));

        if (!isValid) {
            throw new AppException("Invalid file extension. only allows");
        }

        // create directory if not exists
        this.fileService.createUploadFolder(baseURI + folder);

        // save file
        String uploadFile = this.fileService.store(file, folder);
        ResUploadFileDTO res = new ResUploadFileDTO();
        res.setFileName(uploadFile);
        res.setUploadedAt(Instant.now());

        ApiResponse<ResUploadFileDTO> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Upload file successful");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok().body(response);
    }
}
