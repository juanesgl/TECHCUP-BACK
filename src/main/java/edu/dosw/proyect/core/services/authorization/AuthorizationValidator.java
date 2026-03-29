package edu.dosw.proyect.core.services.authorization;

import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Validador centralizado de permisos y autorizaciones.
 * Encapsula toda la lógica de control de acceso basada en roles.
 */
@Slf4j
@Component
public class AuthorizationValidator {

    /**
     * Valida que el usuario tenga un rol específico.
     */
    public void validateRole(User user, UserRole requiredRole) {
        if (user == null) {
            throw new BusinessRuleException("Usuario no encontrado");
        }
        
        UserRole userRole = UserRole.valueOf(user.getRole());
        if (!userRole.equals(requiredRole)) {
            log.warn("Acceso denegado: usuario {} (rol: {}) intentó accesar recurso de {}", 
                    user.getId(), userRole, requiredRole);
            throw new BusinessRuleException(
                    "Solo " + requiredRole.getDisplayName() + "s pueden realizar esta acción");
        }
    }

    /**
     * Valida que el usuario tenga el permiso específico.
     */
    public void validatePermission(User user, String permission) {
        if (user == null) {
            throw new BusinessRuleException("Usuario no encontrado");
        }
        
        UserRole userRole = UserRole.valueOf(user.getRole());
        if (!userRole.hasPermission(permission)) {
            log.warn("Permiso denegado: usuario {} (rol: {}) no tiene permiso: {}", 
                    user.getId(), userRole, permission);
            throw new BusinessRuleException(
                    "No tiene permiso para: " + permission);
        }
    }

    /**
     * Valida que el usuario tenga uno de varios permisos.
     */
    public void validateAnyPermission(User user, String... permissions) {
        if (user == null) {
            throw new BusinessRuleException("Usuario no encontrado");
        }
        
        UserRole userRole = UserRole.valueOf(user.getRole());
        for (String permission : permissions) {
            if (userRole.hasPermission(permission)) {
                return;
            }
        }
        
        log.warn("Permisos denegados: usuario {} no tiene ninguno de los permisos requeridos", 
                user.getId());
        throw new BusinessRuleException("No tiene permisos suficientes para realizar esta acción");
    }

    /**
     * Valida que el usuario sea el propietario del recurso (para acciones personales).
     */
    public void validateOwnership(Long userId, Long resourceOwnerId) {
        if (!userId.equals(resourceOwnerId)) {
            log.warn("Acceso denegado: usuario {} intentó acceder recurso de usuario {}", 
                    userId, resourceOwnerId);
            throw new BusinessRuleException(
                    "Solo puede acceder a sus propios recursos");
        }
    }

    /**
     * Obtiene el rol del usuario.
     */
    public UserRole getUserRole(User user) {
        if (user == null) {
            throw new BusinessRuleException("Usuario no encontrado");
        }
        try {
            return UserRole.valueOf(user.getRole());
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException("Rol de usuario inválido: " + user.getRole());
        }
    }
}
