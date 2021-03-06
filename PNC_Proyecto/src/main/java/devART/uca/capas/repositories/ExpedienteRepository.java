package devART.uca.capas.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import devART.uca.capas.domain.Expediente;


public interface ExpedienteRepository extends JpaRepository<Expediente, Integer> {
	@Query(nativeQuery=true, value="SELECT * FROM public.expediente order by c_expediente ASC")
	public List<Expediente> mostrarTodo() throws DataAccessException;

	@Query(nativeQuery=true, value="SELECT * FROM public.expediente WHERE c_expediente = ?1 order by c_expediente ASC")
	public List<Expediente> mostrarPorID(Integer cadena) throws DataAccessException;

	@Query(nativeQuery=true, value="SELECT * FROM public.expediente WHERE s_nombre= ?1 or s_apellido = ?1 or s_carne=?1 order by c_expediente ASC")
	public List<Expediente> mostrarPorGeneral(String cadena) throws DataAccessException;

	@Query(nativeQuery=true, value="SELECT * FROM public.expediente WHERE s_nombre like %?1% order by c_expediente ASC")
	public List<Expediente> mostrarPorNombre(String cadena) throws DataAccessException;

	@Query(nativeQuery=true, value="SELECT * FROM public.expediente WHERE s_apellido like %?1% order by c_expediente ASC")
	public List<Expediente> mostrarPorApellido(String cadena) throws DataAccessException;

	@Query(nativeQuery=true, value="SELECT * FROM public.expediente WHERE s_carne like %?1% order by c_expediente ASC")
	public List<Expediente> mostrarPorCarne(String cadena) throws DataAccessException;

//	@Query(nativeQuery=true, value="SELECT * FROM public.expediente WHERE s_institucion like %?1% order by c_expediente ASC")
//	public List<Expediente> mostrarPorCentro(String cadena) throws DataAccessException;

}
//select * from  public.expediente  where s_nombre  like any (array['%trix', 'trix%', '%trix%', '_Trix_', '_Trix', 'Trix_']);