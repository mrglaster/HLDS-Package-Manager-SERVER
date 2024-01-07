package ru.hldspm.web.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hldspm.web.entities.Content;
import ru.hldspm.web.entities.Platform;

import java.util.List;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {
    List<Platform> findAll();
}
