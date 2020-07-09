package devART.uca.capas.controller;

import java.security.Principal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.validation.Valid;

import devART.uca.capas.domain.*;
import devART.uca.capas.service.*;
import devART.uca.capas.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class CoordinadorController {
	@Autowired
	ExpedienteServiceImpl expedienteService;

	@Autowired
	MateriaService materiaService;

	@Autowired
	AlumnoxMateriaServiceImpl alumnoxMateriaService;

	private static DecimalFormat df = new DecimalFormat("0.00");


	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String listado(Principal principal) {

		User auth = (User) ((Authentication) principal).getPrincipal();
		String rol = WebUtils.getRole(auth);
		//System.out.println(rol);

		if(rol.equals("ROLE_USER")){
			return "redirect:/userCoordinador";
		}

		if(rol.equals("ROLE_ADMIN")){
			return "redirect:/admin";
		}
		return "redirect:/";
	}
	@RequestMapping(value = "/userCoordinador", method = RequestMethod.GET)
	public ModelAndView listadoCoordinador(Principal principal) {
		ModelAndView mav = new ModelAndView();
		List<Expediente> expedientes = null;
		List<Expediente> expediente = null;
		List<AlumnoxMateria> alumnoxMaterias = null;
		try {

			expedientes = expedienteService.findAllExpe();
			promediotodo(expedientes);
			aprobadasreprobadas(expedientes);
		}catch (Exception e) {
			e.printStackTrace();
		}

		mav.addObject("expedientes", expedientes);
		mav.setViewName("/Coordinador/coordinador");

		return mav;
	}

	@RequestMapping("/guardarExpediente")
	public ModelAndView guardarExpediente(Principal principal,@Valid @ModelAttribute Expediente expediente, BindingResult result) {
		ModelAndView mav = new ModelAndView();
		List<Expediente> expedientes = null;
		if(result.hasErrors()) {
			mav.setViewName("/Coordinador/AgregarExpediente");
			return mav;

		}else{
			try {
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate ahora = LocalDate.now();
				LocalDate fechaNac = LocalDate.parse(expediente.getD_fnacimiento(), fmt);
				System.out.println("Fecha Nacimiento es  "+fechaNac);
				System.out.println("getD_fnacimiento() es  "+expediente.getD_fnacimiento());
				Period periodo = Period.between(fechaNac, ahora);
				if(periodo.getYears()>999){
					expediente.setS_edad(Integer.toString(999));
					expedienteService.insert(expediente);
				}else {
					expediente.setS_edad(Integer.toString(periodo.getYears()));
					expedienteService.insert(expediente);
				}

			}catch (Exception e) {
				e.printStackTrace();
			}
			return listadoCoordinador(principal);
		}
	}

	@RequestMapping(value="/nuevaMateriaexpediente", method=RequestMethod.POST)
	public ModelAndView NuevoMateria(@RequestParam(value="codigo") Integer codigo) {
		System.out.println("Codigo final final final es :"+codigo);
		AlumnoxMateria alumnoxmateria = new AlumnoxMateria();
		ModelAndView mav = new ModelAndView();
		List<Materia> materias = null;
		materias = materiaService.findAll();
		List<Expediente> expedientes = null;
		expedientes = expedienteService.filtrarPorID(codigo);
		mav.addObject("expedientes", expedientes);
		mav.addObject("alumnoxmateria", alumnoxmateria);
		mav.addObject("materias", materias);
		mav.setViewName("/Coordinador/AgregarMateria");
		return mav;
	}
	@RequestMapping(value="/guardarExpedientemateria", method=RequestMethod.POST)
	public ModelAndView guardarExpedientemateria(Principal principal,@Valid @ModelAttribute AlumnoxMateria alumnoxMateria, BindingResult result) {
		ModelAndView mav = new ModelAndView();

		if(result.hasErrors()) {
			mav.setViewName("/Coordinador/AgregarMateria");
//			return mav;
		}else{
			try {
				System.out.println(alumnoxMateria.getNota());
				System.out.println(alumnoxMateria.getAnnio());
				System.out.println(alumnoxMateria.getCiclo());
				System.out.println(alumnoxMateria.getExpediente().getS_nombre());
				System.out.println(alumnoxMateria.getMateria().getNombreMateria());
				if(alumnoxMateria.getNota()>=6){
					alumnoxMateria.setEstado("Aprobado");
					System.out.println(alumnoxMateria.getEstado());
				}else{
					alumnoxMateria.setEstado("Reprobado");
					System.out.println(alumnoxMateria.getEstado());
				}


				alumnoxMateriaService.insert(alumnoxMateria);

			}catch (Exception e) {
				e.printStackTrace();
			}
//			mav.addObject("mensaje","Agregado con exito!");
//			AlumnoxMateria alumnoxmateria = new AlumnoxMateria();
//			List<Materia> materias = null;
//			materias = materiaService.findAll();
//			List<Expediente> expedientes = null;
//			expedientes = expedienteService.filtrarPorID(alumnoxMateria.getExpediente().getCodigo());
//			mav.addObject("expedientes", expedientes);
//			mav.addObject("alumnoxmateria", alumnoxmateria);
//			mav.addObject("materias", materias);
			return listadoCoordinador(principal);
		}

		return mav;
	}
	@RequestMapping("/NuevoExpediente")
	public ModelAndView NuevoExpediente() {
		ModelAndView mav = new ModelAndView();
		Expediente expediente=new Expediente();
		mav.addObject("expediente", expediente);
		mav.setViewName("/Coordinador/AgregarExpediente");
		return mav;
	}



	@RequestMapping(value="/buscarexpediente", method=RequestMethod.POST)
	public ModelAndView filtrar(Principal principal,@RequestParam(value="busqueda") String cadena,@RequestParam Long tipo)
	{
		ModelAndView mav = new ModelAndView();
		List<Expediente> expedientes = null;
		try {
			if (cadena==""){
				return listadoCoordinador(principal);
			}else {
				if (tipo ==1) {
					System.out.println("Nombre = "+cadena);
					expedientes = expedienteService.filtrarPorNombre(cadena);
					promediotodo(expedientes);
					aprobadasreprobadas(expedientes);
				}
				if (tipo ==2) {
					System.out.println("Apellido = "+cadena);
					expedientes = expedienteService.filtrarPorApellido(cadena);
					promediotodo(expedientes);
					aprobadasreprobadas(expedientes);
				}}

		}catch (Exception e) {
			e.printStackTrace();
		}

		mav.addObject("expedientes", expedientes);
		mav.setViewName("/Coordinador/coordinador");

		return mav;
	}
	@RequestMapping(value="/editarexpedienteborrar")
	public ModelAndView editarBorrar(@RequestParam(value="id") String codigo)
	{
		ModelAndView mav = new ModelAndView();
		List<Expediente> expedientes = null;
		try {
			int codigoint = Integer.parseInt(codigo);
			expedienteService.delete(codigoint);

		}catch (Exception e) {
			e.printStackTrace();
		}

		mav.addObject("expedientes", expedientes);
		mav.setViewName("/Coordinador/coordinador");

		return mav;
	}
	@RequestMapping(value="/editarexpedienteeditar")
	public ModelAndView editareditar(@RequestParam(value="id") String codigo)
	{
		ModelAndView mav = new ModelAndView();
		Expediente expediente=null;
		try {
			int codigoint = Integer.parseInt(codigo);
			expediente = expedienteService.filtrarUNO(codigoint);
			mav.addObject("expediente", expediente);
			mav.setViewName("/Coordinador/modificarExpediente");

		}catch (Exception e) {
			e.printStackTrace();
		}


		return mav;
	}

	@RequestMapping("/guardarExpedientemodificado")
	public ModelAndView guardarExpedientemodificado(Principal principal,@Valid @ModelAttribute Expediente expediente, BindingResult result) {

		ModelAndView mav = new ModelAndView();
		List<Expediente> expedientes = null;
		if(result.hasErrors()) {
			mav.setViewName("/Coordinador/modificarExpediente");
			return mav;
		}else{
			try {
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate ahora = LocalDate.now();
				LocalDate fechaNac = LocalDate.parse(expediente.getD_fnacimiento(), fmt);
				System.out.println("Fecha NAch es  "+fechaNac);
				System.out.println("getD_fnacimiento() es  "+expediente.getD_fnacimiento());
				Period periodo = Period.between(fechaNac, ahora);
				if(periodo.getYears()>999){

					expediente.setS_edad(Integer.toString(999));
					expedienteService.insert(expediente);
					expedientes = expedienteService.findAllExpe();
				}else {
					expediente.setS_edad(Integer.toString(periodo.getYears()));
					expedienteService.insert(expediente);
					expedientes = expedienteService.findAllExpe();
				}

			}catch (Exception e) {
				e.printStackTrace();
			}
			return listadoCoordinador(principal);
		}


	}

	@RequestMapping(value="/expediente", method=RequestMethod.POST)
	public ModelAndView mostrarExpediente(Principal principal,@RequestParam(value="id") String codigo)
	{
		ModelAndView mav = new ModelAndView();
		Expediente expediente=null;
		try {
			int codigoint = Integer.parseInt(codigo);
			expediente = expedienteService.filtrarUNO(codigoint);
			mav.addObject("expediente", expediente);
			mav.setViewName("/Coordinador/expediente");

		}catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value="/cursadas", method=RequestMethod.POST)
	public ModelAndView mostrarMaterias(@RequestParam(value="id") Integer codigo)
	{
		System.out.println("Su codigo es :"+codigo);
		ModelAndView mav = new ModelAndView();
		List<AlumnoxMateria> alumnoxMaterias = null;
		AlumnoxMateria alumnoxmateria = new AlumnoxMateria();
		Expediente expediente=null;
		List<Materia> materias = null;
		try {
			//int codigoint = Integer.parseInt(codigo);
			alumnoxMaterias = alumnoxMateriaService.findOneEstudiante(codigo);
			mav.addObject("promedio","Su promedio es: "+df.format(promedio(alumnoxMaterias)));

//			alumnoxMaterias = alumnoxMateriaService.findAll();
			System.out.println("Codigo es :"+codigo);

		}catch (Exception e) {
			e.printStackTrace();
		}
		if (codigo==null){
			mav.setViewName("/Coordinador/expediente");
			return mav;
		}
		mav.addObject("alumnoxmateria", alumnoxmateria);
		expediente = expedienteService.filtrarUNO(codigo);
		materias = materiaService.findAll();
		mav.addObject("materias", materias);
		mav.addObject("expediente", expediente);
		mav.addObject("alumnoxmaterias", alumnoxMaterias);
		mav.setViewName("/Coordinador/materiasCursadas");
		return mav;
	}

	public double promedio(List<AlumnoxMateria> alumnoxMaterias){
		double suma = 0;
		for (int i = 0; i < alumnoxMaterias.size(); i++) {
			suma = suma + alumnoxMaterias.get(i).getNota();
		}
		suma = suma/alumnoxMaterias.size();
		if(alumnoxMaterias.size()==0){
			return 0.0;
		}
		else{
			return suma;
		}
	};
	public void promediotodo(List<Expediente> expedientes){
		expedientes.forEach(e->{
			AtomicReference<Float> sumanotas = new AtomicReference<>((float) 0);
			e.getAlumnoxMaterias().forEach(a-> {
					sumanotas.set((float) (sumanotas.get() + a.getNota()));
			});
			if(sumanotas!=null||e.getAlumnoxMaterias().size()!=0){
				double promedio;
				promedio = sumanotas.get() / e.getAlumnoxMaterias().size();
				if(promedio==Double.NaN){
//					System.out.println("No tienen promedio");
					e.setPromedio(0);
				}else{
//					System.out.println(promedio);
					double roundedDouble = Math.round(promedio * 100.0) / 100.0;
					e.setPromedio(roundedDouble);
				}
			}
		});
	};

	public void aprobadasreprobadas(List<Expediente> expedientes){
		expedientes.forEach(e->{
			AtomicInteger aprobadas= new AtomicInteger();
			AtomicInteger reprobadas= new AtomicInteger();
			e.getAlumnoxMaterias().forEach(a-> {
				if(a.getNota()>=6){
					aprobadas.addAndGet( 1);

				}
				else
				{
					reprobadas.addAndGet(1);
				}
			});
//			System.out.println("Aprobadas: "+aprobadas);
//			System.out.println("Reprobadas: "+reprobadas);
			e.setAprobadas(aprobadas.get());
			e.setReprobadas(reprobadas.get());
		});
	}
}
//https://parzibyte.me/blog/2019/09/02/th-each-thymeleaf-recorrer-listas/

//https://stackoverflow.com/questions/24802681/org-springframework-validation-beanpropertybindingresult-exception