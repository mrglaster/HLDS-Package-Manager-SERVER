package ru.hldspm.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hldspm.web.entities.Content;
import ru.hldspm.web.entities.ContentType;

import java.util.List;

@Repository
public interface ContentTypeRepository extends JpaRepository<ContentType, Long> {
    List<ContentType> findAll();
}
