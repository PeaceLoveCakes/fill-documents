package com.example.docapi.db.repository;

import com.example.docapi.db.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    List<Template> findAllByNameLike(String name);

    @Query("select t.data " +
            "from Template t " +
            "where t.id = :id")
    byte[] getDataById(Long id);
}
