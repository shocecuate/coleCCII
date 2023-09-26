/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author luismorales
 */
@Entity
@Table(name = "mensaje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mensaje.findAll", query = "SELECT m FROM Mensaje m")
    , @NamedQuery(name = "Mensaje.findByIdMensaje", query = "SELECT m FROM Mensaje m WHERE m.idMensaje = :idMensaje")
    , @NamedQuery(name = "Mensaje.findByTipoMensaje", query = "SELECT m FROM Mensaje m WHERE m.tipoMensaje = :tipoMensaje")
    , @NamedQuery(name = "Mensaje.findByFechaEnvio", query = "SELECT m FROM Mensaje m WHERE m.fechaEnvio = :fechaEnvio")})
public class Mensaje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_mensaje")
    private Integer idMensaje;
    @Basic(optional = false)
    @Column(name = "tipo_mensaje")
    private String tipoMensaje;
    @Basic(optional = false)
    @Column(name = "fecha_envio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;
    @JoinTable(name = "mensaje_tarea_entregada", joinColumns = {
        @JoinColumn(name = "id_mensaje", referencedColumnName = "id_mensaje")}, inverseJoinColumns = {
        @JoinColumn(name = "id_tarea", referencedColumnName = "id_tarea")})
    @ManyToMany
    private List<Tarea> tareaList;
    @JoinTable(name = "mensaje_tarea_pendiente", joinColumns = {
        @JoinColumn(name = "id_mensaje", referencedColumnName = "id_mensaje")}, inverseJoinColumns = {
        @JoinColumn(name = "id_tarea", referencedColumnName = "id_tarea")})
    @ManyToMany
    private List<Tarea> tareaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mensaje")
    private List<MensajeAsistencia> mensajeAsistenciaList;
    @JoinColumn(name = "id_destinatario", referencedColumnName = "id_persona")
    @ManyToOne(optional = false)
    private Persona idDestinatario;

    public Mensaje() {
    }

    public Mensaje(Integer idMensaje) {
        this.idMensaje = idMensaje;
    }

    public Mensaje(Integer idMensaje, String tipoMensaje, Date fechaEnvio) {
        this.idMensaje = idMensaje;
        this.tipoMensaje = tipoMensaje;
        this.fechaEnvio = fechaEnvio;
    }

    public Integer getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(Integer idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    @XmlTransient
    public List<Tarea> getTareaList() {
        return tareaList;
    }

    public void setTareaList(List<Tarea> tareaList) {
        this.tareaList = tareaList;
    }

    @XmlTransient
    public List<Tarea> getTareaList1() {
        return tareaList1;
    }

    public void setTareaList1(List<Tarea> tareaList1) {
        this.tareaList1 = tareaList1;
    }

    @XmlTransient
    public List<MensajeAsistencia> getMensajeAsistenciaList() {
        return mensajeAsistenciaList;
    }

    public void setMensajeAsistenciaList(List<MensajeAsistencia> mensajeAsistenciaList) {
        this.mensajeAsistenciaList = mensajeAsistenciaList;
    }

    public Persona getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(Persona idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMensaje != null ? idMensaje.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mensaje)) {
            return false;
        }
        Mensaje other = (Mensaje) object;
        if ((this.idMensaje == null && other.idMensaje != null) || (this.idMensaje != null && !this.idMensaje.equals(other.idMensaje))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Mensaje[ idMensaje=" + idMensaje + " ]";
    }
    
}
