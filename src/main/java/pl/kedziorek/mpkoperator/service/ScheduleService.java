package pl.kedziorek.mpkoperator.service;

import org.springframework.web.multipart.MultipartFile;
import pl.kedziorek.mpkoperator.domain.Schedule;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ScheduleService {
    Schedule saveDispatcherSchedule(MultipartFile multipartFile) throws IOException;
    Schedule saveMechanicSchedule(MultipartFile multipartFile) throws IOException;
    Schedule saveDriverSchedule(MultipartFile multipartFile) throws IOException;
    Schedule editSchedule(MultipartFile multipartFile, UUID uuid) throws IOException;
    byte[] getSchedule(UUID uuid) throws IOException;
    List<Schedule> getSchedulesByName(String name);
    Schedule deleteSchedule(UUID uuid) throws IOException;
    DataResponse<Schedule> getSchedules(Map<String, String> params, int page, int size);
}
