package br.leg.rr.al.supleg.enums;

import lombok.Getter;

/**
 *
 * @author heliton
 */
@Getter
public enum TipoMateriaPesquisa {

    PROJEO_DE_LEI("Projeto de Lei", 1), 
    PROJEO_DE_LEI_COMPLEMENTAR("Projeto de Lei Complementar", 2), 
    INDICACAO("Indicação", 3), 
    REQUERIMENTO("Requerimento", 4), 
    PROPOSTA_DE_EMENDA_A_CONSTITUICAO("Proposta de Emenda à Constituição", 5), 
    PROJETO_DE_DECRETO_LEGISLATIVO("Projeto de Decreto Legislativo", 6), 
    PROJETO_DE_RESOLUCAO_LEGISLATIVA("Projeto de Resolução Legislativa", 7), 
    PARECER("Parecer", 8), 
    MOCAO("Moção", 9),
    EMENDA("Emensa", 10),
    RECURSO("Recurso", 11),
    REGIMENTO_INTERNO("Regimento Interno", 12),
    CONSTITUICAO_ESTADUAL("Constituição Estadual", 13),
    PEDIDO_DE_INFORCAO("Pedido de Informação", 14),
    SUBEMENDA("Subemenda", 15),
    PROPOSTA_DE_FISCALIZACAO_e_CONTROLE("Proposta de Fiscalização e Controle", 16),
    MENSAGEM_GOVERNAMENTAL("Mensagem Governamental", 17),
    SUBSTITUTIVO("Substitutivo", 18),
    REPRESENTACAO("Representação", 19);
    
    private final String descricao;
    private final int sequencia;
    
    TipoMateriaPesquisa(String descricao, int sequencia){
        this.descricao = descricao;
        this.sequencia = sequencia;
    }

    public String value() {
        return name();
    }

    public String getValue() {
        return name();
    }

    public static TipoMateriaPesquisa fromValue(String v) {
        return valueOf(v);
    }

}
