package com.finserv.entity;

import com.finserv.enums.DocumentType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private DocumentType documentType;

    private String fileName;

    private String contentType;

    private Long fileSize;

    private String status;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;

    private LocalDateTime uploadedAt;
}