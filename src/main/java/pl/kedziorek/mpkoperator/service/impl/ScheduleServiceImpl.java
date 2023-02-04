package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Schedule;
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
import java.util.UUID;

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
}
