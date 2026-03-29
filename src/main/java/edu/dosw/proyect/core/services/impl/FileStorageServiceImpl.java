package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.core.services.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageServiceImpl() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio uploads para multimedia.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        String originalName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (originalName.contains("..")) {
                throw new RuntimeException("El nombre del archivo es invalido " + originalName);
            }

            String extension = "";
            int i = originalName.lastIndexOf('.');
            if (i >= 0) {
                extension = originalName.substring(i);
            }

            String uniqueName = UUID.randomUUID().toString() + extension;
            Path targetLocation = this.fileStorageLocation.resolve(uniqueName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + uniqueName;
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo almacenar el archivo " + originalName, ex);
        }
    }
}

