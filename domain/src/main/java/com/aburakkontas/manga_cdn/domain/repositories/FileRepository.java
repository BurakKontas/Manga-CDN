package com.aburakkontas.manga_cdn.domain.repositories;

import com.aburakkontas.manga_cdn.domain.file.File;
import com.aburakkontas.manga_cdn.domain.file.FileContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {

    @Query("SELECT f FROM File f WHERE f.ownerId = :ownerId AND (:contentType IS NULL OR f.contentType = :contentType)")
    List<File> findByOwnerIdAndContentType(@Param("ownerId") UUID ownerId, @Param("contentType") FileContentType contentType);

    @Query("SELECT f FROM File f WHERE :contentType IS NULL OR f.contentType = :contentType")
    List<File> findAllByContentType(@Param("contentType") FileContentType contentType);
}