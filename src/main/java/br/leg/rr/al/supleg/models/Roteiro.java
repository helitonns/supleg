 package br.leg.rr.al.supleg.models;

import br.leg.rr.al.supleg.utils.BaseEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class Roteiro implements Serializable, BaseEntity{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    @Nonnull
    private Long id;
    
    @Column(name = "data_sessao",nullable = false)
    @ToString.Include
    private LocalDateTime dataSessao;   
    
    @Column(nullable = false)
    @ToString.Include
    private String nome;   
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_modelo_roteiro")
    private ModeloRoteiro modeloRoteiro;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_ordem_dia")
    private OrdemDia ordemDia;
    
    @Column(name = "roteiro_presidente_revisado", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean roteiroPresidenteRevisado;
    
    @Column(name = "roteiro_secretario_revisado", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean roteiroSecretarioRevisado;
    
    public String getNomeParte1(){
        if(nome != null && !nome.isBlank() && !nome.isEmpty()){
            if(nome.toUpperCase().contains(") SESSÃO")){
                var nomes = nome.toUpperCase().split("SESSÃO");
                return nomes[0].trim();
            }else{
                var nomes = nome.split("ª");
                return nomes[0]+"ª";
            }
        }else{
            return "";
        }
    }
    
    public String getNomeParte2(){
        if(nome != null && !nome.isBlank() && !nome.isEmpty()){
            if(nome.toUpperCase().contains(") SESSÃO")){
                var nomes = nome.toUpperCase().split("SESSÃO");
                return "SESSÃO " + nomes[1].trim();
            }else{
                var nomes = nome.split("ª");
                return nomes[1];
            }
        }else{
            return "";
        }
    }
    
    public Boolean getPossuiNumeroNoTitulo(){
        return nome.contains("ª") || nome.contains("º") || nome.contains("°");
    }
    
}
