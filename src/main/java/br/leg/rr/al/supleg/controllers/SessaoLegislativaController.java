package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.SessaoLegislativa;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.services.SessaoLegislativaService;
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
public class SessaoLegislativaController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private SessaoLegislativaService sessaoLegislativaService;
    
    @Inject
    private LogService logService;
    
    @Getter
    private List<SessaoLegislativa> sessoesLegislativas;
    
    @Getter
    @Setter
    private SessaoLegislativa sessaoLegislativa;

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        iniciar();
        
        logService.salvar(TipoAcao.ACESSOU, "O usuário acessou a página: " + FacesUtils.getURL(), "","");
    }

    public String salvar(){
        try {
            sessaoLegislativaService.salvar(sessaoLegislativa);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "sessaoLegislativa.xhtml" + "?faces-redirect=true";
    }

    public String excluir(){
        try {
            sessaoLegislativaService.excluir(sessaoLegislativa);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "sessaoLegislativa.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "sessaoLegislativa.xhtml" + "?faces-redirect=true";
    }

    //__________________________________________________________________________
    private void iniciar() {
        sessaoLegislativa = new SessaoLegislativa();
        sessaoLegislativa.setStatus(Boolean.TRUE);
        
        listarSessoesLegislativasAtivas();
    }

    private void listarSessoesLegislativasAtivas(){
        try {
            sessoesLegislativas = sessaoLegislativaService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar SessaoLegislativa");
        }
    }
}
