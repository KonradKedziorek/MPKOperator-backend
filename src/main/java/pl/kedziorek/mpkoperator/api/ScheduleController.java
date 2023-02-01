package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kedziorek.mpkoperator.service.ScheduleService;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;

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

    @GetMapping("/schedules={name}")
    public ResponseEntity<?> getSchedules(@PathVariable String name) {
        return ResponseEntity.ok().body(scheduleService.getSchedulesByName(name));
    }
}
