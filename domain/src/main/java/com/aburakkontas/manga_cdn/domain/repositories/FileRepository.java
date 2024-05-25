package com.aburakkontas.manga_cdn.domain.repositories;

import com.aburakkontas.manga_cdn.domain.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {

    @Query("SELECT f FROM File f WHERE f.ownerId = :ownerId")
    List<File> findByOwnerId(@Param("ownerId") UUID ownerId);
}