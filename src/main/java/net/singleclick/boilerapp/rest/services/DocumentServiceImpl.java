package net.singleclick.boilerapp.rest.services;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import net.singleclick.boilerapp.repository.DocumentRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.singleclick.boilerapp.models.Document;
import net.singleclick.boilerapp.models.Documents;

@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private Map<String, Document> documents = new ConcurrentHashMap<>();

    @Autowired
    DocumentRepository documentRepository;

    @Override
    public Documents getDocuments() {
        Documents docList = new Documents();
        List<Document> docs = new ArrayList<>(documents.values());
        docList.setDocuments(docs);
        return docList;
    }

    @Override
    public Document getDocument(String documentId) {

        Document document = documents.get(documentId);
        if (document == null) {
            throw new DocumentNotFoundException(documentId);
        }

        return document;
    }

    @Override
    public Document createDocument(Document document) {
        if (document == null) {
            LOGGER.error("createDocument: document parameter is null!");
            throw new DocumentConstraintViolationException("createDocument: Document must not be null");
        } else if (StringUtils.isBlank(document.getContent())) {
            LOGGER.error("createDocument: document parameter must have non-empty content; document=" + document);
            throw new DocumentConstraintViolationException(
                    "createDocument: Document must have non-empty content, but document=" + document);
        }

        // set the time stamps in the document
        LocalDateTime now = java.time.LocalDateTime.now();
        document.setCreatedDate(now);
        document.setLastModifiedDate(now);

        document.setId(RandomStringUtils.randomNumeric(10));

        try {
            documents.put(document.getId(), document);
            Document _document = documentRepository.save(document);
        } catch (OutOfMemoryError oome) {
            LOGGER.error("createDocument: got OutOfMemoryError");
            LOGGER.error("createDocument: documents.size=" + documents.size());
            throw oome;
        }
        return document;
    }

    @Override
    public Document updateDocument(Document document, String documentId) {

        validateDocument(document);

        validateDocumentId(document, documentId);

        Document updatingDocument = getDocument(documentId);

        if (updatingDocument == null) {
            throw new DocumentNotFoundException(documentId);
        }

        updatingDocument.setLastModifiedDate(java.time.LocalDateTime.now());
        updatingDocument.setContent(document.getContent());

        documents.put(documentId, updatingDocument);
        documentRepository.save(updatingDocument);

        return updatingDocument;
    }

    @Override
    public void deleteDocument(String documentId) {
        Document document = documents.remove(documentId);
        documentRepository.deleteById(document.getId());
        if (document == null) {
            throw new DocumentNotFoundException(documentId);
        }
    }

    private void validateDocumentId(Document document, String documentId) {
        if (StringUtils.isEmpty(documentId)) {
            throw new DocumentConstraintViolationException(
                    "documentId must not be null or empty, document=" + document + ", documentId=" + documentId);
        }

        if (!StringUtils.isEmpty(document.getId())) {
            if (!document.getId().equals(documentId)) {
                throw new DocumentConstraintViolationException("if document ID is present, it must equal the documentId"
                        + " argument, " + "document=" + document + ", documentId=" + documentId);
            }
        }
    }

    private void validateDocument(Document document) {
        if (document == null || StringUtils.isBlank(document.getContent())) {
            throw new DocumentConstraintViolationException(
                    "Document must not be null and must have non-empty content, document=" + document);
        }
    }

}