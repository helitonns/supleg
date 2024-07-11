package br.leg.rr.al.supleg.models;

import br.leg.rr.al.supleg.dtos.EtiquetaDTO;
import br.leg.rr.al.supleg.enums.TurnoVotacao;
import br.leg.rr.al.supleg.utils.BaseEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author heliton.nascimento
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Builder
public class Ordem implements Serializable, BaseEntity{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;
    
    @Column(nullable = false)
    private Integer ordem;
    
    @Column(nullable = true, name = "id_materia")
    private String idMateria;
    
    @Column(nullable = false)
    private String autor;
    
    @Column(nullable = true, name = "tipo_autor")
    private String tipoAutor;
    
    @Column(nullable = false)
    private String materia;
    
    @Column(nullable = true, name = "id_materia_principal")
    private String idMateriaPrincipal;
    
    @Column(nullable = true, name = "autor_materia_principal")
    private String autorMateriaPrincipal;
    
    @Column(nullable = true, name = "tipo_autor_materia_principal")
    private String tipoAutorMateriaPrincipal;
    
    @Column(nullable = true, name = "materia_materia_principal")
    private String materiaMateriaPrincipal;
    
    @Column(nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean veto;
    
    private String processo;
    
    private String protocolo;
    
    private String turno;
    
    @Column(nullable = false, columnDefinition = "text")
    private String ementa;
    
    @Column(nullable = true)
    private String resultado;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "status_materia")
    private StatusMateria statusMateria;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "status_tramitacao")
    private StatusTramitacao statusTramitacao;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "parecer_procuradoria")
    private ParecerProcuradoria parecerProcuradoria;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "roteiro_materia")
    private RoteiroMateria roteiroMateria;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "turno_votacao", nullable = false)
    private TurnoVotacao turnoVotacao;
    
    @Column(nullable = true)
    private String etiqueta;
    
    @Column(name = "cor_etiqueta", nullable = true)
    private String corEtiqueta;
    
    public String getOrdemCompleta(){
        return ordem < 10 ? "0" + ordem : ""+ordem;
    }
    
    public String getNomeMateria(){
        if (veto) {
             return (materia.replace("Mensagem Governamental", "Mensagem Governamental de veto") + " ao " + materiaMateriaPrincipal).toUpperCase();
        }
        return materia.toUpperCase();
    }
    
    public String getNomeAutor(){
        if (veto) {
             return tipoAutorMateriaPrincipal.equalsIgnoreCase("PARLAMENTAR") ? "DEP. " + autorMateriaPrincipal.toUpperCase() : tipoAutor.toUpperCase();
        }
        return tipoAutor.equalsIgnoreCase("PARLAMENTAR") ? "DEP. " + autor.toUpperCase() : tipoAutor.toUpperCase();
    }
    
    public List<EtiquetaDTO> getEtiquetas(){
        if(etiqueta != null){
            var etiquetas = etiqueta.split(";");
            var cores = corEtiqueta.split(";");
            var lista = new ArrayList<EtiquetaDTO>();
            
            for (int i = 0; i < etiquetas.length; i++) {
                lista.add(new EtiquetaDTO(etiquetas[i], cores[i]));
            }
            
            return lista;
        }
        return null;
    }
    
    public String getEtiquetasRelatorio(){
        var result = "";
        if(etiqueta != null && !etiqueta.isBlank()){
            result = etiqueta.replaceFirst(";", ", ");
            if(result.contains(";")){
                result = result.replace(";", ".");
            }else{
                result = result.replace(", ", ".");
            }
        }
        return result;
    }
}
