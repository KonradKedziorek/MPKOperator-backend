package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Complaint;
import pl.kedziorek.mpkoperator.domain.dto.request.ComplaintRequest;
import pl.kedziorek.mpkoperator.domain.dto.response.DataResponse;

import java.util.Map;
import java.util.UUID;

public interface ComplaintService<T> {
    Complaint saveComplaint(ComplaintRequest complaint);
    Complaint findByUuid(UUID uuid);
    Complaint updateComplaint(Complaint complaint, UUID uuid);
    DataResponse<T> getComplaints(Map<String, String> params, int page, int size);
}
