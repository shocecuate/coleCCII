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
@Table(name = "curso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Curso.findAll", query = "SELECT c FROM Curso c")
    , @NamedQuery(name = "Curso.findByIdCurso", query = "SELECT c FROM Curso c WHERE c.idCurso = :idCurso")
    , @NamedQuery(name = "Curso.findByNombre", query = "SELECT c FROM Curso c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Curso.findByDescripcion", query = "SELECT c FROM Curso c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "Curso.findByFechaInicio", query = "SELECT c FROM Curso c WHERE c.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Curso.findByFechaFin", query = "SELECT c FROM Curso c WHERE c.fechaFin = :fechaFin")
    , @NamedQuery(name = "Curso.findByActivo", query = "SELECT c FROM Curso c WHERE c.activo = :activo")
    , @NamedQuery(name = "Curso.findByEliminado", query = "SELECT c FROM Curso c WHERE c.eliminado = :eliminado")})
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_curso")
    private Integer idCurso;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @Column(name = "activo")
    private boolean activo;
    @Basic(optional = false)
    @Column(name = "eliminado")
    private boolean eliminado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCurso")
    private List<Horario> horarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCurso")
    private List<Tarea> tareaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCurso")
    private List<Materiacursoporusuario> materiacursoporusuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCurso")
    private List<Nota> notaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCurso")
    private List<Examen> examenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCurso")
    private List<Cursoporcarrera> cursoporcarreraList;

    public Curso() {
    }

    public Curso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public Curso(Integer idCurso, String nombre, Date fechaInicio, Date fechaFin, boolean activo, boolean eliminado) {
        this.idCurso = idCurso;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
        this.eliminado = eliminado;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    @XmlTransient
    public List<Horario> getHorarioList() {
        return horarioList;
    }

    public void setHorarioList(List<Horario> horarioList) {
        this.horarioList = horarioList;
    }

    @XmlTransient
    public List<Tarea> getTareaList() {
        return tareaList;
    }

    public void setTareaList(List<Tarea> tareaList) {
        this.tareaList = tareaList;
    }

    @XmlTransient
    public List<Materiacursoporusuario> getMateriacursoporusuarioList() {
        return materiacursoporusuarioList;
    }

    public void setMateriacursoporusuarioList(List<Materiacursoporusuario> materiacursoporusuarioList) {
        this.materiacursoporusuarioList = materiacursoporusuarioList;
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
        hash += (idCurso != null ? idCurso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Curso)) {
            return false;
        }
        Curso other = (Curso) object;
        if ((this.idCurso == null && other.idCurso != null) || (this.idCurso != null && !this.idCurso.equals(other.idCurso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Curso[ idCurso=" + idCurso + " ]";
    }
    
}
