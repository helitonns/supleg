package br.leg.rr.al.supleg.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 *
 * @author heliton.nascimento
 */
@Data
public class PaginationDTO {

    private LinkDTO link;
    
    @SerializedName("previous_page")
    private Integer previousPage;
    
    @SerializedName("next_page")
    private Integer nextPage;
    
    @SerializedName("start_index")
    private Integer startIndex;
    
    @SerializedName("end_index")
    private Integer endIndex;
    
    @SerializedName("total_entries")
    private Integer totalEntries;
    
    @SerializedName("total_pages")
    private Integer totalPages;
    
    private Integer page;
}
