package com.cloud.aerohub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportPage {
    private List<AirportDto> content;
    private long totalElements;
    private long totalPages;
    private int pageNumber;
}
