package pl.kedziorek.mpkoperator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.ComplaintHistory;
import pl.kedziorek.mpkoperator.repository.ComplaintHistoryRepository;
import pl.kedziorek.mpkoperator.service.ComplaintHistoryService;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ComplaintHistoryServiceImpl implements ComplaintHistoryService {
    private final ComplaintHistoryRepository complaintHistoryRepository;

    @Override
    public ComplaintHistory saveComplaintInComplaintHistory(Complaint complaint, UUID uuid) {
        log.info("Saving new complaint in complaint history to the database");
        ComplaintHistory complaintHistory = ComplaintHistory.createComplaintHistory(complaint, uuid);
        return complaintHistoryRepository.save(complaintHistory);
    }
}
