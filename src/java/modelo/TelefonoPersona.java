/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author luismorales
 */
@Entity
@Table(name = "telefono_persona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TelefonoPersona.findAll", query = "SELECT t FROM TelefonoPersona t")
    , @NamedQuery(name = "TelefonoPersona.findByIdTelefonoPersona", query = "SELECT t FROM TelefonoPersona t WHERE t.idTelefonoPersona = :idTelefonoPersona")
    , @NamedQuery(name = "TelefonoPersona.findByNumero", query = "SELECT t FROM TelefonoPersona t WHERE t.numero = :numero")
    , @NamedQuery(name = "TelefonoPersona.findByTipo", query = "SELECT t FROM TelefonoPersona t WHERE t.tipo = :tipo")})
public class TelefonoPersona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_telefono_persona")
    private Integer idTelefonoPersona;
    @Basic(optional = false)
    @Column(name = "numero")
    private String numero;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    @ManyToOne(optional = false)
    private Persona idPersona;

    public TelefonoPersona() {
    }

    public TelefonoPersona(Integer idTelefonoPersona) {
        this.idTelefonoPersona = idTelefonoPersona;
    }

    public TelefonoPersona(Integer idTelefonoPersona, String numero, String tipo) {
        this.idTelefonoPersona = idTelefonoPersona;
        this.numero = numero;
        this.tipo = tipo;
    }

    public Integer getIdTelefonoPersona() {
        return idTelefonoPersona;
    }

    public void setIdTelefonoPersona(Integer idTelefonoPersona) {
        this.idTelefonoPersona = idTelefonoPersona;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
        hash += (idTelefonoPersona != null ? idTelefonoPersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TelefonoPersona)) {
            return false;
        }
        TelefonoPersona other = (TelefonoPersona) object;
        if ((this.idTelefonoPersona == null && other.idTelefonoPersona != null) || (this.idTelefonoPersona != null && !this.idTelefonoPersona.equals(other.idTelefonoPersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.TelefonoPersona[ idTelefonoPersona=" + idTelefonoPersona + " ]";
    }
    
}
