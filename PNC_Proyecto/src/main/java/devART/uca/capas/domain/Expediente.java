package devART.uca.capas.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(schema="public", name="expediente")
public class Expediente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "incrementExpediente")
    @GenericGenerator(name = "incrementExpediente", strategy = "increment")
	@Column(name="c_expediente")
	private Integer codigo;
	
	@NotNull(message = "Este campo no puede estar vacio")
	@Column(name="s_nombre")
	@Size(message = "El nombre no debe tener mas de 50 caracteres", max = 50)
	@NotEmpty(message = "Este campo no puede estar vacio")
	private String s_nombre;
	
	@NotNull(message = "Este campo no puede estar vacio")
	@Column(name="s_apellido")
	@Size(message = "El apellido no debe tener mas de 50 caracteres", max = 50)
	@NotEmpty(message = "Este campo no puede estar vacio")
	private String s_apellido;
	//TODO(9 caracteres alfanumericos)
	
	@NotNull(message = "Este campo no puede estar vacio")
	@Column(name="s_carne")
	@Pattern(regexp = "^[a-zA-Z0-9]{9}$", message = "El carne debe tener exactamente 9 digitos alfanumericos")
	@NotEmpty(message = "Este campo no puede estar vacio")
	private String s_carne;

	@NotEmpty(message = "Este campo no puede estar vacio")
	@NotNull(message = "El campo Fecha no puede quedar vacio")
	@Pattern(regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "Lo sentimos pero no creemos que tengas esa edad")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "d_fnacimiento")
	private String d_fnacimiento;
	
	@Column(name = "s_edad")
	private String s_edad;
	
	@NotNull(message = "Este campo no puede estar vacio")
	@Column(name="s_direccion")
	@Size(message = "El nombre no debe tener mas de 50 caracteres", max = 50)
	@NotEmpty(message = "Este campo no puede estar vacio")
	private String s_direccion;
	
	@NotNull(message = "Este campo no puede estar vacio")
	@Column(name="s_telefonof")
	@Pattern(regexp = "^[0-9]{8}$", message = "El carne debe tener exactamente 8 digitos")
	@NotEmpty(message = "Este campo no puede estar vacio")
	private String s_telefonof;

	@NotNull(message = "Este campo no puede estar vacio")
	@Column(name="s_telefonom")
	@Pattern(regexp = "^[0-9]{8}$", message = "El carne debe tener exactamente 8 digitos")
	@NotEmpty(message = "Este campo no puede estar vacio")
	private String s_telefonom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "s_institucion", nullable = false)
	private Institucion institucion;

	@NotNull(message = "Este campo no puede estar vacio")
	@Column(name="s_nombrePadre")
	@Size(message = "El nombre no debe tener mas de 50 caracteres", max = 50)
	@NotEmpty(message = "Este campo no puede estar vacio")
	private String s_nombrePadre;
	
	@NotNull(message = "Este campo no puede estar vacio")
	@Column(name="s_nombreMadre")
	@Size(message = "El nombre no debe tener mas de 50 caracteres", max = 50)
	@NotEmpty(message = "Este campo no puede estar vacio")
	private String s_nombreMadre;

	@OneToMany(mappedBy="expediente", fetch = FetchType.EAGER)
	private List<AlumnoxMateria> alumnoxMaterias;

	@Transient
	private double promedio;

	@Transient
	private int reprobadas;

	@Transient
	private int aprobadas;


	public Expediente() {}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getS_nombre() {
		return s_nombre;
	}

	public void setS_nombre(String s_nombre) {
		this.s_nombre = s_nombre;
	}

	public String getS_apellido() {
		return s_apellido;
	}

	public void setS_apellido(String s_apellido) {
		this.s_apellido = s_apellido;
	}

	public String getS_carne() {
		return s_carne;
	}

	public void setS_carne(String s_carne) {
		this.s_carne = s_carne;
	}

	public String getD_fnacimiento() {
		return d_fnacimiento;
	}

	public void setD_fnacimiento(String d_fnacimiento) {
		this.d_fnacimiento = d_fnacimiento;
	}

	public String getS_edad() {
		return s_edad;
	}

	public void setS_edad(String s_edad) {
		this.s_edad = s_edad;
	}

	public String getS_direccion() {
		return s_direccion;
	}

	public void setS_direccion(String s_direccion) {
		this.s_direccion = s_direccion;
	}

	public String getS_telefonof() {
		return s_telefonof;
	}

	public void setS_telefonof(String s_telefonof) {
		this.s_telefonof = s_telefonof;
	}

	public String getS_telefonom() {
		return s_telefonom;
	}

	public void setS_telefonom(String s_telefonom) {
		this.s_telefonom = s_telefonom;
	}

	public Institucion getInstitucion() {
		return institucion;
	}

	public void setInstitucion(Institucion institucion) {
		this.institucion = institucion;
	}

	public String getS_nombrePadre() {
		return s_nombrePadre;
	}

	public void setS_nombrePadre(String s_nombrePadre) {
		this.s_nombrePadre = s_nombrePadre;
	}

	public String getS_nombreMadre() {
		return s_nombreMadre;
	}

	public void setS_nombreMadre(String s_nombreMadre) {
		this.s_nombreMadre = s_nombreMadre;
	}

	public double getPromedio() {
		if(promedio==Double.NaN)
		{return 0 ;}
		else{
		return promedio;}
	}

	public void setPromedio(double promedio) {
		this.promedio = promedio;
	}

	public int getReprobadas() {
		return reprobadas;
	}

	public void setReprobadas(int reprobadas) {
		this.reprobadas = reprobadas;
	}

	public int getAprobadas() {
		return aprobadas;
	}

	public void setAprobadas(int aprobadas) {
		this.aprobadas = aprobadas;
	}

	public List<AlumnoxMateria> getAlumnoxMaterias() {
		return alumnoxMaterias;
	}

	public void setAlumnoxMaterias(List<AlumnoxMateria> alumnoxMaterias) {
		this.alumnoxMaterias = alumnoxMaterias;
	}

	//	public String get_d_fnacimientoShort() {if(this.d_fnacimiento == null){return "";	}
//	else{
////		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//		String shortdate = sdf.format(this.d_fnacimiento.getTime());
//		return shortdate;
//	}
//}

}



//	@Pattern(regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$\n", message = "Solo Se acepta valores entre 1900 y 2999")

//https://www.baeldung.com/java-date-regular-expressions
