package com.finserv.serviceImpl;

import com.finserv.dto.DocumentResponseDTO;
import com.finserv.entity.Document;
import com.finserv.entity.User;
import com.finserv.enums.DocumentType;
import com.finserv.exception.BadRequestException;
import com.finserv.repository.DocumentRepository;
import com.finserv.repository.UserRepository;
import com.finserv.service.DocumentService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl
        implements DocumentService {

    private final DocumentRepository documentRepository;

    private final UserRepository userRepository;

    @Override
    public Object uploadUnified(

            Long userId,

            MultipartFile file,

            String base64,

            DocumentType type
    ) {

        // USER VALIDATION
        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->

                                new BadRequestException(
                                        "User Not Found"
                                ));

        // DUPLICATE DOCUMENT CHECK
        boolean alreadyExists =

                documentRepository
                        .existsByUserIdAndDocumentType(
                                userId,
                                type
                        );

        if (alreadyExists) {

            throw new BadRequestException(
                    type + " Already Uploaded"
            );
        }

        try {

            Document document =
                    new Document();

            document.setUserId(
                    user.getUserId()
            );

            document.setDocumentType(type);

            document.setStatus("PENDING");

            document.setUploadedAt(
                    LocalDateTime.now()
            );

            // FILE UPLOAD
            if (file != null && !file.isEmpty()) {

                document.setFileName(
                        file.getOriginalFilename()
                );

                document.setContentType(
                        file.getContentType()
                );

                document.setFileSize(
                        file.getSize()
                );

                document.setFileData(
                        file.getBytes()
                );
            }

            // BASE64 UPLOAD
            else if (base64 != null
                    && !base64.isBlank()) {

                byte[] decodedBytes =

                        Base64.getDecoder()
                                .decode(base64);

                document.setFileName(
                        type + ".pdf"
                );

                document.setContentType(
                        "application/pdf"
                );

                document.setFileSize(
                        (long) decodedBytes.length
                );

                document.setFileData(
                        decodedBytes
                );
            }

            Document savedDocument =

                    documentRepository.save(
                            document
                    );

            // RESPONSE DTO
            DocumentResponseDTO response =

                    new DocumentResponseDTO();

            response.setDocumentId(
                    savedDocument.getDocumentId()
            );

            response.setUserId(
                    savedDocument.getUserId()
            );

            response.setDocumentType(
                    savedDocument
                            .getDocumentType()
                            .name()
            );

            response.setFileName(
                    savedDocument.getFileName()
            );

            response.setContentType(
                    savedDocument.getContentType()
            );

            response.setFileSize(
                    savedDocument.getFileSize()
            );

            response.setStatus(
                    savedDocument.getStatus()
            );

            response.setUploadedAt(
                    savedDocument.getUploadedAt()
            );

            return response;

        } catch (Exception e) {

            throw new BadRequestException(
                    "Document Upload Failed : "
                            + e.getMessage()
            );
        }
    }
}

