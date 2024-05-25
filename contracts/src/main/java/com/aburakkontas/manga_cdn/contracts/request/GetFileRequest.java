package com.aburakkontas.manga_cdn.contracts.request;

import lombok.Data;

import java.util.UUID;

@Data
public class GetFileRequest {
    private UUID fileId;
}
