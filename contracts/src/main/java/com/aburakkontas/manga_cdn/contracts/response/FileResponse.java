package com.aburakkontas.manga_cdn.contracts.response;

import lombok.Data;

import java.util.UUID;

@Data
public class FileResponse {
    private UUID fileId;
    private String fileName;
    private String fileType;
    private long size;
    private UUID ownerId;
    private String url;
}
