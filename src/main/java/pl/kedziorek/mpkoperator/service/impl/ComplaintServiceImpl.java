package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Comment;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.dto.request.ComplaintRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;
import pl.kedziorek.mpkoperator.domain.enums.ComplaintStatus;
import pl.kedziorek.mpkoperator.repository.CommentRepository;
import pl.kedziorek.mpkoperator.repository.ComplaintRepository;
import pl.kedziorek.mpkoperator.service.ComplaintHistoryService;
import pl.kedziorek.mpkoperator.service.ComplaintService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static pl.kedziorek.mpkoperator.utils.LocalDateConverter.convertToLocalDate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ComplaintServiceImpl implements ComplaintService<Complaint> {
    private final ComplaintRepository complaintRepository;
    private final ComplaintHistoryService complaintHistoryService;
    private final CommentRepository commentRepository;

    @Override
    public Complaint saveOfUpdateComplaint(ComplaintRequest complaintRequest) {
        log.info("Saving new complaint to the database");
        //if uuid is null should create new object
        if (Objects.equals(complaintRequest.getUuid(), "")) {
            return saveComplaint(complaintRequest);
        }//else update existing object
        return editComplaint(complaintRequest);
    }

    @Override
    public Complaint findByUuid(UUID uuid) {
        return complaintRepository.findByUuid(uuid).orElseThrow(() ->
                new ResourceNotFoundException("Complaint not found in database"));
    }

    @Override
    public Complaint updateComplaintStatus(ComplaintStatus complaintStatus, UUID uuid) {
        Complaint updatedComplaint = complaintRepository.findByUuid(uuid).orElseThrow(() ->
                new ResourceNotFoundException("Complaint not found in the database"));

        log.info("Updating complaint with uuid: {} to the database", updatedComplaint.getUuid());

        updatedComplaint.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        updatedComplaint.setModifiedAt(LocalDateTime.now());
        updatedComplaint.setComplaintStatus(complaintStatus);

        complaintHistoryService.saveComplaintInComplaintHistory(updatedComplaint, updatedComplaint.getUuid());

        return complaintRepository.save(updatedComplaint);
    }

    @Override
    public DataResponse<Complaint> getComplaints(Map<String, String> params, int page, int size) {
      Page<Complaint> pageComplaint = complaintRepository.findAllParams(
                params.get("placeOfEvent") == null ? "" : params.get("placeOfEvent").toUpperCase(),
                params.get("nameOfNotifier") == null ? "" : params.get("nameOfNotifier").toUpperCase(),
                params.get("surnameOfNotifier") == null ? "" : params.get("surnameOfNotifier").toUpperCase(),
                params.get("peselOfNotifier") == null ? "" : params.get("peselOfNotifier"),
                params.get("createdBy") == null ? "" : params.get("createdBy").toUpperCase(),
                convertToLocalDate(params.get("date")),
                params.get("complaintStatus") != null ? ComplaintStatus.valueOf(params.get("complaintStatus")) : null,
                PageRequest.of(page, size)
        );

        return DataResponse.<Complaint>builder()
                .data(pageComplaint.get().collect(Collectors.toList()))
                .page(pageComplaint.getTotalPages())
                .size(pageComplaint.getTotalElements())
                .build();
    }

    @Override
    public List<Comment> createComment(UUID uuid, String content) {
        Complaint complaint = findByUuid(uuid);
        Comment comment = Comment.builder()
                .content(content)
                .complaint(complaint)
                .createdAt(LocalDateTime.now())
                .createdBy(SecurityContextHolder.getContext().getAuthentication().getName())
                .build();
        Comment newComment =  commentRepository.save(comment);
        List<Comment> comments = complaint.getComments();
        comments.add(newComment);
        return comments;
    }

    private Complaint saveComplaint(ComplaintRequest complaintRequest) {
        Complaint complaint = Complaint.map(complaintRequest);
        Complaint complaintResult = complaintRepository.save(complaint);

        complaintHistoryService.saveComplaintInComplaintHistory(complaintResult, complaintResult.getUuid());
        return complaintResult;
    }

    private Complaint editComplaint(ComplaintRequest complaintRequest) {
        Complaint complaint = findByUuid(UUID.fromString(complaintRequest.getUuid()));
        var complaintRef = changePropertiesValue(complaintRequest, complaint);
        return complaintRepository.save(complaintRef);
    }

    private Complaint changePropertiesValue(ComplaintRequest complaintRequest, Complaint complaint) {
        complaint.setDateOfEvent(LocalDateTime.of(LocalDate.parse(complaintRequest.getDateOfEvent()), LocalTime.parse(complaintRequest.getTimeOfEvent())));
        complaint.setContactToNotifier(complaintRequest.getContactToNotifier());
        complaint.setDescription(complaintRequest.getDescription());
        complaint.setNameOfNotifier(complaintRequest.getNameOfNotifier());
        complaint.setSurnameOfNotifier(complaintRequest.getSurnameOfNotifier());
        complaint.setPlaceOfEvent(complaintRequest.getPlaceOfEvent());
        complaint.setPeselOfNotifier(complaintRequest.getPeselOfNotifier());
        complaint.setModifiedAt(LocalDateTime.now());
        complaint.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return complaint;
    }
}
