package com.aburakkontas.manga_cdn.api.controllers;

import com.aburakkontas.manga.common.main.queries.GetFileDetailsQuery;
import com.aburakkontas.manga.common.main.queries.GetFileQuery;
import com.aburakkontas.manga.common.main.queries.results.GetFileDetailsQueryResult;
import com.aburakkontas.manga.common.main.queries.results.GetFileQueryResult;
import com.aburakkontas.manga.common.main.queries.results.GetFilesQueryResult;
import com.aburakkontas.manga_cdn.contracts.response.FileResponse;
import com.aburakkontas.manga_cdn.domain.file.FileContentType;
import com.aburakkontas.manga_cdn.domain.primitives.Result;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/file")
public class FileQueryController {

    private QueryGateway queryGateway;

    public FileQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable UUID id) {
        var query = new GetFileQuery(id);

        var result = queryGateway.query(query, GetFileQueryResult.class).join();

        var fileType = FileContentType.fromExtension(result.getFileType());

        return ResponseEntity.ok().contentType(MediaType.valueOf(fileType.getContentType())).body(result.getFile());
    }

    @GetMapping("/get/{id}/details")
    public ResponseEntity<Result<FileResponse>> getFileDetails(@PathVariable UUID id, Authentication authentication) {
        var ownerId = UUID.fromString(authentication.getCredentials().toString());

        var query = new GetFileDetailsQuery(id,ownerId);

        var result = queryGateway.query(query, GetFileDetailsQueryResult.class).join();

        var response = getFileDetails(result);

        return ResponseEntity.ok(Result.success(response));
    }

    @GetMapping("/get/details")
    public ResponseEntity<Result<FileResponse[]>> getFilesDetailed(@RequestParam(required = false, defaultValue = "png") FileContentType contentType, Authentication authentication) {
        var ownerId = UUID.fromString(authentication.getCredentials().toString());

        var query = new GetFileDetailsQuery(null, ownerId);

        var result = queryGateway.query(query, GetFilesQueryResult.class).join();

        var response = new FileResponse[result.getFiles().size()];

        for (var file : result.getFiles()) {
            response[result.getFiles().indexOf(file)] = getFileDetails(file);
        }

        return ResponseEntity.ok(Result.success(response));
    }

    private FileResponse getFileDetails(GetFileDetailsQueryResult result) {
        var response = new FileResponse();
        response.setFileId(result.getFileId());
        response.setFileName(result.getFileName());
        response.setFileType(result.getFileType());
        response.setSize(result.getSize());
        response.setOwnerId(result.getOwnerId());
        response.setUrl("/api/v1/file/get/" + result.getFileId());

        return response;
    }

}
