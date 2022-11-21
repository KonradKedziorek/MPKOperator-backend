package pl.kedziorek.mpkoperator.service;

import pl.kedziorek.mpkoperator.domain.Complaint;

import javax.swing.text.html.Option;
import java.util.List;

public interface ComplaintService {
    Complaint saveComplaint(Complaint complaint);
    List<Complaint> getAllComplaints();
    List<Complaint> getComplaintsOfOneNotifier(String peselOfNotifier);

}
