package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.Schedule;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;
import pl.kedziorek.mpkoperator.domain.enums.ComplaintStatus;
import pl.kedziorek.mpkoperator.domain.enums.ScheduleName;
import pl.kedziorek.mpkoperator.repository.ScheduleRepository;
import pl.kedziorek.mpkoperator.service.ScheduleService;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.kedziorek.mpkoperator.utils.LocalDateConverter.convertToLocalDate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Value("${dispatcherSchedules.dir}")
    private String dispatcherSchedulesDir;

    @Value("${driverSchedules.dir}")
    private String driverSchedulesDir;

    @Value("${mechanicSchedules.dir}")
    private String mechanicSchedulesDir;

    @Value("${schedules.dir}")
    private String schedulesDir;

    @Override
    public Schedule saveDispatcherSchedule(MultipartFile multipartFile) throws IOException {
        log.info("Saving new dispatcher schedule to the database");

        Schedule schedule = new Schedule();

        setStaticPropertiesOfSchedule(schedule);

        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        schedule.setName(ScheduleName.DISPATCHER_SCHEDULE + "_" + schedule.getDate() + "." + extension);

        Path path = Paths.get(dispatcherSchedulesDir + File.separator + schedule.getUuid() + "." + extension);

        schedule.setScheduleDir(dispatcherSchedulesDir + "/" + schedule.getUuid() + "." + extension);

        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule saveDriverSchedule(MultipartFile multipartFile) throws IOException {
        log.info("Saving new driver schedule to the database");

        Schedule schedule = new Schedule();

        setStaticPropertiesOfSchedule(schedule);

        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        schedule.setName(ScheduleName.DRIVER_SCHEDULE + "_" + schedule.getDate() + "." + extension);

        Path path = Paths.get(driverSchedulesDir + File.separator + schedule.getUuid() + "." + extension);

        schedule.setScheduleDir(driverSchedulesDir + "/" + schedule.getUuid() + "." + extension);

        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule saveMechanicSchedule(MultipartFile multipartFile) throws IOException {
        log.info("Saving new mechanic schedule to the database");

        Schedule schedule = new Schedule();

        setStaticPropertiesOfSchedule(schedule);

        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        schedule.setName(ScheduleName.MECHANIC_SCHEDULE + "_" + schedule.getDate() + "." + extension);

        Path path = Paths.get(mechanicSchedulesDir + File.separator + schedule.getUuid() + "." + extension);

        schedule.setScheduleDir(mechanicSchedulesDir + "/" + schedule.getUuid() + "." + extension);

        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return scheduleRepository.save(schedule);
    }

    private void setStaticPropertiesOfSchedule(Schedule schedule) {
        schedule.setUuid(UUID.randomUUID());
        schedule.setDate(LocalDateTime.now());
        schedule.setCreatedAt(LocalDateTime.now());
        schedule.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public List<Schedule> getSchedulesByName(String name) {
        return scheduleRepository.findSchedulesByNameOrderByDateDesc(name)
                .orElseThrow(() -> new ResourceNotFoundException("Schedules not found in database"));
    }

    @Override
    public DataResponse<Schedule> getSchedules(Map<String, String> params, int page, int size) {
        Page<Schedule> pageSchedule = scheduleRepository.findAllParamsByName(
                params.get("name") == null ? "" : params.get("name").toUpperCase(),
                PageRequest.of(page, size)
        );

        return DataResponse.<Schedule>builder()
                .data(pageSchedule.get().collect(Collectors.toList()))
                .page(pageSchedule.getTotalPages())
                .size(pageSchedule.getTotalElements())
                .build();
    }

    @Override
    public byte[] getSchedule(UUID uuid) throws IOException {
        Schedule schedule = scheduleRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found in the database"));

        String extension = schedule.getScheduleDir().subSequence(
                schedule.getScheduleDir().indexOf('.'),
                schedule.getScheduleDir().length()
        ).toString();

        File file = getFileFromResources(uuid, schedule, extension);
        return FileUtils.readFileToByteArray(file);
    }

    @Override
    public Schedule editSchedule(MultipartFile multipartFile, UUID uuid) throws IOException {
        Schedule schedule = scheduleRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found in database"));

        String nameStart;
        String constPath = "src/main/resources/static";
        String flexiblePath;

        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        if (schedule.getName().contains("DISPATCHER_SCHEDULE")) {
            nameStart = "DISPATCHER_SCHEDULE";
            flexiblePath = "/dispatcherSchedules";
        } else if (schedule.getName().contains("DRIVER_SCHEDULE")) {
            nameStart = "DRIVER_SCHEDULE";
            flexiblePath = "/driverSchedules";
        } else {
            nameStart = "MECHANIC_SCHEDULE";
            flexiblePath = "/mechanicSchedules";
        }

        String fullPath = constPath + flexiblePath;
        schedule.setName(nameStart + "_" + schedule.getDate() + "." + extension);
        schedule.setScheduleDir(fullPath + "/" + schedule.getUuid() + "." + extension);

        Path path = Paths.get( fullPath + File.separator + schedule.getUuid() + "." + extension);
        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return scheduleRepository.save(schedule);
    }

    private String getExtensionFromString(String s) {
        return s.subSequence(s.indexOf('.'), s.length()).toString();
    }

    @Override
    public Schedule deleteSchedule(UUID uuid) throws IOException {
        Schedule schedule = scheduleRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule with uuid: {uuid} not found in database"));

        String extension = getExtensionFromString(schedule.getScheduleDir());
        File file = getFileFromResources(uuid, schedule, extension);

        FileUtils.forceDelete(file);
        scheduleRepository.delete(schedule);
        return schedule;
    }

    private File getFileFromResources(UUID uuid, Schedule schedule, String extension) {
        String path;

        if (schedule.getName().contains("DISPATCHER_SCHEDULE")) {
            path = "/dispatcherSchedules/";
        } else if (schedule.getName().contains("DRIVER_SCHEDULE")) {
            path = "/driverSchedules/";
        } else {
            path = "/mechanicSchedules/";
        }

        return new File(schedulesDir + path + uuid.toString() + extension);
    }
}
