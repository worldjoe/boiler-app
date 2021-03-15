package net.singleclick.boilerapp.models;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import javax.persistence.*;

@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "document")
public class Document implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String userName;
    private String content;
}