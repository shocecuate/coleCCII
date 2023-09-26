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
@Table(name = "bimestreporcarrera")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bimestreporcarrera.findAll", query = "SELECT b FROM Bimestreporcarrera b")
    , @NamedQuery(name = "Bimestreporcarrera.findByIdBimestreporcarrera", query = "SELECT b FROM Bimestreporcarrera b WHERE b.idBimestreporcarrera = :idBimestreporcarrera")})
public class Bimestreporcarrera implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_bimestreporcarrera")
    private Integer idBimestreporcarrera;
    @JoinColumn(name = "id_bimestre", referencedColumnName = "id_bimestre")
    @ManyToOne(optional = false)
    private Bimestre idBimestre;
    @JoinColumn(name = "id_carrera", referencedColumnName = "id_carrera")
    @ManyToOne(optional = false)
    private Carrera idCarrera;

    public Bimestreporcarrera() {
    }

    public Bimestreporcarrera(Integer idBimestreporcarrera) {
        this.idBimestreporcarrera = idBimestreporcarrera;
    }

    public Integer getIdBimestreporcarrera() {
        return idBimestreporcarrera;
    }

    public void setIdBimestreporcarrera(Integer idBimestreporcarrera) {
        this.idBimestreporcarrera = idBimestreporcarrera;
    }

    public Bimestre getIdBimestre() {
        return idBimestre;
    }

    public void setIdBimestre(Bimestre idBimestre) {
        this.idBimestre = idBimestre;
    }

    public Carrera getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Carrera idCarrera) {
        this.idCarrera = idCarrera;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBimestreporcarrera != null ? idBimestreporcarrera.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bimestreporcarrera)) {
            return false;
        }
        Bimestreporcarrera other = (Bimestreporcarrera) object;
        if ((this.idBimestreporcarrera == null && other.idBimestreporcarrera != null) || (this.idBimestreporcarrera != null && !this.idBimestreporcarrera.equals(other.idBimestreporcarrera))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Bimestreporcarrera[ idBimestreporcarrera=" + idBimestreporcarrera + " ]";
    }
    
}
