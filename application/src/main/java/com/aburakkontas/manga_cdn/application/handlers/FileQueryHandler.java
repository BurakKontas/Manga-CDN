package com.aburakkontas.manga_cdn.application.handlers;

import com.aburakkontas.manga.common.main.queries.GetFileQuery;
import com.aburakkontas.manga.common.main.queries.GetFilesQuery;
import com.aburakkontas.manga.common.main.queries.results.GetFileDetailsQueryResult;
import com.aburakkontas.manga.common.main.queries.results.GetFileQueryResult;
import com.aburakkontas.manga.common.main.queries.results.GetFilesQueryResult;
import com.aburakkontas.manga_cdn.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_cdn.domain.file.File;
import com.aburakkontas.manga_cdn.domain.repositories.FileRepository;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class FileQueryHandler {

    private final FileRepository fileRepository;

    @QueryHandler
    public GetFileQueryResult getFile(GetFileQuery query) throws IOException {
        var file = fileRepository.findById(query.getFileId()).orElseThrow(() -> new ExceptionWithErrorCode("File not found", 404));

        var queryResult = new GetFileQueryResult();
        queryResult.setFile(file.getMultipartFile().getBytes());
        queryResult.setFileName(file.getName());
        queryResult.setFileType(file.getMultipartFile().getContentType());

        return queryResult;
    }

    @QueryHandler
    public GetFilesQueryResult getFiles(GetFilesQuery query) {
        List<File> files;

        if(query.getOwnerId() != null) {
            files = fileRepository.findByOwnerId(query.getOwnerId());
        } else {
            files = fileRepository.findAll();
        }

        var fileArray = new ArrayList<GetFileDetailsQueryResult>();
        files.forEach(file -> {
            fileArray.add(getFileDetails(file));
        });

        GetFilesQueryResult queryResult = new GetFilesQueryResult();
        queryResult.setFiles(fileArray);

        return queryResult;
    }

    @QueryHandler
    public GetFileDetailsQueryResult getFileDetails(GetFileQuery query) {
        var file = fileRepository.findById(query.getFileId()).orElseThrow(() -> new ExceptionWithErrorCode("File not found", 404));
        return getFileDetails(file);
    }

    private GetFileDetailsQueryResult getFileDetails(File file) {
        var fileDetails = new GetFileDetailsQueryResult();
        fileDetails.setSize(file.getMultipartFile().getSize());
        fileDetails.setOwnerId(file.getOwnerId());
        fileDetails.setFileName(file.getName());
        fileDetails.setFileType(file.getMultipartFile().getContentType());
        fileDetails.setFileId(file.getId());

        return fileDetails;
    }
}