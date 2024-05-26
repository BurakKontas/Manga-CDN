package com.aburakkontas.manga_cdn.domain.entities.file;

import jakarta.persistence.*;
import lombok.*;

import java.io.ByteArrayOutputStream;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


@Entity
@Table(name = "files")
@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

    @Id
    private UUID id;
    private String name;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Enumerated(EnumType.STRING)
    private FileContentType contentType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "data", length = 2000000000)
    private byte[] data;

    public File(UUID fileId, String fileName, byte[] fileData, FileContentType fileContentType, UUID ownerId) {
        this.id = fileId;
        this.name = fileName;
        this.data = compressFile(fileData, 0);
        this.contentType = fileContentType;
        this.ownerId = ownerId;
    }

    public byte[] getData() {
        return decompressFile(this.data, 0);
    }

    public String getFileExtension() {
        return this.contentType.getExtension();
    }

    public String getContentType() {
        return this.contentType.getContentType();
    }

    private int DEFAULT_TMP_SIZE = 20 * 1024 * 1024;

    private byte[] compressFile(byte[] data, int tmpSize) {
        if(tmpSize == 0) tmpSize = DEFAULT_TMP_SIZE;

        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[tmpSize];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }

    private byte[] decompressFile(byte[] data, int tmpSize) {
        if(tmpSize == 0) tmpSize = DEFAULT_TMP_SIZE;

        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[tmpSize];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }

}