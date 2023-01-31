package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
