package devART.uca.capas.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import devART.uca.capas.domain.AppUser;
import devART.uca.capas.domain.Expediente;
import devART.uca.capas.domain.Materia;
import devART.uca.capas.domain.UserRole;
import devART.uca.capas.service.AppRoleService;
import devART.uca.capas.service.AppUserService;
import devART.uca.capas.service.ExpedienteServiceImpl;
import devART.uca.capas.service.MateriaService;
import devART.uca.capas.service.UserDetailsServiceImpl;
import devART.uca.capas.service.UserRoleService;
import devART.uca.capas.utils.EncrytedPasswordUtils;
import devART.uca.capas.utils.WebUtils;
 
@Controller
public class CoordinadorController {
	@Autowired
	ExpedienteServiceImpl expedienteService;
    //cordinador
    
    @RequestMapping("/guardarExpediente")
	public ModelAndView guardarExpediente(@Valid @ModelAttribute Expediente expediente, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();
		if(!result.hasErrors()) {
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
				}else {
					expediente.setS_edad(Integer.toString(periodo.getYears()));
					expedienteService.insert(expediente);
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			expediente = new Expediente();
			mav.addObject("expediente", expediente); 
			mav.addObject("message", "Estudiante Guardado!");
		}

		mav.setViewName("/Coordinador/AgregarExpediente");
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
	public ModelAndView filtrar(@RequestParam(value="busqueda") String cadena,@RequestParam Long tipo) 
	{	
		ModelAndView mav = new ModelAndView();
		List<Expediente> expedientes = null;
		try {
			if (cadena==""){
				expedientes = expedienteService.findAllExpe();
			}else {
			if (tipo ==1) {
				System.out.println("Nombre = "+cadena);
				expedientes = expedienteService.filtrarPorNombre(cadena);

			}
			if (tipo ==2) {
				System.out.println("Apellido = "+cadena);
				expedientes = expedienteService.filtrarPorApellido(cadena);

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
	public ModelAndView guardarExpedientemodificado(@Valid @ModelAttribute Expediente expediente, BindingResult result) {

		ModelAndView mav = new ModelAndView();
		List<Expediente> expedientes = null;
		if(!result.hasErrors()) {
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

			expediente = new Expediente();
			mav.addObject("expedientes", expedientes);
			mav.addObject("message", "Estudiante Modificado!");
		}

		mav.setViewName("/Coordinador/coordinador");
		return mav;
	}
 
}
//https://parzibyte.me/blog/2019/09/02/th-each-thymeleaf-recorrer-listas/

//https://stackoverflow.com/questions/24802681/org-springframework-validation-beanpropertybindingresult-exception