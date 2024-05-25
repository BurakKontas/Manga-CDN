package com.aburakkontas.manga_cdn.domain.file;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


@Entity
@Table(name = "files")
@Data
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Enumerated(EnumType.STRING)
    private FileContentType contentType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "data", length = 2000000000)
    @Getter(AccessLevel.PRIVATE)
    private byte[] data;

    public String getFileExtension() {
        return this.contentType.getExtension();
    }

    public String getContentType() {
        return this.contentType.getContentType();
    }

    public void setMultipartFile(MultipartFile file) throws IOException {
        this.name = file.getOriginalFilename();
        this.contentType = FileContentType.fromExtension(getFileExtensionFromName(file.getOriginalFilename()));
        this.data = compressFile(file.getBytes(), 0);
    }

    public MultipartFile getMultipartFile() {
        byte[] decompressedData = decompressFile(this.data, 0);
        MultipartFile multipartFile = new MockMultipartFile(this.name, decompressedData);
        return multipartFile;
    }

    private String getFileExtensionFromName(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        } else {
            throw new IllegalArgumentException("Invalid file name: " + fileName);
        }
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