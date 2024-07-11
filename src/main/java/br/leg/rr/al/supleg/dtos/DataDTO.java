package br.leg.rr.al.supleg.dtos;

import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author heliton.nascimento
 */
@Data
@ToString
public class DataDTO {
    private PaginationDTO pagination;
    private List<ResultDTO> results;
}
