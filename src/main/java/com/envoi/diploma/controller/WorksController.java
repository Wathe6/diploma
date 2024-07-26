package com.envoi.diploma.controller;

import com.envoi.diploma.model.Grade;
import com.envoi.diploma.service.GradeService;
import com.envoi.diploma.types.enums.statuses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.regex.Matcher;

@Controller
@RequestMapping("/works/")
public class WorksController
{
    @Autowired
    private GradeService gradeService;
    private final Path fileStorageLocation = Paths.get("works").toAbsolutePath().normalize();

    public WorksController()
    {
        try
        {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex)
        {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @PostMapping("/{idStudent}/{idSubject}/{filename:.+}")
    public ResponseEntity<String> uploadFile(
            @PathVariable int idStudent,
            @PathVariable int idSubject,
            @PathVariable String filename,
            @RequestParam("file") MultipartFile file)
    {
        try
        {
            if (file.isEmpty() || !Objects.equals(file.getContentType(), "application/pdf"))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only PDF files are allowed!");
            }

            Path reference = Paths.get(
                    String.valueOf(idStudent),
                    String.valueOf(idSubject),
                    filename
            ).normalize();
            Path targetPath = fileStorageLocation.resolve(reference).normalize();
            Files.createDirectories(targetPath.getParent());
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String ref = reference.toString().replaceAll(Matcher.quoteReplacement("\\"), "/");
            Grade grade = gradeService.findByReference("/" + ref);
            grade.setStatuses(statuses.WAITING_FOR_CHECK);
            gradeService.save(grade);

            return ResponseEntity.ok("File uploaded successfully: " + filename);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error processing the request: " + e.getMessage());
        }
    }
    @GetMapping("/{idStudent}/{idSubject}/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable int idStudent,
            @PathVariable int idSubject,
            @PathVariable String filename
            )
    {
        try
        {
            Path filePath = fileStorageLocation
                    .resolve(String.valueOf(idStudent))
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
