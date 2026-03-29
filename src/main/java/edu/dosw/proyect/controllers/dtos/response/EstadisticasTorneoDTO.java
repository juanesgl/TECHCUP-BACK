package edu.dosw.proyect.controllers.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasTorneoDTO {
    private String torneoId;
    private int totalPartidosJugados;
    private int totalGolesAnotados;
    private List<EstadisticasEquipoDTO> tablaPosiciones;
    private List<EstadisticasJugadorDTO> tablaGoleadores;
    private List<EstadisticasJugadorDTO> tablaTarjetas;

    public String getTorneoId() {
        return torneoId;
    }

    public void setTorneoId(String id) {
        this.torneoId = id;
    }

    public int getTotalPartidosJugados() {
        return totalPartidosJugados;
    }

    public void setTotalPartidosJugados(int n) {
        this.totalPartidosJugados = n;
    }

    public int getTotalGolesAnotados() {
        return totalGolesAnotados;
    }

    public void setTotalGolesAnotados(int n) {
        this.totalGolesAnotados = n;
    }

    public List<EstadisticasEquipoDTO> getTablaPosiciones() {
        return tablaPosiciones;
    }

    public void setTablaPosiciones(List<EstadisticasEquipoDTO> l) {
        this.tablaPosiciones = l;
    }

    public List<EstadisticasJugadorDTO> getTablaGoleadores() {
        return tablaGoleadores;
    }

    public void setTablaGoleadores(List<EstadisticasJugadorDTO> l) {
        this.tablaGoleadores = l;
    }

    public List<EstadisticasJugadorDTO> getTablaTarjetas() {
        return tablaTarjetas;
    }

    public void setTablaTarjetas(List<EstadisticasJugadorDTO> l) {
        this.tablaTarjetas = l;
    }

    public static EstadisticasTorneoDTOBuilder builder() {
        return new EstadisticasTorneoDTOBuilder();
    }

    public static class EstadisticasTorneoDTOBuilder {
        private EstadisticasTorneoDTO dto = new EstadisticasTorneoDTO();

        public EstadisticasTorneoDTOBuilder torneoId(String id) {
            dto.setTorneoId(id);
            return this;
        }

        public EstadisticasTorneoDTOBuilder totalPartidosJugados(int n) {
            dto.setTotalPartidosJugados(n);
            return this;
        }

        public EstadisticasTorneoDTOBuilder totalGolesAnotados(int n) {
            dto.setTotalGolesAnotados(n);
            return this;
        }

        public EstadisticasTorneoDTOBuilder tablaPosiciones(List<EstadisticasEquipoDTO> l) {
            dto.setTablaPosiciones(l);
            return this;
        }

        public EstadisticasTorneoDTOBuilder tablaGoleadores(List<EstadisticasJugadorDTO> l) {
            dto.setTablaGoleadores(l);
            return this;
        }

        public EstadisticasTorneoDTOBuilder tablaTarjetas(List<EstadisticasJugadorDTO> l) {
            dto.setTablaTarjetas(l);
            return this;
        }

        public EstadisticasTorneoDTO build() {
            return dto;
        }
    }
}
