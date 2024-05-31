package com.aburakkontas.manga_cdn.api.controllers;

import com.aburakkontas.manga.common.cdn.commands.DeleteFileCommand;
import com.aburakkontas.manga.common.cdn.commands.SaveFileCommand;
import com.aburakkontas.manga.common.cdn.commands.UpdateFileCommand;
import com.aburakkontas.manga_cdn.contracts.response.SaveFileResponse;
import com.aburakkontas.manga_cdn.domain.primitives.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/file")
public class FileCommandController {

    @Value("${file.get.url}")
    private String fileGetUrl;

    private CommandGateway commandGateway;

    public FileCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Operation(
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "File to be processed",
                content = @Content(
                        mediaType = "multipart/form-data",
                        schema = @Schema(type = "string", format = "binary")
                )
        )
    )
    @PostMapping(value = "/upload")
    public ResponseEntity<Result<SaveFileResponse>> uploadFile(
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) throws IOException {
        var ownerId = authentication.getCredentials();

        var command = new SaveFileCommand(
                UUID.randomUUID(),
                file.getOriginalFilename(),
                file.getBytes(),
                file.getContentType(),
                UUID.fromString(ownerId.toString())
        );

        UUID id = commandGateway.sendAndWait(command);

        var response = new SaveFileResponse(
                id,
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                fileGetUrl + id
        );

        return ResponseEntity.ok(Result.success(response));
    }

    @Operation(
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "File to be processed",
                content = @Content(
                        mediaType = "multipart/form-data",
                        schema = @Schema(type = "string", format = "binary")
                )
        )
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<Result<String>> updateFile(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) throws IOException {
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

    @Operation(
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "File to be processed",
                content = @Content(
                        mediaType = "multipart/form-data",
                        schema = @Schema(type = "string", format = "binary")
                )
        )
    )
    @PutMapping("/update/{ownerId}/{id}")
    public ResponseEntity<Result<String>> updateFile(
            @PathVariable UUID ownerId,
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
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
