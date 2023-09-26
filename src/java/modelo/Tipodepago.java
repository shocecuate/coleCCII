/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author luismorales
 */
@Entity
@Table(name = "tipodepago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipodepago.findAll", query = "SELECT t FROM Tipodepago t")
    , @NamedQuery(name = "Tipodepago.findByIdTipoPago", query = "SELECT t FROM Tipodepago t WHERE t.idTipoPago = :idTipoPago")
    , @NamedQuery(name = "Tipodepago.findByNombreTipoPago", query = "SELECT t FROM Tipodepago t WHERE t.nombreTipoPago = :nombreTipoPago")
    , @NamedQuery(name = "Tipodepago.findByDescripcion", query = "SELECT t FROM Tipodepago t WHERE t.descripcion = :descripcion")})
public class Tipodepago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idTipoPago")
    private Integer idTipoPago;
    @Basic(optional = false)
    @Column(name = "nombreTipoPago")
    private String nombreTipoPago;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoPago")
    private List<Pagos> pagosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoPago")
    private List<Inscripcion> inscripcionList;

    public Tipodepago() {
    }

    public Tipodepago(Integer idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public Tipodepago(Integer idTipoPago, String nombreTipoPago) {
        this.idTipoPago = idTipoPago;
        this.nombreTipoPago = nombreTipoPago;
    }

    public Integer getIdTipoPago() {
        return idTipoPago;
    }

    public void setIdTipoPago(Integer idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public String getNombreTipoPago() {
        return nombreTipoPago;
    }

    public void setNombreTipoPago(String nombreTipoPago) {
        this.nombreTipoPago = nombreTipoPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Pagos> getPagosList() {
        return pagosList;
    }

    public void setPagosList(List<Pagos> pagosList) {
        this.pagosList = pagosList;
    }

    @XmlTransient
    public List<Inscripcion> getInscripcionList() {
        return inscripcionList;
    }

    public void setInscripcionList(List<Inscripcion> inscripcionList) {
        this.inscripcionList = inscripcionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoPago != null ? idTipoPago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipodepago)) {
            return false;
        }
        Tipodepago other = (Tipodepago) object;
        if ((this.idTipoPago == null && other.idTipoPago != null) || (this.idTipoPago != null && !this.idTipoPago.equals(other.idTipoPago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Tipodepago[ idTipoPago=" + idTipoPago + " ]";
    }
    
}
