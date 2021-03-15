package net.singleclick.boilerapp.models;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class Documents implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Document> documents;

}