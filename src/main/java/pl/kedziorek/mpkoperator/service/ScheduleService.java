package pl.kedziorek.mpkoperator.service;

import org.springframework.web.multipart.MultipartFile;
import pl.kedziorek.mpkoperator.domain.Schedule;

import java.io.IOException;

public interface ScheduleService {
    Schedule saveDispatcherSchedule(MultipartFile multipartFile) throws IOException;
}
