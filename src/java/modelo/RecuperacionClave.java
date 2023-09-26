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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author luismorales
 */
@Entity
@Table(name = "recuperacion_clave")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecuperacionClave.findAll", query = "SELECT r FROM RecuperacionClave r")
    , @NamedQuery(name = "RecuperacionClave.findByIdRecuperacion", query = "SELECT r FROM RecuperacionClave r WHERE r.idRecuperacion = :idRecuperacion")
    , @NamedQuery(name = "RecuperacionClave.findByCodigoRecuperacion", query = "SELECT r FROM RecuperacionClave r WHERE r.codigoRecuperacion = :codigoRecuperacion")
    , @NamedQuery(name = "RecuperacionClave.findByFechaSolicitud", query = "SELECT r FROM RecuperacionClave r WHERE r.fechaSolicitud = :fechaSolicitud")
    , @NamedQuery(name = "RecuperacionClave.findByFechaExpiracion", query = "SELECT r FROM RecuperacionClave r WHERE r.fechaExpiracion = :fechaExpiracion")
    , @NamedQuery(name = "RecuperacionClave.findByCodigoUtilizado", query = "SELECT r FROM RecuperacionClave r WHERE r.codigoUtilizado = :codigoUtilizado")})
public class RecuperacionClave implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_recuperacion")
    private Integer idRecuperacion;
    @Basic(optional = false)
    @Column(name = "codigo_recuperacion")
    private String codigoRecuperacion;
    @Basic(optional = false)
    @Column(name = "fecha_solicitud")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitud;
    @Basic(optional = false)
    @Column(name = "fecha_expiracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpiracion;
    @Basic(optional = false)
    @Column(name = "codigo_utilizado")
    private boolean codigoUtilizado;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    public RecuperacionClave() {
    }

    public RecuperacionClave(Integer idRecuperacion) {
        this.idRecuperacion = idRecuperacion;
    }

    public RecuperacionClave(Integer idRecuperacion, String codigoRecuperacion, Date fechaSolicitud, Date fechaExpiracion, boolean codigoUtilizado) {
        this.idRecuperacion = idRecuperacion;
        this.codigoRecuperacion = codigoRecuperacion;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaExpiracion = fechaExpiracion;
        this.codigoUtilizado = codigoUtilizado;
    }

    public Integer getIdRecuperacion() {
        return idRecuperacion;
    }

    public void setIdRecuperacion(Integer idRecuperacion) {
        this.idRecuperacion = idRecuperacion;
    }

    public String getCodigoRecuperacion() {
        return codigoRecuperacion;
    }

    public void setCodigoRecuperacion(String codigoRecuperacion) {
        this.codigoRecuperacion = codigoRecuperacion;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public boolean getCodigoUtilizado() {
        return codigoUtilizado;
    }

    public void setCodigoUtilizado(boolean codigoUtilizado) {
        this.codigoUtilizado = codigoUtilizado;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRecuperacion != null ? idRecuperacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecuperacionClave)) {
            return false;
        }
        RecuperacionClave other = (RecuperacionClave) object;
        if ((this.idRecuperacion == null && other.idRecuperacion != null) || (this.idRecuperacion != null && !this.idRecuperacion.equals(other.idRecuperacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.RecuperacionClave[ idRecuperacion=" + idRecuperacion + " ]";
    }
    
}
