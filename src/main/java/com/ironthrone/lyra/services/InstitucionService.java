package com.ironthrone.lyra.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ironthrone.lyra.contracts.InstitucionRequest;
import com.ironthrone.lyra.ejb.Alumno;
import com.ironthrone.lyra.ejb.Bitacora;
import com.ironthrone.lyra.ejb.Grado;
import com.ironthrone.lyra.ejb.Institucion;
import com.ironthrone.lyra.ejb.Materia;
import com.ironthrone.lyra.ejb.Subscripcion;
import com.ironthrone.lyra.ejb.Usuario;
import com.ironthrone.lyra.pojo.AlumnoPOJO;
import com.ironthrone.lyra.pojo.InstitucionPOJO;
import com.ironthrone.lyra.pojo.SubscripcionPOJO;
import com.ironthrone.lyra.pojo.UsuarioPOJO;
import com.ironthrone.lyra.repositories.InstitucionRepository;

/**
 * Clase de tipo service, manejo de las instituciones y interacción con los repositorios correspondientes 
 * @author Randall
 *
 */
@Service
public class InstitucionService implements InstitucionServiceInterface{

	@Autowired private InstitucionRepository institucionRepository;
	
	/**
	 * Genera POJOs a partir de una lista EJB.
	 * @param List<Institucion> representa una lista de instituciones tipo ejb
	 * @return List<InstitucionPOJO>
	 */
	private List<InstitucionPOJO> generateInstitucionDtos(List<Institucion> instituciones){
		
		List<InstitucionPOJO> institucionesPojo = new ArrayList<InstitucionPOJO>();
		
		instituciones.stream().forEach(i -> {
			InstitucionPOJO dto = new InstitucionPOJO();
			BeanUtils.copyProperties(i,dto);
			dto.setHasSuscripcion(i.getHasSuscripcion());
			dto.setAlumnos(null);
			dto.setBitacoras(null);
			dto.setGrados(null);
			dto.setMaterias(null);
			dto.setSubscripcions(null);
			dto.setUsuarios(null);
			institucionesPojo.add(dto);
		});	
		
		return institucionesPojo;
	};

	/**
	 * Retorna una lista de instituciones.
	 * @return lista de instituciones POJO
	 */
	@Override
	@Transactional
	public List<InstitucionPOJO> getAll() {
		List<Institucion> instituciones = institucionRepository.findAll();
		return generateInstitucionDtos(instituciones);
	}

	/**
	 * Guarda los datos de una Institución.
	 * @param InstitucionRequest de la capa frontend.
	 * @return booleano, true = success, false = error.
	 */
	@Override
	@Transactional
	public Boolean saveInstitucion(InstitucionRequest institucionRequest) {
		
		Institucion institucion = new Institucion();
		Institucion ninstitucionT = null;
		BeanUtils.copyProperties(institucionRequest.getInstitucion(), institucion);
		
		institucion.setAlumnos(new ArrayList<Alumno>());
		institucion.setBitacoras(new ArrayList<Bitacora>());
		institucion.setGrados(new ArrayList<Grado>());
		institucion.setMaterias(new ArrayList<Materia>());
		institucion.setSubscripcions(new ArrayList<Subscripcion>());
		institucion.setUsuarios(new ArrayList<Usuario>());
		
		if(institucionRequest.getInstitucion().getIdInstitucion() <= -1){		
			
			institucion.setHasSuscripcion(true);
			ninstitucionT = institucionRepository.save(institucion);
			
		}else{		
			Institucion oldInstitucion = findById(institucion.getIdInstitucion());
			
			oldInstitucion.setHasSuscripcion(institucion.getHasSuscripcion());
			oldInstitucion.setLogoInstitucion(institucion.getLogoInstitucion());
			oldInstitucion.setNombreInstitucion(institucion.getNombreInstitucion());
			oldInstitucion.setAlumnos(institucion.getAlumnos());
			oldInstitucion.setBitacoras(institucion.getBitacoras());
			oldInstitucion.setGrados(institucion.getGrados());
			oldInstitucion.setMaterias(institucion.getMaterias());
			oldInstitucion.setSubscripcions(institucion.getSubscripcions());
			oldInstitucion.setUsuarios(institucion.getUsuarios());
			
			ninstitucionT = institucionRepository.save(oldInstitucion);	
		}

		return (ninstitucionT == null) ? false : true;
		
	}

	/**
	 * Retorna los detalles de una institución.
	 * @param idInstitucion, identificador único de la institución.
	 * @return Institución de tipo InstitucionPOJO.
	 */
	@Override
	@Transactional
	public InstitucionPOJO getInstitucionById(int idInstitucion) {
		
		Institucion institucion =  institucionRepository.findOne(idInstitucion);
		InstitucionPOJO dto = new InstitucionPOJO();
		BeanUtils.copyProperties(institucion,dto);
		dto.setHasSuscripcion(institucion.getHasSuscripcion());
		dto.setAlumnos(null);
		dto.setBitacoras(null);
		dto.setGrados(null);
		dto.setMaterias(null);
		dto.setSubscripcions(null);
		dto.setUsuarios(null);
		
		return dto;
	}
	
