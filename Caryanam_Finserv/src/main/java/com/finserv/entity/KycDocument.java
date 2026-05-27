package com.finserv.entity;



import com.finserv.enums.DocumentType;
import com.finserv.enums.KycStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "kyc_documents")
@Data
public class KycDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @ManyToOne
    @JoinColumn(name = "kyc_id")
    private UserKyc userKyc;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private String documentUrl;

    // ADMIN REMARK
    private String adminRemark;

    @Enumerated(EnumType.STRING)
    private KycStatus status;
}
