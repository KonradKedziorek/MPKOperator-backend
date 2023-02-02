package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Schedule;
import pl.kedziorek.mpkoperator.repository.ScheduleRepository;
import pl.kedziorek.mpkoperator.service.ScheduleService;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ScheduleRepository scheduleRepository;


    @PostMapping("/dispatcherSchedule/save")
    public ResponseEntity<?> saveDispatcherSchedule(
            @RequestParam(value = "dispatcherSchedule") MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok().body(scheduleService.saveDispatcherSchedule(multipartFile));
    }

    @PostMapping("/driverSchedule/save")
    public ResponseEntity<?> saveDriverSchedule(
            @RequestParam(value = "driverSchedule") MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok().body(scheduleService.saveDriverSchedule(multipartFile));
    }

    @PostMapping("/mechanicSchedule/save")
    public ResponseEntity<?> saveMechanicSchedule(
            @RequestParam(value = "mechanicSchedule") MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok().body(scheduleService.saveMechanicSchedule(multipartFile));
    }

    @GetMapping("/schedules/name={name}")
    public ResponseEntity<?> getSchedules(@PathVariable String name) {
        return ResponseEntity.ok().body(scheduleService.getSchedulesByName(name));
    }

    @GetMapping(
            value = "/schedule/uuid={uuid}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getSchedules(@PathVariable UUID uuid) throws IOException {
        Schedule schedule = scheduleRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found in the database"));

        String extension = schedule.getScheduleDir().subSequence(
                schedule.getScheduleDir().indexOf('.'),
                schedule.getScheduleDir().length()
        ).toString();

        InputStream in = getClass()
                .getResourceAsStream("/static/dispatcherSchedules/" + uuid.toString() + extension);
        return IOUtils.toByteArray(in);
    }
}
