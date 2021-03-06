package devART.uca.capas.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;
import devART.uca.capas.domain.Materia;

public interface MateriaDAO {
	
	public List<Materia> findAll() throws DataAccessException;
	
	public Materia findOne(String codigo) throws DataAccessException;
	
	public void insert(Materia materia) throws DataAccessException;
	
	public void delete(Materia materia) throws DataAccessException;

}
