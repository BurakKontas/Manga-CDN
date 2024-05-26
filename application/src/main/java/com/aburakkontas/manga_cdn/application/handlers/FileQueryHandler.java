package com.aburakkontas.manga_cdn.application.handlers;

import com.aburakkontas.manga.common.cdn.queries.GetFileDetailsQuery;
import com.aburakkontas.manga.common.cdn.queries.GetFileQuery;
import com.aburakkontas.manga.common.cdn.queries.GetFilesQuery;
import com.aburakkontas.manga.common.cdn.queries.results.GetFileDetailsQueryResult;
import com.aburakkontas.manga.common.cdn.queries.results.GetFileQueryResult;
import com.aburakkontas.manga.common.cdn.queries.results.GetFilesQueryResult;
import com.aburakkontas.manga_cdn.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_cdn.domain.entities.file.File;
import com.aburakkontas.manga_cdn.domain.entities.file.FileContentType;
import com.aburakkontas.manga_cdn.domain.repositories.FileRepository;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class FileQueryHandler {

    private final FileRepository fileRepository;

    @QueryHandler
    public GetFileQueryResult getFile(GetFileQuery query) {
        var file = fileRepository.findById(query.getFileId()).orElseThrow(() -> new ExceptionWithErrorCode("File not found", 404));

        var queryResult = new GetFileQueryResult();
        queryResult.setFile(file.getData());
        queryResult.setFileName(file.getName());
        queryResult.setFileType(file.getContentType());

        return queryResult;
    }

    @QueryHandler
    public GetFilesQueryResult getFiles(GetFilesQuery query) {
        List<File> files;
        var contentType = FileContentType.valueOf(query.getFileType());

        if(query.getOwnerId() != null) {
            files = fileRepository.findByOwnerIdAndContentType(query.getOwnerId(), contentType);
        } else {
            files = fileRepository.findAllByContentType(contentType);
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
    public GetFileDetailsQueryResult getFileDetails(GetFileDetailsQuery query) {
        var file = fileRepository.findById(query.getFileId()).orElseThrow(() -> new ExceptionWithErrorCode("File not found", 404));
        return getFileDetails(file);
    }

    private GetFileDetailsQueryResult getFileDetails(File file) {
        var fileDetails = new GetFileDetailsQueryResult();
        fileDetails.setSize(file.getData().length);
        fileDetails.setOwnerId(file.getOwnerId());
        fileDetails.setFileName(file.getName());
        fileDetails.setFileType(file.getContentType());
        fileDetails.setFileId(file.getId());

        return fileDetails;
    }
}