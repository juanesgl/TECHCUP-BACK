package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.UpdateUserRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.UserResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.services.impl.UserServiceImpl;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.mapper.UserPersistenceMapper;
import edu.dosw.proyect.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserPersistenceMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private RegisterRequestDTO buildRequest(String role) {
        return new RegisterRequestDTO(
                "Test User",
                "test@mail.escuelaing.edu.co",
                "pass123", role, "Delantero", 4);
    }

    private UserEntity adminEntity(Long id) {
        UserEntity e = new UserEntity();
        e.setId(id);
        e.setRole("ADMINISTRATOR");
        e.setName("Admin");
        e.setEmail("admin@mail.com");
        e.setActive(true);
        return e;
    }

    private UserEntity studentEntity(Long id) {
        UserEntity e = new UserEntity();
        e.setId(id);
        e.setRole("STUDENT");
        e.setName("Student");
        e.setEmail("student@mail.escuelaing.edu.co");
        e.setActive(true);
        return e;
    }

    // ─── registerUser ─────────────────────────────────────────────────────────

    @Test
    void registerUser_HappyPath_RetornaResponse() {
        RegisterRequestDTO request = buildRequest("STUDENT");
        UserEntity saved = new UserEntity();
        saved.setId(1L);
        saved.setEmail("test@mail.escuelaing.edu.co");
        saved.setRole("STUDENT");

        when(passwordEncoder.encode(any())).thenReturn("hashedPass");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userMapper.toEntity(any())).thenReturn(saved);
        when(userRepository.save(any())).thenReturn(saved);

        RegisterResponseDTO response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void registerUser_RolInvalido_LanzaException() {
        RegisterRequestDTO request = buildRequest("ROL_INVALIDO");
        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(request));
    }

    @Test
    void registerUser_CorreoYaRegistrado_LanzaException() {
        RegisterRequestDTO request = buildRequest("STUDENT");
        UserEntity existing = new UserEntity();
        existing.setEmail("test@mail.escuelaing.edu.co");

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class,
                () -> userService.registerUser(request));
    }

    @Test
    void registerUser_CorreoInvalidoParaRol_LanzaException() {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Test", "test@hotmail.com", "pass", "STUDENT", null, 1);

        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(request));
    }

    @Test
    void registerUser_DataIntegrityViolation_LanzaIllegalState() {
        RegisterRequestDTO request = buildRequest("STUDENT");

        when(passwordEncoder.encode(any())).thenReturn("hashed");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userMapper.toEntity(any())).thenReturn(new UserEntity());
        when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(IllegalStateException.class,
                () -> userService.registerUser(request));
    }

    @Test
    void registerUser_RolAdministrator_AceptadoComoAdmin() {
        RegisterRequestDTO request = buildRequest("ADMINISTRATOR");
        UserEntity saved = new UserEntity();
        saved.setId(7L);

        when(passwordEncoder.encode(any())).thenReturn("hashedPass");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userMapper.toEntity(any())).thenReturn(saved);
        when(userRepository.save(any())).thenReturn(saved);

        RegisterResponseDTO response = userService.registerUser(request);

        assertEquals(7L, response.getUserId());
    }

    @Test
    void registerUser_PreferredPositionNull_YSkillLevelNull_EnNoJugador_NoFalla() {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Admin User",
                "admin@mail.escuelaing.edu.co",
                "pass12345",
                "ADMINISTRATOR",
                null,
                null
        );

        UserEntity saved = new UserEntity();
        saved.setId(10L);

        when(passwordEncoder.encode(any())).thenReturn("hashedPass");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userMapper.toEntity(any())).thenReturn(saved);
        when(userRepository.save(any())).thenReturn(saved);

        RegisterResponseDTO response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals(10L, response.getUserId());
        verify(userRepository).save(any());
    }

    @Test
    void registerUser_PreferredPositionBlank_YSkillLevelCero_EnJugador_NoFalla() {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Player User",
                "player@random.com",
                "pass12345",
                "PLAYER",
                "   ",
                0
        );

        UserEntity saved = new UserEntity();
        saved.setId(11L);

        when(passwordEncoder.encode(any())).thenReturn("hashedPass");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userMapper.toEntity(any())).thenReturn(saved);
        when(userRepository.save(any())).thenReturn(saved);

        RegisterResponseDTO response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals(11L, response.getUserId());
        verify(userRepository).save(any());
    }

    // ─── getAllUsers ──────────────────────────────────────────────────────────

    @Test
    void getAllUsers_AdminValido_RetornaLista() {
        UserEntity admin = adminEntity(1L);
        UserEntity student = studentEntity(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.findAll()).thenReturn(List.of(admin, student));

        List<UserResponseDTO> result = userService.getAllUsers(1L);

        assertEquals(2, result.size());
    }

    @Test
    void getAllUsers_NoEsAdmin_LanzaBusinessRuleException() {
        UserEntity student = studentEntity(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(student));

        assertThrows(BusinessRuleException.class,
                () -> userService.getAllUsers(2L));
    }

    @Test
    void getAllUsers_RequesterNoExiste_LanzaResourceNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getAllUsers(99L));
    }

    @Test
    void getAllUsers_RolADMINISTRATOR_TambienEsValido() {
        UserEntity admin = new UserEntity();
        admin.setId(1L);
        admin.setRole("ADMINISTRATOR");

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.findAll()).thenReturn(List.of());

        assertDoesNotThrow(() -> userService.getAllUsers(1L));
    }

    // ─── getUserById ─────────────────────────────────────────────────────────

    @Test
    void getUserById_AdminAccedeAOtroUsuario_Retorna() {
        UserEntity admin = adminEntity(1L);
        UserEntity student = studentEntity(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.findById(2L)).thenReturn(Optional.of(student));

        UserResponseDTO result = userService.getUserById(2L, 1L);

        assertEquals(2L, result.getId());
    }

    @Test
    void getUserById_OwnerAccedeASiMismo_Retorna() {
        UserEntity student = studentEntity(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(student));

        UserResponseDTO result = userService.getUserById(2L, 2L);

        assertEquals(2L, result.getId());
    }

    @Test
    void getUserById_UsuarioNoExiste_LanzaResourceNotFound() {
        UserEntity admin = adminEntity(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(99L, 1L));
    }

    @Test
    void getUserById_SinPermiso_LanzaBusinessRuleException() {
        UserEntity student = studentEntity(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(student));

        assertThrows(BusinessRuleException.class,
                () -> userService.getUserById(5L, 2L));
    }

    @Test
    void getUserById_RequesterNoExiste_LanzaResourceNotFound() {
        when(userRepository.findById(88L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(2L, 88L));
    }

    // ─── updateUser ──────────────────────────────────────────────────────────

    @Test
    void updateUser_AdminActualizaOtroUsuario_Retorna() {
        UserEntity admin = adminEntity(1L);
        UserEntity student = studentEntity(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.findById(2L)).thenReturn(Optional.of(student));
        when(userRepository.save(any())).thenReturn(student);

        UpdateUserRequestDTO req = new UpdateUserRequestDTO();
        req.setName("Nuevo Nombre");
        req.setLastName("Apellido");
        req.setAcademicProgram("Ingeniería");

        UserResponseDTO result = userService.updateUser(2L, 1L, req);

        assertNotNull(result);
        verify(userRepository).save(student);
    }

    @Test
    void updateUser_OwnerSeActualizaASiMismo_CamposBlankONull_NoModifica() {
        UserEntity student = studentEntity(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(student));
        when(userRepository.save(any())).thenReturn(student);

        UpdateUserRequestDTO req = new UpdateUserRequestDTO();
        req.setName("  ");      // blank → no debe cambiar
        req.setLastName(null);  // null  → no debe cambiar

        UserResponseDTO result = userService.updateUser(2L, 2L, req);

        assertNotNull(result);
        verify(userRepository).save(student);
    }

    @Test
    void updateUser_SinPermiso_LanzaBusinessRuleException() {
        UserEntity student = studentEntity(2L);
        UpdateUserRequestDTO request = new UpdateUserRequestDTO();

        when(userRepository.findById(2L)).thenReturn(Optional.of(student));

        assertThrows(BusinessRuleException.class,
                () -> userService.updateUser(5L, 2L, request));
    }

    @Test
    void updateUser_UsuarioObjetivoNoExiste_LanzaResourceNotFound() {
        UserEntity admin = adminEntity(1L);
        UpdateUserRequestDTO request = new UpdateUserRequestDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(99L, 1L, request));
    }

    // ─── deleteUser ──────────────────────────────────────────────────────────

    @Test
    void deleteUser_AdminElimina_Exitoso() {
        UserEntity admin = adminEntity(1L);
        UserEntity student = studentEntity(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.findById(2L)).thenReturn(Optional.of(student));

        userService.deleteUser(2L, 1L);

        verify(userRepository).delete(student);
    }

    @Test
    void deleteUser_NoEsAdmin_LanzaBusinessRuleException() {
        UserEntity student = studentEntity(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(student));

        assertThrows(BusinessRuleException.class,
                () -> userService.deleteUser(5L, 2L));
    }

    @Test
    void deleteUser_UsuarioNoExiste_LanzaResourceNotFound() {
        UserEntity admin = adminEntity(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(99L, 1L));
    }
}