package com.aburakkontas.manga_cdn.contracts.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SaveFileRequest {
    private MultipartFile fileData;
}
