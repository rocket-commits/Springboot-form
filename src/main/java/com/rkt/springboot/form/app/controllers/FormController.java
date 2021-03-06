package com.rkt.springboot.form.app.controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import java.util.HashMap;
//import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.rkt.springboot.form.app.editors.NombreMayusculaEditor;
import com.rkt.springboot.form.app.editors.PaisPropertyEditor;
import com.rkt.springboot.form.app.models.domain.Pais;
import com.rkt.springboot.form.app.models.domain.Usuario;
import com.rkt.springboot.form.app.services.PaisService;
import com.rkt.springboot.form.app.validation.UsuarioValidador;

@Controller
@SessionAttributes("usuarioclass")
public class FormController {

	@Autowired
	private UsuarioValidador validador;

	@Autowired
	private PaisService paisService;
	
	@Autowired
	private PaisPropertyEditor paisEditor;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(validador);
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		dateformat.setLenient(false);
		binder.registerCustomEditor(Date.class, "fechaNacimiento", new CustomDateEditor(dateformat, true));

		binder.registerCustomEditor(String.class, "nombre", new NombreMayusculaEditor());
		binder.registerCustomEditor(String.class, "apellido", new NombreMayusculaEditor());

		binder.registerCustomEditor(Pais.class, "pais", paisEditor);
	}

	@GetMapping("/form")
	public String form(Model model) {
		Usuario usuario = new Usuario();
		usuario.setNombre("Son");
		usuario.setApellido("Goku");
		usuario.setIdentificador("12.456.789-Z");
		model.addAttribute("titulo", "Formulario usuarios");
		model.addAttribute("usuarioclass", usuario);
		return "form";
	}

	@ModelAttribute("paises")
	public List<String> paises() {

		return Arrays.asList("Espa??a", "M??xico", "Chile", "Argentina", "Per??", "Colombia", "Venezuela");
	}

	@ModelAttribute("paisesMap")
	public Map<String, String> paisesMap() {

		Map<String, String> paises = new HashMap<String, String>();
		paises.put("ES", "Espa??a");
		paises.put("MX", "M??xico");
		paises.put("CL", "Chile");
		paises.put("AR", "Argentina");
		paises.put("PE", "Per??");
		paises.put("CO", "Colombia");
		paises.put("VE", "Venezuela");
		return paises;
	}

	@ModelAttribute("listaPaises")
	public List<Pais> listaPaises() {
		return paisService.listar();
	}

	@PostMapping("/form")
	public String procesar(@Valid @ModelAttribute("usuarioclass") Usuario usuario, BindingResult result, Model model,
			SessionStatus status
	/*
	 * Cuando los atributos coinciden con los parametros se poblaran de forma
	 * automatica en la clase, es importante tener para esto los getters/setters
	 * 
	 * ,@RequestParam(name = "username") String username,
	 * 
	 * @RequestParam String password,
	 * 
	 * @RequestParam String email
	 *
	 */

	) {

		/*
		 * Forma usando el constructor de la clase y haciendo uso de RequestParam
		 * 
		 * Usuario usuario = new Usuario(); usuario.setUsername(username);
		 * usuario.setPassword(password); usuario.setEmail(email);
		 */

//		validador.validate(usuario, result);

		model.addAttribute("titulo", "Resultado form");

		if (result.hasErrors()) {
//			Map<String, String> errores = new HashMap<>();
//			result.getFieldErrors().forEach(err ->{
//				errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
//				});
//			model.addAttribute("error", errores);
			return "form";
		}

		model.addAttribute("user", usuario);
		status.setComplete();
		return "resultado";
	}
}
