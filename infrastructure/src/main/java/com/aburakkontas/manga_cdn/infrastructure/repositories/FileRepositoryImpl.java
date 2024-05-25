package com.aburakkontas.manga_cdn.infrastructure.repositories;

import com.aburakkontas.manga_cdn.domain.repositories.FileRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface FileRepositoryImpl extends FileRepository {
}
