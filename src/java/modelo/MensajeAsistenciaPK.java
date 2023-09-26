/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author luismorales
 */
@Embeddable
public class MensajeAsistenciaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_mensaje")
    private int idMensaje;
    @Basic(optional = false)
    @Column(name = "id_horario")
    private int idHorario;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    public MensajeAsistenciaPK() {
    }

    public MensajeAsistenciaPK(int idMensaje, int idHorario, Date fecha) {
        this.idMensaje = idMensaje;
        this.idHorario = idHorario;
        this.fecha = fecha;
    }

    public int getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idMensaje;
        hash += (int) idHorario;
        hash += (fecha != null ? fecha.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MensajeAsistenciaPK)) {
            return false;
        }
        MensajeAsistenciaPK other = (MensajeAsistenciaPK) object;
        if (this.idMensaje != other.idMensaje) {
            return false;
        }
        if (this.idHorario != other.idHorario) {
            return false;
        }
        if ((this.fecha == null && other.fecha != null) || (this.fecha != null && !this.fecha.equals(other.fecha))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.MensajeAsistenciaPK[ idMensaje=" + idMensaje + ", idHorario=" + idHorario + ", fecha=" + fecha + " ]";
    }
    
}
