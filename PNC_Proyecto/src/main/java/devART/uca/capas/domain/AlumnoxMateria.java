package devART.uca.capas.domain;

import devART.uca.capas.domain.Expediente;
import devART.uca.capas.domain.Materia;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(schema="public", name="alumnoxmateria")
public class AlumnoxMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "incrementalumnoxmateria")
    @GenericGenerator(name = "incrementalumnoxmateria", strategy = "increment")
    @Column(name="c_alumnoxmateria")
    private Integer c_alumnoxmateria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_materia", nullable = false)
    private Materia materia ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_expediente", nullable = false)
    private Expediente expediente;

    @Column(name="s_nota")
    @NotNull(message="No puede estar vacio")
    @Min(value = 0,message = "La nota no puede ser menor a 0")
    @Max(value = 10,message = "La nota no puede ser mayor a 10")
    private double nota;

    @Column(name = "s_estado")
    private String estado;

    @Column(name = "i_annio_materia")
    @NotNull(message="No puede estar vacio")
    @Min(value = 2005,message = "El año no puede ser menor a 2005")
    @Max(value = 2020,message = "La año no puede ser mayor a 2020")
    private Integer annio;

    @Column(name = "s_ciclo")
    @NotNull(message = "Este campo no puede estar vacio")
    private String ciclo;

    public AlumnoxMateria(){}

    public Integer getC_alumnoxmateria() {
        return c_alumnoxmateria;
    }

    public void setC_alumnoxmateria(Integer c_alumnoxmateria) {
        this.c_alumnoxmateria = c_alumnoxmateria;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getAnnio() {
        return annio;
    }

    public void setAnnio(Integer annio) {
        this.annio = annio;
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }
}
