package edu.dosw.proyect.core.security;

import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Se ejecuta cuando Google autentica exitosamente a un usuario.
 *
 * Flujo:
 * 1. Obtiene el email del perfil de Google.
 * 2. Valida que sea un correo Gmail.
 * 3. Busca el usuario en la BD — si no existe, rechaza el acceso.
 * 4. Genera un JWT con el rol que ya tiene registrado.
 * 5. Redirige al frontend con el token en ?token=xxx
 *    o con el error en ?error=xxx si algo falla.
 *
 * Solo pueden usar Google: familiares, organizadores y árbitros
 * que hayan sido registrados previamente en el sistema.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider    jwtProvider;
    private final UserRepository userRepository;

    @Value("${app.oauth2.redirect-uri:http://localhost:5173/oauth2/callback}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        log.info("Intento de login con Google para: {}", email);

        if (email == null) {
            log.error("No se pudo obtener el email del perfil de Google");
            getRedirectStrategy().sendRedirect(request, response,
                    redirectUri + "?error=No se pudo obtener el email de Google");
            return;
        }

        if (!email.endsWith("@gmail.com")) {
            log.warn("Intento de login con Google usando correo no Gmail: {}", email);
            getRedirectStrategy().sendRedirect(request, response,
                    redirectUri + "?error=Solo se permite autenticación con Google " +
                            "para cuentas Gmail. El personal institucional debe " +
                            "usar el login tradicional.");
            return;
        }

        UserEntity user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            log.warn("Login con Google rechazado — usuario no registrado: {}", email);
            getRedirectStrategy().sendRedirect(request, response,
                    redirectUri + "?error=Usuario no registrado en el sistema. " +
                            "Debe registrarse antes de iniciar sesión con Google.");
            return;
        }

        String role = user.getRole();
        log.info("ROL DEL USUARIO DESDE BD: {}", role);
        if (!role.equals("FAMILY_MEMBER") &&
                !role.equals("ORGANIZER") &&
                !role.equals("REFEREE")) {
            log.warn("Login con Google rechazado — rol no permitido: {} para usuario: {}",
                    role, email);
            getRedirectStrategy().sendRedirect(request, response,
                    redirectUri + "?error=Su tipo de usuario debe iniciar sesión " +
                            "con correo institucional.");
            return;
        }

        String token = jwtProvider.generateToken(user.getEmail(), user.getRole(), user.getId());
        log.info("Login con Google exitoso para: {} (rol: {})", email, role);

        getRedirectStrategy().sendRedirect(request, response,
                redirectUri + "?token=" + token);
    }
}