package com.aburakkontas.manga_cdn.application.handlers;

import com.aburakkontas.manga.common.main.events.FileDeletedEvent;
import com.aburakkontas.manga.common.main.events.FileUpdatedEvent;
import com.aburakkontas.manga.common.main.events.FileUploadedEvent;
import com.aburakkontas.manga_cdn.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_cdn.domain.file.File;
import com.aburakkontas.manga_cdn.domain.file.FileContentType;
import com.aburakkontas.manga_cdn.domain.repositories.FileRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class FileEventHandler {

    private final FileRepository fileRepository;

    public FileEventHandler(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @EventHandler
    public void on(FileUploadedEvent event) {
        var file = new File(
                event.getFileId(),
                event.getFileName(),
                event.getFileData(),
                FileContentType.fromExtension(event.getFileType()),
                event.getOwnerId()
        );

        fileRepository.save(file);
    }

    @EventHandler
    public void on(FileUpdatedEvent event) {
        if(!fileRepository.existsById(event.getFileId())) {
            throw new ExceptionWithErrorCode("File not found", 404);
        }

        var file = new File(
                event.getFileId(),
                event.getFileName(),
                event.getFileData(),
                FileContentType.fromExtension(event.getFileType()),
                event.getOwnerId()
        );

        fileRepository.save(file);
    }

    @EventHandler
    public void on(FileDeletedEvent event) {
        if(!fileRepository.existsById(event.getFileId())) {
            throw new ExceptionWithErrorCode("File not found", 404);
        }

        fileRepository.deleteById(event.getFileId());
    }
}
