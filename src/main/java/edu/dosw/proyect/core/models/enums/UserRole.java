package edu.dosw.proyect.core.models.enums;

import java.util.HashSet;
import java.util.Set;

public enum UserRole {
    
    ORGANIZER("Organizador", new String[]{
        "CREATE_TOURNAMENT",
        "UPDATE_TOURNAMENT",
        "MANAGE_PAYMENTS",
        "VIEW_TOURNAMENT_INFO",
        "REGISTER_MATCH_RESULTS",
        "MANAGE_LINEUP_LOCK",
        "VIEW_ALL_TEAMS",
        "FINALIZE_TOURNAMENT"
    }),
    
    CAPTAIN("CapitÃ¡n", new String[]{
        "CREATE_TEAM",
        "MANAGE_TEAM",
        "SEARCH_PLAYERS",
        "SEND_INVITATIONS",
        "UPLOAD_PAYMENT",
        "MANAGE_LINEUP",
        "VIEW_OPPONENT_LINEUP",
        "VIEW_TOURNAMENT_INFO"
    }),
    
    PLAYER("Jugador", new String[]{
        "MANAGE_PROFILE",
        "TOGGLE_AVAILABILITY",
        "RESPOND_INVITATIONS",
        "VIEW_OPPONENT_LINEUP",
        "VIEW_TOURNAMENT_INFO",
        "VIEW_MATCH_INFO"
    }),
    
    REFEREE("Ãrbitro", new String[]{
        "VIEW_TOURNAMENT_INFO",
        "VIEW_ASSIGNED_MATCHES",
        "VIEW_STANDINGS",
        "VIEW_STATISTICS"
    }),
    
    ADMINISTRATOR("Administrador", new String[]{
        "VIEW_ALL_TOURNAMENTS",
        "VIEW_ALL_STATISTICS",
        "MANAGE_USERS"
    });

    private final String displayName;
    private final Set<String> permissions;

    UserRole(String displayName, String[] permissions) {
        this.displayName = displayName;
        this.permissions = new HashSet<>();
        for (String perm : permissions) {
            this.permissions.add(perm);
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    public Set<String> getPermissions() {
        return new HashSet<>(permissions);
    }
}

