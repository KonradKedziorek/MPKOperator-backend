package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Value("${schedules.dir}")
    private String schedulesDir;

    @Override
    public Schedule saveDispatcherSchedule(MultipartFile multipartFile) throws IOException {
        log.info("Saving new schedule to the database");

        Schedule schedule = new Schedule();
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        schedule.setUuid(UUID.randomUUID());
        schedule.setDate(LocalDateTime.now());
        schedule.setName(ScheduleName.DISPATCHER_SCHEDULE + "_" + schedule.getDate() + "." + extension);
        schedule.setScheduleDir(schedulesDir);
        schedule.setCreatedAt(LocalDateTime.now());
        schedule.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        Path path = Paths.get(schedulesDir + File.separator + schedule.getUuid() + "." + extension);
        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return scheduleRepository.save(schedule);
    }
}