	/**
	 * Retorna una única institución por ID.
	 * @param idInstitucion, identificador único de la institución.
	 * @return Usuario de tipo UsuarioEJB.
	 */
	@Override
	@Transactional
	public Institucion findById(int idInstitucion) {	
		return institucionRepository.findOne(idInstitucion);
	}
	
	/**
	 * Retorna la institucion con sus alumnos.
	 * @param idInstitucion, identificador único de la institución.
	 * @return Institución de tipo InstitucionPOJO.
	 */
	@Override
	@Transactional
	public InstitucionPOJO getAlumnosDeInstitucionById(int idInstitucion) {
		
		Institucion institucion =  institucionRepository.findOne(idInstitucion);
		InstitucionPOJO dto = new InstitucionPOJO();
		BeanUtils.copyProperties(institucion,dto);
		dto.setHasSuscripcion(institucion.getHasSuscripcion());
		dto.setAlumnos(generateAlumnoDto(institucion));
		dto.setBitacoras(null);
		dto.setGrados(null);
		dto.setMaterias(null);
		dto.setSubscripcions(null);
		dto.setUsuarios(null);
		
		return dto;
	}
	
	/**
	 * Retorna la institucion con sus alumnos.
	 * @param idInstitucion, identificador único de la institución.
	 * @return Institución de tipo InstitucionPOJO.
	 */
	@Override
	@Transactional
	public InstitucionPOJO getUsuariosDeInstitucionById(int idInstitucion) {
		
		Institucion institucion =  institucionRepository.findOne(idInstitucion);
		InstitucionPOJO dto = new InstitucionPOJO();
		BeanUtils.copyProperties(institucion,dto);
		dto.setHasSuscripcion(institucion.getHasSuscripcion());
		dto.setAlumnos(null);
		dto.setBitacoras(null);
		dto.setGrados(null);
		dto.setMaterias(null);
		dto.setSubscripcions(null);
		dto.setUsuarios(generateUserDto(institucion));
		
		return dto;
	}
	
	/**
	 * Retorna una lista de Usuarios POJO de una institución
	 * @param Institucion recibe un objeto Institución
	 * @return List<UsuarioPOJO> Lista de usuario de tipo POJO
	 */
	private List<UsuarioPOJO> generateUserDto(Institucion i) {
		
		List<UsuarioPOJO> users = new ArrayList<UsuarioPOJO>();
		
		i.getUsuarios().stream().forEach(u -> {
			UsuarioPOJO user = new UsuarioPOJO();
			BeanUtils.copyProperties(u, user);	
			user.setPassword("secret");
			user.setActiveUs(u.getIsActiveUs());
			user.setRols(null);
			user.setInstitucion(null);
			
			users.add(user);
		});	

		return users;
	};
	
	/**
	 * Genera POJOs a partir de una lista EJB.
	 * @param users representa una lista de subscripciones tipo ejb
	 * @return Lista de Subscripcion POJO.
	 */
	@SuppressWarnings("unused")
	private List<SubscripcionPOJO> generateSubscripcionDtos(Institucion i){
		
		List<SubscripcionPOJO> uiSubscripciones = new ArrayList<SubscripcionPOJO>();
		
		i.getSubscripcions().stream().forEach(s -> {
			SubscripcionPOJO dto = new SubscripcionPOJO();
			BeanUtils.copyProperties(s,dto);
			dto.setActiveSub(s.getIsActiveSub());
			dto.setInstitucion(null);
			dto.setFechaFin(null);
			dto.setFechaInicio(null);
			uiSubscripciones.add(dto);
		});	
		
		return uiSubscripciones;
	};
	
	/**
	 * Obtiene una lista de AlumnoPojo de una institucion
	 * @param Institucion
	 * @return List<AlumnoPOJO>
	 */
	private List<AlumnoPOJO> generateAlumnoDto(Institucion i) {
		
		List<AlumnoPOJO> alumnos = new ArrayList<AlumnoPOJO>();
		
		i.getAlumnos().stream().forEach(a -> {
			AlumnoPOJO alumno = new AlumnoPOJO();
			BeanUtils.copyProperties(a, alumno);	
			alumno.setRegistrosMedicos(null);
			alumno.setSeccion(null);
			alumno.setUsuarios(generateUserDto(a));
			alumno.setActiveAl(a.getIsActiveAl());
			alumnos.add(alumno);
		});	

		return alumnos;
	};
	
	/**
	 * Retorna una lista de Usuarios POJO de un Alumno
	 * @param Institucion recibe un objeto Alumno
	 * @return List<UsuarioPOJO> Lista de usuario de tipo POJO
	 */
	private List<UsuarioPOJO> generateUserDto(Alumno a) {
		
		List<UsuarioPOJO> users = new ArrayList<UsuarioPOJO>();
		
		a.getUsuarios().stream().forEach(u -> {
			UsuarioPOJO user = new UsuarioPOJO();
			BeanUtils.copyProperties(u, user);	
			user.setPassword("secret");
			user.setActiveUs(u.getIsActiveUs());
			user.setRols(null);
			user.setInstitucion(null);
			
			users.add(user);
		});	

		return users;
	};
}
