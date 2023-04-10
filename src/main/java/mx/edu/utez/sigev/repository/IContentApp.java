package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.Color;
import mx.edu.utez.sigev.entity.ContentApp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IContentApp extends JpaRepository<ContentApp, Long> {
    public ContentApp findById(long id);
}
