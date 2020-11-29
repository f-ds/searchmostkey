package it.fds.searchmostkey;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstimationDTO {
    private String keyword;
    private Integer score;
}
