package ru.hldspm.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hldspm.web.entities.ApiToken;

public interface ApiTokensRepository extends JpaRepository<ApiToken, Long> {

}
