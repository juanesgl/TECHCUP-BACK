package edu.dosw.proyect.core.models.enums;


public enum LineupStatus {
    DRAFT,    // Lineup created but not confirmed
    SAVED,    // Lineup saved and confirmed by captain
    LOCKED    // Match has started — lineup can no longer be modified
}