package net.singleclick.boilerapp.rest.services;

public class DocumentNotFoundException extends DocumentServiceException {

    private static final long serialVersionUID = 1L;

    private final String documentId;

    /**
     * Construct with a message {@code String} that is returned by the inherited
     * {@code Throwable.getMessage}.
     *
     * @param documentId
     *            the id of the missing document
     */
    public DocumentNotFoundException(String documentId) {
        super("Failed to find document for documentId=" + documentId);
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }
}
