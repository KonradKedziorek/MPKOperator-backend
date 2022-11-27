package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.dto.ComplaintRequest;
import pl.kedziorek.mpkoperator.domain.dto.DataResponse;

import java.util.Map;
import java.util.UUID;

public interface ComplaintService {
    Complaint saveComplaint(ComplaintRequest complaint);
    Complaint findByUuid(UUID uuid);
    Complaint updateComplaint(Complaint complaint, UUID uuid);
    DataResponse<?> getComplaints(Map<String, String> params, int page, int size);
}
