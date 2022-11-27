package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.ComplaintHistory;

import java.util.UUID;

public interface ComplaintHistoryService {
    ComplaintHistory saveComplaintInComplaintHistory(Complaint complaint, UUID uuid);
}
