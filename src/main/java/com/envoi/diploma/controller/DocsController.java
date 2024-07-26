package com.envoi.diploma.controller;

import com.envoi.diploma.model.Work;
import com.envoi.diploma.service.GradeService;
import com.envoi.diploma.service.ModelService;
import com.envoi.diploma.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Objects;
import java.util.regex.Matcher;

@Controller
@RequestMapping("/docs/")
public class DocsController
{
    @Autowired
    private WorkService workService;
    @Autowired
    private ModelService modelService;
    private final Path fileStorageLocation = Paths.get("docs").toAbsolutePath().normalize();

    public DocsController()
    {
        try
        {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex)
        {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    @PostMapping("/{idSubject}/{filename:.+}")
    public ResponseEntity<String> uploadFile(
            @PathVariable int idSubject,
            @PathVariable String filename,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "page", required = false) Integer page)
    {
        try
        {
            if (file.isEmpty() || !Objects.equals(file.getContentType(), "application/pdf"))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only PDF files are allowed!");
            }
            Path reference = Paths.get(
                    String.valueOf(idSubject),
                    filename
            ).normalize();
            Path targetPath = fileStorageLocation.resolve(reference).normalize();
            Files.createDirectories(targetPath.getParent());
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            String ref = reference.toString().replaceAll(Matcher.quoteReplacement("\\"), "/");
            Work work = workService.findByReference("/" + ref + "?page=" + page);
            if(Objects.isNull(work))
                work = workService.findByReference("/" + ref);
            work.setReference("/" + ref + "?page=" + modelService.getBestPage(file));
            workService.save(work);

            return ResponseEntity.ok("File uploaded successfully: " + filename);
        } catch (IOException ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not store file. Please try again!");
        }
    }

    @GetMapping("/{idSubject}/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable int idSubject,
            @PathVariable String filename)
    {
        try
        {
            Path filePath = fileStorageLocation
                    .resolve(String.valueOf(idSubject))
                    .resolve(filename)
                    .normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists())
            {
                String encodedFilename = URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename*=UTF-8''" + encodedFilename)
                        .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                        .body(resource);
            } else
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}