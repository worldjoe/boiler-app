package net.singleclick.boilerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import net.singleclick.boilerapp.models.Document;

public interface DocumentRepository extends JpaRepository<Document, String> {
    // This can be extended to support things like
    //List<Document> findByContent(String content);
}