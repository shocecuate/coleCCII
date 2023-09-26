/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author luismorales
 */
@Entity
@Table(name = "mensaje_asistencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MensajeAsistencia.findAll", query = "SELECT m FROM MensajeAsistencia m")
    , @NamedQuery(name = "MensajeAsistencia.findByIdMensaje", query = "SELECT m FROM MensajeAsistencia m WHERE m.mensajeAsistenciaPK.idMensaje = :idMensaje")
    , @NamedQuery(name = "MensajeAsistencia.findByIdHorario", query = "SELECT m FROM MensajeAsistencia m WHERE m.mensajeAsistenciaPK.idHorario = :idHorario")
    , @NamedQuery(name = "MensajeAsistencia.findByFecha", query = "SELECT m FROM MensajeAsistencia m WHERE m.mensajeAsistenciaPK.fecha = :fecha")})
public class MensajeAsistencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MensajeAsistenciaPK mensajeAsistenciaPK;
    @JoinColumn(name = "id_mensaje", referencedColumnName = "id_mensaje", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Mensaje mensaje;
    @JoinColumn(name = "id_horario", referencedColumnName = "id_horario", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Horario horario;

    public MensajeAsistencia() {
    }

    public MensajeAsistencia(MensajeAsistenciaPK mensajeAsistenciaPK) {
        this.mensajeAsistenciaPK = mensajeAsistenciaPK;
    }

    public MensajeAsistencia(int idMensaje, int idHorario, Date fecha) {
        this.mensajeAsistenciaPK = new MensajeAsistenciaPK(idMensaje, idHorario, fecha);
    }

    public MensajeAsistenciaPK getMensajeAsistenciaPK() {
        return mensajeAsistenciaPK;
    }

    public void setMensajeAsistenciaPK(MensajeAsistenciaPK mensajeAsistenciaPK) {
        this.mensajeAsistenciaPK = mensajeAsistenciaPK;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mensajeAsistenciaPK != null ? mensajeAsistenciaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MensajeAsistencia)) {
            return false;
        }
        MensajeAsistencia other = (MensajeAsistencia) object;
        if ((this.mensajeAsistenciaPK == null && other.mensajeAsistenciaPK != null) || (this.mensajeAsistenciaPK != null && !this.mensajeAsistenciaPK.equals(other.mensajeAsistenciaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.MensajeAsistencia[ mensajeAsistenciaPK=" + mensajeAsistenciaPK + " ]";
    }
    
}
