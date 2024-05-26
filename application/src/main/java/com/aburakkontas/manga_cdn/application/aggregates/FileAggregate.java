package com.aburakkontas.manga_cdn.application.aggregates;

import com.aburakkontas.manga.common.cdn.commands.DeleteFileCommand;
import com.aburakkontas.manga.common.cdn.commands.SaveFileCommand;
import com.aburakkontas.manga.common.cdn.commands.UpdateFileCommand;
import com.aburakkontas.manga.common.cdn.events.FileDeletedEvent;
import com.aburakkontas.manga.common.cdn.events.FileUpdatedEvent;
import com.aburakkontas.manga.common.cdn.events.FileUploadedEvent;
import com.aburakkontas.manga_cdn.domain.entities.file.File;
import com.aburakkontas.manga_cdn.domain.entities.file.FileContentType;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@Aggregate
@NoArgsConstructor
public class FileAggregate {

    @AggregateIdentifier
    private UUID fileId;
    private File file;

    @CommandHandler
    public FileAggregate(SaveFileCommand command) {
        AggregateLifecycle.apply(new FileUploadedEvent(command.getFileId(), command.getFileName(), command.getFileData(), command.getFileType(), command.getOwnerId()));
    }

    @CommandHandler
    public void handle(UpdateFileCommand command) {
        AggregateLifecycle.apply(new FileUpdatedEvent(command.getFileId(), command.getFileName(), command.getFileData(), command.getFileType(), command.getOwnerId()));
    }

    @CommandHandler
    public void handle(DeleteFileCommand command) {
        AggregateLifecycle.apply(new FileDeletedEvent(command.getFileId(), command.getOwnerId()));
    }

    @EventSourcingHandler
    public void on(FileUploadedEvent event) {
        this.fileId = event.getFileId();
        this.file = new File(
                event.getFileId(),
                event.getFileName(),
                event.getFileData(),
                FileContentType.fromExtension(event.getFileType()),
                event.getOwnerId()
        );
    }

    @EventSourcingHandler
    public void on(FileUpdatedEvent event) {
        this.file = new File(
                event.getFileId(),
                event.getFileName(),
                event.getFileData(),
                FileContentType.fromExtension(event.getFileType()),
                event.getOwnerId()
        );
    }

    @EventSourcingHandler
    public void on(FileDeletedEvent event) {
        AggregateLifecycle.markDeleted();
    }
}
