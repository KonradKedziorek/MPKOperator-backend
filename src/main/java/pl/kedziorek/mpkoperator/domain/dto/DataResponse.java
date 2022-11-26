package pl.kedziorek.mpkoperator.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DataResponse<T> {
    private List<T> data;
    private int page;
    private long size;
}
