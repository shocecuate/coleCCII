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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "cursoporcarrera")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cursoporcarrera.findAll", query = "SELECT c FROM Cursoporcarrera c")
    , @NamedQuery(name = "Cursoporcarrera.findByIdCursoPorCarrera", query = "SELECT c FROM Cursoporcarrera c WHERE c.idCursoPorCarrera = :idCursoPorCarrera")
    , @NamedQuery(name = "Cursoporcarrera.findByCreditos", query = "SELECT c FROM Cursoporcarrera c WHERE c.creditos = :creditos")
    , @NamedQuery(name = "Cursoporcarrera.findByHorasTeoricas", query = "SELECT c FROM Cursoporcarrera c WHERE c.horasTeoricas = :horasTeoricas")
    , @NamedQuery(name = "Cursoporcarrera.findByHorasPracticas", query = "SELECT c FROM Cursoporcarrera c WHERE c.horasPracticas = :horasPracticas")})
public class Cursoporcarrera implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_CursoPorCarrera")
    private Integer idCursoPorCarrera;
    @Basic(optional = false)
    @Column(name = "creditos")
    private int creditos;
    @Basic(optional = false)
    @Column(name = "horasTeoricas")
    private int horasTeoricas;
    @Basic(optional = false)
    @Column(name = "horasPracticas")
    private int horasPracticas;
    @ManyToMany(mappedBy = "cursoporcarreraList")
    private List<Pagos> pagosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCursoporCarrera")
    private List<Inscripcion> inscripcionList;
    @JoinColumn(name = "id_materia", referencedColumnName = "id_materia")
    @ManyToOne(optional = false)
    private Materia idMateria;
    @JoinColumn(name = "idCarrera", referencedColumnName = "id_carrera")
    @ManyToOne(optional = false)
    private Carrera idCarrera;
    @JoinColumn(name = "id_Curso", referencedColumnName = "id_curso")
    @ManyToOne(optional = false)
    private Curso idCurso;

    public Cursoporcarrera() {
    }

    public Cursoporcarrera(Integer idCursoPorCarrera) {
        this.idCursoPorCarrera = idCursoPorCarrera;
    }

    public Cursoporcarrera(Integer idCursoPorCarrera, int creditos, int horasTeoricas, int horasPracticas) {
        this.idCursoPorCarrera = idCursoPorCarrera;
        this.creditos = creditos;
        this.horasTeoricas = horasTeoricas;
        this.horasPracticas = horasPracticas;
    }

    public Integer getIdCursoPorCarrera() {
        return idCursoPorCarrera;
    }

    public void setIdCursoPorCarrera(Integer idCursoPorCarrera) {
        this.idCursoPorCarrera = idCursoPorCarrera;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public int getHorasTeoricas() {
        return horasTeoricas;
    }

    public void setHorasTeoricas(int horasTeoricas) {
        this.horasTeoricas = horasTeoricas;
    }

    public int getHorasPracticas() {
        return horasPracticas;
    }

    public void setHorasPracticas(int horasPracticas) {
        this.horasPracticas = horasPracticas;
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

    public Materia getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Materia idMateria) {
        this.idMateria = idMateria;
    }

    public Carrera getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Carrera idCarrera) {
        this.idCarrera = idCarrera;
    }

    public Curso getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Curso idCurso) {
        this.idCurso = idCurso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCursoPorCarrera != null ? idCursoPorCarrera.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cursoporcarrera)) {
            return false;
        }
        Cursoporcarrera other = (Cursoporcarrera) object;
        if ((this.idCursoPorCarrera == null && other.idCursoPorCarrera != null) || (this.idCursoPorCarrera != null && !this.idCursoPorCarrera.equals(other.idCursoPorCarrera))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Cursoporcarrera[ idCursoPorCarrera=" + idCursoPorCarrera + " ]";
    }
    
}
