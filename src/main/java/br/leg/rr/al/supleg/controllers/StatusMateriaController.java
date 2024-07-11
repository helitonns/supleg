package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.StatusMateria;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.services.StatusMateriaService;
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
public class StatusMateriaController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private LogService logService;
    
    @Inject
    private StatusMateriaService statusMateriaService;
    
    @Getter
    private List<StatusMateria> statusMaterias;
    
    @Getter
    @Setter
    private StatusMateria statusMateria;

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        iniciar();
        
        logService.salvar(TipoAcao.ACESSOU, "O usuário acessou a página: " + FacesUtils.getURL(), "","");
    }

    public String salvar(){
        try {
            statusMateriaService.salvar(statusMateria);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "statusMateria.xhtml" + "?faces-redirect=true";
    }

    public String excluir(){
        try {
            statusMateriaService.excluir(statusMateria);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "statusMateria.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "statusMateria.xhtml" + "?faces-redirect=true";
    }

    //__________________________________________________________________________
    private void iniciar() {
        statusMateria = new StatusMateria();
        statusMateria.setStatus(Boolean.TRUE);
        
        listarStatusMateriaesAtivos();
    }

    private void listarStatusMateriaesAtivos(){
        try {
            statusMaterias = statusMateriaService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar StatusMateria");
        }
    }
}
