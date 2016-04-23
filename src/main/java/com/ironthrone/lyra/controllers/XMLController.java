package com.ironthrone.lyra.controllers;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ironthrone.lyra.contracts.XMLResponse;
import com.ironthrone.lyra.services.XMLServiceInterface;
import com.ironthrone.lyra.utils.Utils;



/**
 * Handles requests for the application's bulk upload.
 * @author jeanpaul
 */

@RestController
@RequestMapping(value ="rest/protected/xml")
public class XMLController {

	@Autowired private ServletContext servletContext;
	@Autowired private XMLServiceInterface xmlService;
	
	/**
	 * Retorna una lista de todos los usuarios del sistema
	 * @param ur
	 * @return Usuario Response
	 */
	@RequestMapping(value ="/bulkUpload", method = RequestMethod.POST)
	public XMLResponse bulkUpload(@RequestParam("idInstitucion") int idInstitucion,
							  	  @RequestParam("file") MultipartFile file){	
			
		XMLResponse response = new XMLResponse();
		String resultFileName = Utils.writeToFile(file,servletContext);

		
		if(file != null){
			response = xmlService.bulkUpload(idInstitucion,resultFileName);
		}else{
			response.setCode(404);
			response.setCodeMessage("El archivo de excel no es valido, por favor reviselo.");
		}
		
		
		return response;
	}
	
}
