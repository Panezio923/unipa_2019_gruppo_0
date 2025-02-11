package it.eng.unipa.filesharing.container;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

import com.itextpdf.text.DocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import it.eng.unipa.filesharing.dto.FolderDTO;
import it.eng.unipa.filesharing.dto.ResourceDTO;
import it.eng.unipa.filesharing.service.TeamService;

@RestController
@RequestMapping("/resource")
public class ResourceController {
	
	@Autowired
	TeamService teamService;
	
	@GetMapping("/{uuid}/{bucketName}")
	@ResponseStatus(value = HttpStatus.OK)
	public ResourceDTO tree(@PathVariable("uuid") UUID uuid,@PathVariable("bucketName") String bucketName){
		return teamService.tree(uuid,bucketName);
	}
	
	
	@PostMapping("/addFolder/{uuid}/{bucketName}")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void addFolder(@PathVariable("uuid") UUID uuid,@PathVariable("bucketName") String bucketName,@RequestBody FolderDTO folderDTO) {
		teamService.addFolder(uuid, bucketName, folderDTO.getParentUniqueId(), folderDTO.getName());
	}
	
	@PostMapping("/addContent/{uuid}/{bucketName}")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void addContent(@PathVariable("uuid") UUID uuid,@PathVariable("bucketName") String bucketName,@RequestParam(value = "parentUniqueId", required = false)String parentUniqueId,@RequestParam("file") MultipartFile multipartFile) throws IOException {
		teamService.addContent(uuid, bucketName, parentUniqueId, multipartFile.getOriginalFilename(), multipartFile.getBytes());
	}
	
	
	@GetMapping("/{uuid}/{bucketName}/{uniqueId}")
	public ResponseEntity<Resource> download(@PathVariable("uuid") UUID uuid,@PathVariable("bucketName") String bucketName,@PathVariable("uniqueId") String uniqueId) {
		ResourceDTO resourceDTO = teamService.getContent(uuid,bucketName,uniqueId);
		return getResponseEntityResource(resourceDTO.getName(), resourceDTO.getContent());
	}

	@GetMapping("/preview/{uuid}/{bucketName}/{uniqueId}")
	public ResponseEntity<Resource> preview(@PathVariable("uuid") UUID uuid,@PathVariable("bucketName") String bucketName,@PathVariable("uniqueId") String uniqueId) throws IOException, InvalidFormatException, DocumentException, com.lowagie.text.DocumentException {
		byte[] resource = teamService.getPreviewContent(uuid,bucketName,uniqueId);
		return getResponseEntityResource("", resource);
	}
	
	
	private ResponseEntity<Resource> getResponseEntityResource(String name, byte[] body) {
		return ResponseEntity.ok()
                //.contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
                .body(new InputStreamResource(new ByteArrayInputStream(body)));
	}
}
