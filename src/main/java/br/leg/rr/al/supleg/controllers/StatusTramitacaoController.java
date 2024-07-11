package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.StatusTramitacao;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.services.StatusTramitacaoService;
import br.leg.rr.al.supleg.utils.FacesUtils;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Heliton
 */
@Named
@ViewScoped
public class StatusTramitacaoController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private LogService logService;
    
    @Inject
    private StatusTramitacaoService statusTramitacaoService;
    
    @Getter
    private List<StatusTramitacao> statusTramitacoes;
    
    @Getter
    @Setter
    private StatusTramitacao statusTramitacao;

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        iniciar();
        
        logService.salvar(TipoAcao.ACESSOU, "O usuário acessou a página: " + FacesUtils.getURL(), "","");
    }

    public String salvar(){
        try {
            statusTramitacaoService.salvar(statusTramitacao);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "statusTramitacao.xhtml" + "?faces-redirect=true";
    }

    public String excluir(){
        try {
            statusTramitacaoService.excluir(statusTramitacao);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "statusTramitacao.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "statusTramitacao.xhtml" + "?faces-redirect=true";
    }

    //__________________________________________________________________________
    private void iniciar() {
        statusTramitacao = new StatusTramitacao();
        statusTramitacao.setStatus(Boolean.TRUE);
        
        listarStatusTramitacaoesAtivas();
    }

    private void listarStatusTramitacaoesAtivas(){
        try {
            statusTramitacoes = statusTramitacaoService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar StatusTramitacao");
        }
    }
}
