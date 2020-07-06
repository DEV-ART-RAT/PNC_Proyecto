package devART.uca.capas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import devART.uca.capas.domain.Expediente;
import devART.uca.capas.repositories.ExpedienteRepository;

@Service
public class ExpedienteServiceImpl implements ExpedienteService{
	@Autowired
	private ExpedienteRepository expedienteRepo;
	
	
	@Override
	public List<Expediente> findAllExpe() throws DataAccessException {
			return expedienteRepo.mostrarTodo();	
	}

	@Override
	@Transactional
	public void insert(Expediente expediente) throws DataAccessException {
			
		// TODO Auto-generated method stub
		expedienteRepo.save(expediente);
	}
}