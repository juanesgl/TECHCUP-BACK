package edu.dosw.proyect.services;

import edu.dosw.proyect.core.services.impl.FileStorageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceImplTest {

    private final FileStorageServiceImpl service = new FileStorageServiceImpl();

    @Test
    void storeFile_HappyPath_RetornaUrl() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "comprobante.pdf",
                "application/pdf", "contenido del archivo".getBytes());

        String url = service.storeFile(file);

        assertNotNull(url);
        assertTrue(url.startsWith("/uploads/"));
        assertTrue(url.endsWith(".pdf"));
    }

    @Test
    void storeFile_SinExtension_RetornaUrl() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "comprobante",
                "application/octet-stream", "contenido".getBytes());

        String url = service.storeFile(file);

        assertNotNull(url);
        assertTrue(url.startsWith("/uploads/"));
    }

    @Test
    void storeFile_NombreInvalido_LanzaRuntimeException() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "../archivo-malicioso.pdf",
                "application/pdf", "contenido".getBytes());

        assertThrows(RuntimeException.class, () -> service.storeFile(file));
    }
}