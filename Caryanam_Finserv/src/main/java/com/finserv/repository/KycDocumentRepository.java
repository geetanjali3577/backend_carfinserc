package com.finserv.repository;

import com.finserv.entity.KycDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KycDocumentRepository
        extends JpaRepository<KycDocument, Long> {

    List<KycDocument> findByUserKycKycId(Long kycId);
}
