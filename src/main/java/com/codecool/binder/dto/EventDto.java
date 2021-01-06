package com.codecool.binder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDto {
    private Long id;
    private String description;
    private String title;
    private Date startDate;
    private Date endDate;
    private Long owner;
    private List<Long> participants;
}
