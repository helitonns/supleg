package br.leg.rr.al.supleg.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 *
 * @author heliton.nascimento
 */
@Data
public class ResultDTO {

    @SerializedName("codReuniao")
    private Integer codReuniao;
    
    @SerializedName("codReuniaoPrincipal")
    private Integer codReuniaoPrincipal;
    
    @SerializedName("txtTituloReuniao")
    private String txtTituloReuniao;
    
    @SerializedName("txtSiglaOrgao")
    private String txtSiglaOrgao;
    
    @SerializedName("txtApelido")
    private String txtApelido;
    
    @SerializedName("txtNomeOrgao")
    private String txtNomeOrgao;
    
    @SerializedName("codEstadoReuniao")
    private Integer codEstadoReuniao;
    
    @SerializedName("txtTipoReuniao")
    private String txtTipoReuniao;
    
    @SerializedName("txtObjeto")
    private String txtObjeto;
    
    @SerializedName("txtLocal")
    private String txtLocal;
    
    @SerializedName("bolReuniaoConjunta")
    private Boolean bolReuniaoConjunta;
    
    @SerializedName("bolHabilitarEventoInterativo")
    private Boolean bolHabilitarEventoInterativo;
    
    @SerializedName("idYoutube")
    private String idYoutube;
    
    @SerializedName("codEstadoTransmissaoYoutube")
    private Integer codEstadoTransmissaoYoutube;
    
    @SerializedName("datReuniaoString")
    private String datReuniaoString;
}
