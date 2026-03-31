package edu.dosw.proyect.security;

import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.UserRole;
import edu.dosw.proyect.core.services.authorization.AuthorizationValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationValidatorTest {

    private final AuthorizationValidator validator = new AuthorizationValidator();

    private User buildUser(String role) {
        return User.builder()
                .id(1L)
                .name("Test")
                .email("test@mail.com")
                .password("pass")
                .role(role)
                .active(true)
                .build();
    }


    @Test
    void validateRole_RolCorrecto_NoLanzaExcepcion() {
        User user = buildUser("ORGANIZER");
        assertDoesNotThrow(() ->
                validator.validateRole(user, UserRole.ORGANIZER));
    }

    @Test
    void validateRole_RolIncorrecto_LanzaBusinessRule() {
        User user = buildUser("PLAYER");
        assertThrows(BusinessRuleException.class, () ->
                validator.validateRole(user, UserRole.ORGANIZER));
    }

    @Test
    void validateRole_UsuarioNulo_LanzaBusinessRule() {
        assertThrows(BusinessRuleException.class, () ->
                validator.validateRole(null, UserRole.ORGANIZER));
    }


    @Test
    void validatePermission_PermisoCorrecto_NoLanzaExcepcion() {
        User user = buildUser("ORGANIZER");
        assertDoesNotThrow(() ->
                validator.validatePermission(user, "CREATE_TOURNAMENT"));
    }

    @Test
    void validatePermission_SinPermiso_LanzaBusinessRule() {
        User user = buildUser("PLAYER");
        assertThrows(BusinessRuleException.class, () ->
                validator.validatePermission(user, "CREATE_TOURNAMENT"));
    }

    @Test
    void validatePermission_UsuarioNulo_LanzaBusinessRule() {
        assertThrows(BusinessRuleException.class, () ->
                validator.validatePermission(null, "CREATE_TOURNAMENT"));
    }


    @Test
    void validateAnyPermission_TieneUnPermiso_NoLanzaExcepcion() {
        User user = buildUser("CAPTAIN");
        assertDoesNotThrow(() ->
                validator.validateAnyPermission(user,
                        "CREATE_TOURNAMENT", "CREATE_TEAM"));
    }

    @Test
    void validateAnyPermission_SinNingunPermiso_LanzaBusinessRule() {
        User user = buildUser("PLAYER");
        assertThrows(BusinessRuleException.class, () ->
                validator.validateAnyPermission(user,
                        "CREATE_TOURNAMENT", "FINALIZE_TOURNAMENT"));
    }

    @Test
    void validateAnyPermission_UsuarioNulo_LanzaBusinessRule() {
        assertThrows(BusinessRuleException.class, () ->
                validator.validateAnyPermission(null, "CREATE_TEAM"));
    }


    @Test
    void validateOwnership_MismoUsuario_NoLanzaExcepcion() {
        assertDoesNotThrow(() ->
                validator.validateOwnership(1L, 1L));
    }

    @Test
    void validateOwnership_DistintoUsuario_LanzaBusinessRule() {
        assertThrows(BusinessRuleException.class, () ->
                validator.validateOwnership(1L, 2L));
    }


    @Test
    void getUserRole_RolValido_RetornaEnum() {
        User user = buildUser("CAPTAIN");
        assertEquals(UserRole.CAPTAIN, validator.getUserRole(user));
    }

    @Test
    void getUserRole_RolInvalido_LanzaBusinessRule() {
        User user = buildUser("ROL_INVALIDO");
        assertThrows(BusinessRuleException.class, () ->
                validator.getUserRole(user));
    }

    @Test
    void getUserRole_UsuarioNulo_LanzaBusinessRule() {
        assertThrows(BusinessRuleException.class, () ->
                validator.getUserRole(null));
    }
}