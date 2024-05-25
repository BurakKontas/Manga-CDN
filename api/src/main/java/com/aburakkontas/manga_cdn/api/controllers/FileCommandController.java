package com.aburakkontas.manga_cdn.api.controllers;

import com.aburakkontas.manga.common.main.commands.DeleteFileCommand;
import com.aburakkontas.manga.common.main.commands.SaveFileCommand;
import com.aburakkontas.manga.common.main.commands.UpdateFileCommand;
import com.aburakkontas.manga_cdn.domain.primitives.Result;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/file")
public class FileCommandController {

    private CommandGateway commandGateway;

    public FileCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }


    @PostMapping("/upload")
    public ResponseEntity<Result<String>> uploadFile(@RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {
        var ownerId = authentication.getCredentials();

        var command = new SaveFileCommand(
                UUID.randomUUID(),
                file.getOriginalFilename(),
                file.getBytes(),
                file.getContentType(),
                UUID.fromString(ownerId.toString())
        );

        String id = commandGateway.sendAndWait(command);
        return ResponseEntity.ok(Result.success(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Result<String>> updateFile(@PathVariable UUID id, @RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {
        var ownerId = authentication.getCredentials();

        var command = new UpdateFileCommand(
                id,
                file.getOriginalFilename(),
                file.getBytes(),
                file.getContentType(),
                UUID.fromString(ownerId.toString())
        );

        commandGateway.sendAndWait(command);
        return ResponseEntity.ok(Result.success(id.toString()));
    }

    @PutMapping("/update/{ownerId}/{id}")
    public ResponseEntity<Result<String>> updateFile(@PathVariable UUID ownerId, @PathVariable UUID id, @RequestParam("file") MultipartFile file) throws IOException {
        var command = new UpdateFileCommand(
                id,
                file.getOriginalFilename(),
                file.getBytes(),
                file.getContentType(),
                ownerId
        );

        commandGateway.sendAndWait(command);
        return ResponseEntity.ok(Result.success(id.toString()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Result<String>> deleteFile(@RequestParam("id") String id, Authentication authentication) {
        var ownerId = authentication.getCredentials();

        var command = new DeleteFileCommand(
            UUID.fromString(id),
            UUID.fromString(ownerId.toString())
        );

        commandGateway.sendAndWait(command);
        return ResponseEntity.ok(Result.success(id));
    }

    @DeleteMapping("/delete/{ownerId}/{id}")
    public ResponseEntity<Result<String>> deleteFile(@PathVariable UUID ownerId, @PathVariable UUID id) {
        var command = new DeleteFileCommand(
            id,
            ownerId
        );

        commandGateway.sendAndWait(command);
        return ResponseEntity.ok(Result.success(id.toString()));
    }
}
