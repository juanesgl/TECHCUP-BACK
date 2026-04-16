package edu.dosw.proyect.services;

import edu.dosw.proyect.core.services.impl.FileStorageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileStorageServiceImplTest {

    @Test
    void storeFile_HappyPath_GuardaYRetornaRuta() throws Exception {
        FileStorageServiceImpl service = new FileStorageServiceImpl();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-file.txt",
                "text/plain",
                "contenido".getBytes()
        );

        String storedPath = service.storeFile(file);

        assertTrue(storedPath.startsWith("/uploads/"));
        String fileName = storedPath.substring("/uploads/".length());
        Path absolutePath = Paths.get("uploads").toAbsolutePath().normalize().resolve(fileName);
        assertTrue(Files.exists(absolutePath));
        Files.deleteIfExists(absolutePath);
    }

    @Test
    void storeFile_FileNameInvalido_LanzaRuntimeException() {
        FileStorageServiceImpl service = new FileStorageServiceImpl();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "../hack.txt",
                "text/plain",
                "x".getBytes()
        );

        assertThrows(RuntimeException.class, () -> service.storeFile(file));
    }

    @Test
    void storeFile_IOException_LanzaRuntimeException() throws Exception {
        FileStorageServiceImpl service = new FileStorageServiceImpl();
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("ok.txt");
        when(file.getInputStream()).thenThrow(new IOException("fallo io"));

        assertThrows(RuntimeException.class, () -> service.storeFile(file));
    }
}
