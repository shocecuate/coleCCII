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
@Table(name = "bimestre")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bimestre.findAll", query = "SELECT b FROM Bimestre b")
    , @NamedQuery(name = "Bimestre.findByIdBimestre", query = "SELECT b FROM Bimestre b WHERE b.idBimestre = :idBimestre")
    , @NamedQuery(name = "Bimestre.findByNombre", query = "SELECT b FROM Bimestre b WHERE b.nombre = :nombre")
    , @NamedQuery(name = "Bimestre.findByCodigo", query = "SELECT b FROM Bimestre b WHERE b.codigo = :codigo")
    , @NamedQuery(name = "Bimestre.findByFechaInicio", query = "SELECT b FROM Bimestre b WHERE b.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Bimestre.findByFechaFin", query = "SELECT b FROM Bimestre b WHERE b.fechaFin = :fechaFin")})
public class Bimestre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_bimestre")
    private Integer idBimestre;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBimestre")
    private List<Bimestreporcarrera> bimestreporcarreraList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBimestre")
    private List<Nota> notaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBimestre")
    private List<Examen> examenList;

    public Bimestre() {
    }

    public Bimestre(Integer idBimestre) {
        this.idBimestre = idBimestre;
    }

    public Bimestre(Integer idBimestre, String nombre, String codigo, Date fechaInicio, Date fechaFin) {
        this.idBimestre = idBimestre;
        this.nombre = nombre;
        this.codigo = codigo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Integer getIdBimestre() {
        return idBimestre;
    }

    public void setIdBimestre(Integer idBimestre) {
        this.idBimestre = idBimestre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    @XmlTransient
    public List<Bimestreporcarrera> getBimestreporcarreraList() {
        return bimestreporcarreraList;
    }

    public void setBimestreporcarreraList(List<Bimestreporcarrera> bimestreporcarreraList) {
        this.bimestreporcarreraList = bimestreporcarreraList;
    }

    @XmlTransient
    public List<Nota> getNotaList() {
        return notaList;
    }

    public void setNotaList(List<Nota> notaList) {
        this.notaList = notaList;
    }

    @XmlTransient
    public List<Examen> getExamenList() {
        return examenList;
    }

    public void setExamenList(List<Examen> examenList) {
        this.examenList = examenList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBimestre != null ? idBimestre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bimestre)) {
            return false;
        }
        Bimestre other = (Bimestre) object;
        if ((this.idBimestre == null && other.idBimestre != null) || (this.idBimestre != null && !this.idBimestre.equals(other.idBimestre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Bimestre[ idBimestre=" + idBimestre + " ]";
    }
    
}
