package com.aburakkontas.manga_cdn.contracts.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class UpdateFileRequest {
    private UUID fileId;
    private MultipartFile fileData;
}
