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
@Table(name = "documentosporpersona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documentosporpersona.findAll", query = "SELECT d FROM Documentosporpersona d")
    , @NamedQuery(name = "Documentosporpersona.findByIdDocPorPersona", query = "SELECT d FROM Documentosporpersona d WHERE d.idDocPorPersona = :idDocPorPersona")
    , @NamedQuery(name = "Documentosporpersona.findByFechaEntrega", query = "SELECT d FROM Documentosporpersona d WHERE d.fechaEntrega = :fechaEntrega")
    , @NamedQuery(name = "Documentosporpersona.findByComentario", query = "SELECT d FROM Documentosporpersona d WHERE d.comentario = :comentario")})
public class Documentosporpersona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDocPorPersona")
    private Integer idDocPorPersona;
    @Basic(optional = false)
    @Column(name = "fechaEntrega")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrega;
    @Column(name = "comentario")
    private String comentario;
    @JoinColumn(name = "idDocumento", referencedColumnName = "idDocumento")
    @ManyToOne(optional = false)
    private Documento idDocumento;
    @JoinColumn(name = "idPersona", referencedColumnName = "id_persona")
    @ManyToOne(optional = false)
    private Persona idPersona;

    public Documentosporpersona() {
    }

    public Documentosporpersona(Integer idDocPorPersona) {
        this.idDocPorPersona = idDocPorPersona;
    }

    public Documentosporpersona(Integer idDocPorPersona, Date fechaEntrega) {
        this.idDocPorPersona = idDocPorPersona;
        this.fechaEntrega = fechaEntrega;
    }

    public Integer getIdDocPorPersona() {
        return idDocPorPersona;
    }

    public void setIdDocPorPersona(Integer idDocPorPersona) {
        this.idDocPorPersona = idDocPorPersona;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Documento getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Documento idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Persona getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Persona idPersona) {
        this.idPersona = idPersona;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocPorPersona != null ? idDocPorPersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documentosporpersona)) {
            return false;
        }
        Documentosporpersona other = (Documentosporpersona) object;
        if ((this.idDocPorPersona == null && other.idDocPorPersona != null) || (this.idDocPorPersona != null && !this.idDocPorPersona.equals(other.idDocPorPersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Documentosporpersona[ idDocPorPersona=" + idDocPorPersona + " ]";
    }
    
}
