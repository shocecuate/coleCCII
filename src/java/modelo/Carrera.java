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
@Table(name = "carrera")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carrera.findAll", query = "SELECT c FROM Carrera c")
    , @NamedQuery(name = "Carrera.findByIdCarrera", query = "SELECT c FROM Carrera c WHERE c.idCarrera = :idCarrera")
    , @NamedQuery(name = "Carrera.findByNombre", query = "SELECT c FROM Carrera c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Carrera.findByCodigo", query = "SELECT c FROM Carrera c WHERE c.codigo = :codigo")})
public class Carrera implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_carrera")
    private Integer idCarrera;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "codigo")
    private String codigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCarrera")
    private List<Bimestreporcarrera> bimestreporcarreraList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCarrera")
    private List<Alumno> alumnoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCarrera")
    private List<Cursoporcarrera> cursoporcarreraList;

    public Carrera() {
    }

    public Carrera(Integer idCarrera) {
        this.idCarrera = idCarrera;
    }

    public Carrera(Integer idCarrera, String nombre, String codigo) {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public Integer getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Integer idCarrera) {
        this.idCarrera = idCarrera;
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

    @XmlTransient
    public List<Bimestreporcarrera> getBimestreporcarreraList() {
        return bimestreporcarreraList;
    }

    public void setBimestreporcarreraList(List<Bimestreporcarrera> bimestreporcarreraList) {
        this.bimestreporcarreraList = bimestreporcarreraList;
    }

    @XmlTransient
    public List<Alumno> getAlumnoList() {
        return alumnoList;
    }

    public void setAlumnoList(List<Alumno> alumnoList) {
        this.alumnoList = alumnoList;
    }

    @XmlTransient
    public List<Cursoporcarrera> getCursoporcarreraList() {
        return cursoporcarreraList;
    }

    public void setCursoporcarreraList(List<Cursoporcarrera> cursoporcarreraList) {
        this.cursoporcarreraList = cursoporcarreraList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCarrera != null ? idCarrera.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Carrera)) {
            return false;
        }
        Carrera other = (Carrera) object;
        if ((this.idCarrera == null && other.idCarrera != null) || (this.idCarrera != null && !this.idCarrera.equals(other.idCarrera))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Carrera[ idCarrera=" + idCarrera + " ]";
    }
    
}
