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
@Table(name = "inscripcion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inscripcion.findAll", query = "SELECT i FROM Inscripcion i")
    , @NamedQuery(name = "Inscripcion.findByIdInscripcion", query = "SELECT i FROM Inscripcion i WHERE i.idInscripcion = :idInscripcion")
    , @NamedQuery(name = "Inscripcion.findByFechaInscripcion", query = "SELECT i FROM Inscripcion i WHERE i.fechaInscripcion = :fechaInscripcion")})
public class Inscripcion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idInscripcion")
    private Integer idInscripcion;
    @Basic(optional = false)
    @Column(name = "fechaInscripcion")
    @Temporal(TemporalType.DATE)
    private Date fechaInscripcion;
    @JoinColumn(name = "idCursoporCarrera", referencedColumnName = "id_CursoPorCarrera")
    @ManyToOne(optional = false)
    private Cursoporcarrera idCursoporCarrera;
    @JoinColumn(name = "idPersona", referencedColumnName = "id_persona")
    @ManyToOne(optional = false)
    private Persona idPersona;
    @JoinColumn(name = "idTipoPago", referencedColumnName = "idTipoPago")
    @ManyToOne(optional = false)
    private Tipodepago idTipoPago;

    public Inscripcion() {
    }

    public Inscripcion(Integer idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public Inscripcion(Integer idInscripcion, Date fechaInscripcion) {
        this.idInscripcion = idInscripcion;
        this.fechaInscripcion = fechaInscripcion;
    }

    public Integer getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(Integer idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Cursoporcarrera getIdCursoporCarrera() {
        return idCursoporCarrera;
    }

    public void setIdCursoporCarrera(Cursoporcarrera idCursoporCarrera) {
        this.idCursoporCarrera = idCursoporCarrera;
    }

    public Persona getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Persona idPersona) {
        this.idPersona = idPersona;
    }

    public Tipodepago getIdTipoPago() {
        return idTipoPago;
    }

    public void setIdTipoPago(Tipodepago idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInscripcion != null ? idInscripcion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inscripcion)) {
            return false;
        }
        Inscripcion other = (Inscripcion) object;
        if ((this.idInscripcion == null && other.idInscripcion != null) || (this.idInscripcion != null && !this.idInscripcion.equals(other.idInscripcion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Inscripcion[ idInscripcion=" + idInscripcion + " ]";
    }
    
}
