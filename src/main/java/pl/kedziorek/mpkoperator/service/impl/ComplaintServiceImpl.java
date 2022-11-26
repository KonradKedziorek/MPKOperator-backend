package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.ComplaintHistory;
import pl.kedziorek.mpkoperator.domain.dto.ComplaintRequest;
import pl.kedziorek.mpkoperator.domain.dto.DataResponse;
import pl.kedziorek.mpkoperator.repository.ComplaintHistoryRepository;
import pl.kedziorek.mpkoperator.repository.ComplaintRepository;
import pl.kedziorek.mpkoperator.service.ComplaintHistoryService;
import pl.kedziorek.mpkoperator.service.ComplaintService;

import javax.transaction.Transactional;
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
public class ComplaintServiceImpl implements ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final ComplaintHistoryService complaintHistoryService;
    private final ComplaintHistoryRepository complaintHistoryRepository;

    @Override
    public Complaint saveComplaint(ComplaintRequest complaintRequest) {
        log.info("Saving new complaint to the database");
        Complaint complaint = Complaint.map(complaintRequest);

        Complaint complaintResult = complaintRepository.save(complaint);
        complaintHistoryService.saveComplaintInComplaintHistory(complaintResult, complaintResult.getUuid());
        return complaintRepository.save(complaint);
    }

    @Override
    public List<Complaint> getAllComplaints() {
        return (List<Complaint>) complaintRepository.findAll();
    }

    @Override
    public Complaint findByUuid(UUID uuid) {
        return complaintRepository.findByUuid(uuid).orElseThrow(() ->
                new ResourceNotFoundException("Complaint not found in database"));
    }

    @Override
    public Complaint updateComplaint(Complaint complaint, UUID uuid) {
        Complaint updatedComplaint = complaintRepository.findByUuid(uuid).orElseThrow(() ->
                new ResourceNotFoundException("Complaint not found in the database"));

        log.info("Updating complaint with uuid: {} to the database", updatedComplaint.getUuid());

        updatedComplaint.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        updatedComplaint.setModifiedAt(LocalDateTime.now());
        updatedComplaint.setComplaintStatus(complaint.getComplaintStatus());

        complaintHistoryService.saveComplaintInComplaintHistory(updatedComplaint, updatedComplaint.getUuid());

        return complaintRepository.save(updatedComplaint);
    }

    @Override
    public DataResponse<?> getComplaints(Map<String, String> params, int page, int size) {
        Page<Complaint> pageComplaint = complaintRepository.findAllParams(
                params.get("placeOfEvent"),
                params.get("nameOfNotifier"),
                params.get("surnameOfNotifier"),
                params.get("peselOfNotifier"),
                params.get("createdBy"),
//                convertToLocalDate(params.get("date")),
                PageRequest.of(page, size)
        );

        return DataResponse.<Complaint>builder()
                .data(pageComplaint.get().collect(Collectors.toList()))
                .page(pageComplaint.getTotalPages())
                .size(pageComplaint.getTotalElements())
                .build();
    }
}
