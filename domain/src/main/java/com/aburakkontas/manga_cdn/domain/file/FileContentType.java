package com.aburakkontas.manga_cdn.domain.file;


import lombok.Getter;

@Getter
public enum FileContentType {
    JPEG("image/jpeg", "jpeg"),
    JPG("image/jpeg", "jpg"),
    PNG("image/png", "png"),
    GIF("image/gif", "gif"),
    BMP("image/bmp", "bmp"),
    TIFF("image/tiff", "tiff"),
    ICO("image/vnd.microsoft.icon", "ico"),
    SVG("image/svg+xml", "svg"),
    WEBP("image/webp", "webp"),
    PDF("application/pdf", "pdf"),
    MP4("video/mp4", "mp4"),
    WEBM("video/webm", "webm"),
    OGG_VIDEO("video/ogg", "ogv"),
    AVI("video/x-msvideo", "avi"),
    MKV("video/x-matroska", "mkv"),
    MP3("audio/mpeg", "mp3"),
    OGG_AUDIO("audio/ogg", "ogg"),
    WAV("audio/wav", "wav"),
    AAC("audio/aac", "aac"),
    FLAC("audio/flac", "flac"),
    HTML("text/html", "html"),
    CSS("text/css", "css"),
    JS("application/javascript", "js"),
    JSON("application/json", "json"),
    XML("application/xml", "xml"),
    TXT("text/plain", "txt"),
    CSV("text/csv", "csv"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),
    XLS("application/vnd.ms-excel", "xls"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"),
    DOC("application/msword", "doc"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx"),
    PPT("application/vnd.ms-powerpoint", "ppt"),
    ZIP("application/zip", "zip"),
    RAR("application/x-rar-compressed", "rar"),
    TAR("application/x-tar", "tar"),
    GZ("application/gzip", "gz"),
    TGZ("application/gzip", "tgz");

    private final String contentType;
    private final String extension;

    FileContentType(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    public static FileContentType fromExtension(String extension) {
        for (FileContentType type : values()) {
            if (type.getExtension().equalsIgnoreCase(extension)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown file extension: " + extension);
    }
}