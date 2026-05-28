package com.finserv.service;

import com.finserv.enums.DocumentType;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

    Object uploadUnified(

            Long userId,

            MultipartFile file,

            String base64,

            DocumentType type
    );
}