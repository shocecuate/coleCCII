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
@Table(name = "materiacursoporusuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Materiacursoporusuario.findAll", query = "SELECT m FROM Materiacursoporusuario m")
    , @NamedQuery(name = "Materiacursoporusuario.findByIdMateriacursoporusuario", query = "SELECT m FROM Materiacursoporusuario m WHERE m.idMateriacursoporusuario = :idMateriacursoporusuario")})
public class Materiacursoporusuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_materiacursoporusuario")
    private Integer idMateriacursoporusuario;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;
    @JoinColumn(name = "id_curso", referencedColumnName = "id_curso")
    @ManyToOne(optional = false)
    private Curso idCurso;
    @JoinColumn(name = "id_materia", referencedColumnName = "id_materia")
    @ManyToOne(optional = false)
    private Materia idMateria;

    public Materiacursoporusuario() {
    }

    public Materiacursoporusuario(Integer idMateriacursoporusuario) {
        this.idMateriacursoporusuario = idMateriacursoporusuario;
    }

    public Integer getIdMateriacursoporusuario() {
        return idMateriacursoporusuario;
    }

    public void setIdMateriacursoporusuario(Integer idMateriacursoporusuario) {
        this.idMateriacursoporusuario = idMateriacursoporusuario;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Curso getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Curso idCurso) {
        this.idCurso = idCurso;
    }

    public Materia getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Materia idMateria) {
        this.idMateria = idMateria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMateriacursoporusuario != null ? idMateriacursoporusuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Materiacursoporusuario)) {
            return false;
        }
        Materiacursoporusuario other = (Materiacursoporusuario) object;
        if ((this.idMateriacursoporusuario == null && other.idMateriacursoporusuario != null) || (this.idMateriacursoporusuario != null && !this.idMateriacursoporusuario.equals(other.idMateriacursoporusuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Materiacursoporusuario[ idMateriacursoporusuario=" + idMateriacursoporusuario + " ]";
    }
    
}
