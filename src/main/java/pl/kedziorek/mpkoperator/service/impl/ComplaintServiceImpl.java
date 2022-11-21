package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.config.exception.ResourceNotFoundException;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.repository.ComplaintRepository;
import pl.kedziorek.mpkoperator.service.ComplaintService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ComplaintServiceImpl implements ComplaintService {
    private final ComplaintRepository complaintRepository;

    @Override
    public Complaint saveComplaint(Complaint complaint) {
        log.info("Saving new complaint to the database");
        complaint.setUuid(UUID.randomUUID());
        complaint.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        complaint.setCreatedAt(LocalDateTime.now());
        return complaintRepository.save(complaint);
    }

    @Override
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    @Override
    public List<Complaint> getComplaintsOfOneNotifier(String peselOfNotifier) {
        log.info("Fetching complaint");
        return complaintRepository.findByPeselOfNotifier(peselOfNotifier)
                .orElseThrow(()-> new ResourceNotFoundException("Complaint not found in the database"));
    }
}
