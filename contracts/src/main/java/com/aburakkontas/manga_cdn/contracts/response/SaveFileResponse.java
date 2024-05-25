package com.aburakkontas.manga_cdn.contracts.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SaveFileResponse {
    private UUID id;
    private String fileName;
    private String fileType;
    private long size;
    private String url;
}
