package edu.dosw.proyect.controllers.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasJugadorDTO {
    private Long jugadorId;
    private String nombreJugador;
    private String nombreEquipo;
    private int goles;
    private int tarjetasAmarillas;
    private int tarjetasRojas;

    public Long getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(Long jugadorId) {
        this.jugadorId = jugadorId;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public int getGoles() {
        return goles;
    }

    public void setGoles(int goles) {
        this.goles = goles;
    }

    public int getTarjetasAmarillas() {
        return tarjetasAmarillas;
    }

    public void setTarjetasAmarillas(int tarjetasAmarillas) {
        this.tarjetasAmarillas = tarjetasAmarillas;
    }

    public int getTarjetasRojas() {
        return tarjetasRojas;
    }

    public void setTarjetasRojas(int tarjetasRojas) {
        this.tarjetasRojas = tarjetasRojas;
    }

    public static EstadisticasJugadorDTOBuilder builder() {
        return new EstadisticasJugadorDTOBuilder();
    }

    public static class EstadisticasJugadorDTOBuilder {
        private EstadisticasJugadorDTO dto = new EstadisticasJugadorDTO();

        public EstadisticasJugadorDTOBuilder jugadorId(Long id) {
            dto.setJugadorId(id);
            return this;
        }

        public EstadisticasJugadorDTOBuilder nombreJugador(String name) {
            dto.setNombreJugador(name);
            return this;
        }

        public EstadisticasJugadorDTOBuilder nombreEquipo(String team) {
            dto.setNombreEquipo(team);
            return this;
        }

        public EstadisticasJugadorDTOBuilder goles(int g) {
            dto.setGoles(g);
            return this;
        }

        public EstadisticasJugadorDTOBuilder tarjetasAmarillas(int t) {
            dto.setTarjetasAmarillas(t);
            return this;
        }

        public EstadisticasJugadorDTOBuilder tarjetasRojas(int t) {
            dto.setTarjetasRojas(t);
            return this;
        }

        public EstadisticasJugadorDTO build() {
            return dto;
        }
    }
}
