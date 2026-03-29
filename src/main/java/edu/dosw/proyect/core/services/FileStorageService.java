package edu.dosw.proyect.core.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    /**
     * Guarda un archivo multimedia y retorna la URL relativa de acceso.
     */
    String storeFile(MultipartFile file);
}
