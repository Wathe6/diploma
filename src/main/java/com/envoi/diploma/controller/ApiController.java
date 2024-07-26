package com.envoi.diploma.controller;

import com.envoi.diploma.model.Work;
import com.envoi.diploma.service.*;
import com.envoi.diploma.types.enums.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
public class ApiController
{
    private Map<String, CRUDService<?, ?>> serviceMap;
    private Map<String, AbstractENUM[]> enumMap;
    @Autowired
    public ApiController(FieldOfStudyService fieldOfStudyService,
                         ExtendedGroupService extendedGroupService,
                         EducationalProgramService educationalProgramService,
                         SubjectService subjectService,
                         TeacherService teacherService,
                         TeachSubjService teachSubjService,
                         StudentService studentService,
                         GroupService groupService,
                         WorkService workService,
                         GradeService gradeService,
                         UserService userService,
                         Map<String, AbstractENUM> enumMap)
    {
        this.serviceMap = new HashMap<>();
        this.serviceMap.put("fields-of-study", fieldOfStudyService);
        this.serviceMap.put("extended-groups", extendedGroupService);
        this.serviceMap.put("educational-groups", educationalProgramService);
        this.serviceMap.put("subjects", subjectService);
        this.serviceMap.put("teachers", teacherService);
        this.serviceMap.put("teach-subj", teachSubjService);
        this.serviceMap.put("students", studentService);
        this.serviceMap.put("groups", groupService);
        this.serviceMap.put("works", workService);
        this.serviceMap.put("grades", gradeService);
        this.serviceMap.put("users", userService);
        this.enumMap = Map.of(
                "academicdegrees", academicdegrees.values(),
                "academictitles", academictitles.values(),
                "departments", departments.values(),
                "positions", positions.values(),
                "reviewers", reviewers.values(),
                "roles", roles.values(),
                "statuss", statuses.values(),
                "subjtypes", subjtypes.values(),
                "worktypes", worktypes.values()
        );
    }

    @GetMapping("enums/{enumType}")
    public List<?> getAllEnums(@PathVariable String enumType)
    {
        if(enumType.equals("all"))
        {
            return List.of(enumMap);
        }
        else
            return List.of(enumMap.get(enumType));
    }
    @GetMapping("/{entityType}")
    public List<?> getAllEntities(@PathVariable String entityType)
    {
        CRUDService<?, ?> service = serviceMap.get(entityType);
        if (service == null)
        {
            throw new IllegalArgumentException("Invalid entity type: " + entityType);
        }
        return service.findAll();
    }

    @PostMapping("/{entityType}")
    public <T, V> List<T> createEntity(@PathVariable String entityType, @RequestBody List<Map<String, Object>> entitiesData)
    {
        try
        {
            CRUDService<T, V> service = (CRUDService<T, V>) serviceMap.get(entityType);
            List<T> entities = new ArrayList<>();
            for (Map<String, Object> data : entitiesData)
            {
                entities.add(service.create(data));
            }
            return service.saveAll(entities);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error processing the request: " + e.getMessage());
        }
    }
    @DeleteMapping("/works")
    public <T, V> void deleteWork(@RequestBody List<Map<String, Object>> entitiesData) {
        try {
            WorkService workService = (WorkService) serviceMap.get("works");
            GradeService gradeService = (GradeService) serviceMap.get("grades");
            List<Work> entities = new ArrayList<>();
            for (Map<String, Object> data : entitiesData) {
                entities.add(workService.create(data));
            }
            for(Work w : entities)
            {
                gradeService.deleteByWork(w);
            }
            workService.deleteAll(entities);
        } catch (Exception e) {
            throw new RuntimeException("Error processing the request: " + e.getMessage());
        }
    }

    @DeleteMapping("/{entityType}")
    public <T, V> void deleteEntity(@PathVariable String entityType, @RequestBody List<Map<String, Object>> entitiesData) {
        try {
            CRUDService<T, V> service = (CRUDService<T, V>) serviceMap.get(entityType);
            List<T> entities = new ArrayList<>();
            for (Map<String, Object> data : entitiesData) {
                entities.add(service.create(data));
            }
            service.deleteAll(entities);
        } catch (Exception e) {
            throw new RuntimeException("Error processing the request: " + e.getMessage());
        }
    }
}