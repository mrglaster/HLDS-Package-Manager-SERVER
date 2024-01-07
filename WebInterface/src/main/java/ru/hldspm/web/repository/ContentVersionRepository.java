package ru.hldspm.web.repository;

import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hldspm.web.entities.ContentVersion;

import java.util.List;

@Repository
public interface ContentVersionRepository extends JpaRepository<ContentVersion, Long> {
    List<ContentVersion> findAll();
}
