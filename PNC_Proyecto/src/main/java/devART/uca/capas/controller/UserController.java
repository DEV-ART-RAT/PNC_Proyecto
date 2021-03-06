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
import devART.uca.capas.domain.Dpto;
import devART.uca.capas.domain.Municipio;
import devART.uca.capas.repositories.UserExpedienteRepository;
import devART.uca.capas.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import devART.uca.capas.utils.EncrytedPasswordUtils;
import devART.uca.capas.utils.WebUtils;

@Controller
public class UserController {
	@Autowired
	ExpedienteServiceImpl expedienteService;

	@Autowired
	MateriaService materiaService;

	@Autowired
	AppUserService userServices;

	@Autowired
	AppRoleService roleServices;

	@Autowired
	UserRoleService userRoleServices;

	@Autowired
	DptoService dptoService;

	@Autowired
	MunicipioService municipioService;

	@Autowired
	UserExpedienteService userExpedienteService;

	@Autowired
	UserExpedienteRepository userRepo;

	@Autowired
	AlumnoxMateriaServiceImpl alumnoxMateriaService;

	private static DecimalFormat df = new DecimalFormat("0.00");


	@RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        return "/Administrador/administrador";
    }


    @RequestMapping(value = {"/login","/"}, method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "loginPage";
    }

    /*
    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Login");
        return "welcomePage";
    }
     */






//    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
//    public String listado(Principal principal) {
//
//        User auth = (User) ((Authentication) principal).getPrincipal();
//        String rol = WebUtils.getRole(auth);
//        //System.out.println(rol);
//
//        if(rol.equals("ROLE_USER")){
//            return "redirect:/userCoordinador";
//        }
//
//        if(rol.equals("ROLE_ADMIN")){
//            return "redirect:/admin";
//        }
//		return "redirect:/";
//    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {

        if (principal != null) {
            User loginedUser = (User) ((Authentication) principal).getPrincipal();

            String userInfo = WebUtils.toString(loginedUser);

            model.addAttribute("userInfo", userInfo);

            String message = "Ingreso No Autorizado <br>" + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);

        }

        return "403Page";
    }

    @RequestMapping("/registrarUsuario")
   	public ModelAndView ingresarUsuario() {
   		ModelAndView mav = new ModelAndView();
   		//System.out.println("aqui estoy registrando :v");
   		List<Dpto> dptos = null;
   		List<Municipio> municipios=null;
   		try {
			dptos = dptoService.findAll();
			municipios = municipioService.findAll();
		}catch (Exception e){
			e.printStackTrace();
		}

   		/*
   		dptos.forEach(e->{
			System.out.println("depta: "+e.getNombre());
			e.getMunicipios().forEach(a->{
				System.out.println("municipio :"+a.getNombre());
			});
				}
		);*/
		//System.out.println(dptos);
		//System.out.println(municipios);
   		mav.addObject("userNew", new AppUser());
		mav.addObject("userNewExp", new UserExpediente());
		mav.addObject("dptos", dptos);
		mav.addObject("municipios",municipios);
   		mav.setViewName("registerPage");
   		mav.addObject("userInfo","registro");
   		return mav;
   	}

    @RequestMapping("/validarRegistrarUsuario")
	public ModelAndView ingresarUsuarioVerificar(@RequestParam("role") Long role,
												 @ModelAttribute("userNew") @Valid AppUser usery,BindingResult result1, @ModelAttribute("userNewExp") @Valid UserExpediente userExp ,BindingResult result ) {
    	ModelAndView mav = new ModelAndView();
		//System.out.println(usery.getUserName() + userExp.getDpto().getNombre());

    	if(result.hasErrors() || result1.hasErrors()) {
			List<Dpto> dptos = null;
			List<Municipio> municipios=null;
			try {
				dptos = dptoService.findAll();
				municipios = municipioService.findAll();
			}catch (Exception e){
				e.printStackTrace();
			}
			mav.addObject("dptos",dptos);
			mav.addObject("municipios",municipios);
			mav.setViewName("registerPage");
		}
		else {
			try {
				UserExpediente userExpRet;
					if(userServices.findOne(usery.getUserName())==null)
					{
						DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDate ahora = LocalDate.now();
						LocalDate fechaNac = LocalDate.parse(userExp.getFnacimiento(), fmt);
						Period periodo = Period.between(fechaNac, ahora);
						if(periodo.getYears()>999){
							userExp.setEdad(Integer.toString(999));
							userExpRet=userRepo.save(userExp);
							//System.out.println(userExpRet.getCodigo());
							userExp.setCodigo(userExpRet.getCodigo());
						}else {
							userExp.setEdad(Integer.toString(periodo.getYears()));
							userExpRet = userRepo.save(userExp);
							//System.out.println(userExpRet.getCodigo());
							userExp.setCodigo(userExpRet.getCodigo());
						}
						userRepo.flush();
						usery.setEnabled(false);
						usery.setEncrytedPassword(EncrytedPasswordUtils.encrytePassword(usery.getEncrytedPassword()));
						usery.setUser(userExp);
						userServices.insert(usery);
						userRoleServices.insert(new UserRole(userServices.findOne(usery.getUserName()),roleServices.findOne(role)));
						userExp.setCodigo(usery.getUserId());
					}else {
						List<Dpto> dptos = null;
						List<Municipio> municipios=null;
						try {
							dptos = dptoService.findAll();
							municipios = municipioService.findAll();
						}catch (Exception e){
							e.printStackTrace();
						}
						mav.addObject("dptos",dptos);
						mav.addObject("municipios",municipios);
						mav.addObject("userNew", new AppUser());
						mav.addObject("message", "Nombre de Usuario ya existe");
						mav.setViewName("registerPage");
						return mav;
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
	   		AppUser appuser = new AppUser();
	   		mav.addObject("userNew", appuser);
	   		mav.addObject("message", "Usuario ingresado!");
			mav.setViewName("loginPage");
		}
		return mav;

   	}

    @RequestMapping("/administarUsuario")
    public ModelAndView administarUsuario(Principal principal) {
        ModelAndView mav = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<AppUser> users = null;
        try {
            users = userServices.findAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        WebUtils.removeMeUser(users,auth.getName());
        mav.addObject("userList", users);
        mav.setViewName("/Administrador/userManager");
        return mav;
    }


	@RequestMapping("/usuarioDesactive")
	public String disableUser(@RequestParam("disable") String name) {
    	AppUser userUpdate= userServices.findOne(name);
		userUpdate.setEnabled(false);
    	userServices.insert(userUpdate);
		return "redirect:/administarUsuario";
	}
	@RequestMapping("/usuarioActive")
	public String enableUser(@RequestParam("enabled") String name) {
		AppUser userUpdate= userServices.findOne(name);
		userUpdate.setEnabled(true);
		userServices.insert(userUpdate);
		return "redirect:/administarUsuario";
	}

	public void promedio(List<Expediente> expedientes){
		expedientes.forEach(e->{
			AtomicReference<Double> sumanotas= new AtomicReference<>((double) 0);
			e.getAlumnoxMaterias().forEach(a-> {
				if(a.getNota()!=0){
					sumanotas.set(sumanotas.get() + a.getNota());
				}
				else{
					float sumatotal;
					sumatotal = (float) 0.0;
				}
			});
			if(sumanotas.get() !=null||e.getAlumnoxMaterias().size()!=0){
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


//https://stackoverflow.com/questions/24802681/org-springframework-validation-beanpropertybindingresult-exception