package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.TipoMateria;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.services.TipoMateriaService;
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
public class TipoMateriaController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private LogService logService;
    
    @Inject
    private TipoMateriaService tipoMateriaService;
    
    @Getter
    private List<TipoMateria> tipoMaterias;
    
    @Getter
    @Setter
    private TipoMateria tipoMateria;

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        iniciar();
        
        logService.salvar(TipoAcao.ACESSOU, "O usuário acessou a página: " + FacesUtils.getURL(), "","");
    }

    public String salvar(){
        try {
            tipoMateriaService.salvar(tipoMateria);
            
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "tipoMateria.xhtml" + "?faces-redirect=true";
    }

    public String excluir(){
        try {
            tipoMateriaService.excluir(tipoMateria);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "tipoMateria.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "tipoMateria.xhtml" + "?faces-redirect=true";
    }

    //__________________________________________________________________________
    private void iniciar() {
        tipoMateria = new TipoMateria();
        tipoMateria.setStatus(Boolean.TRUE);
        
        listarTipoMateriaesAtivos();
    }

    private void listarTipoMateriaesAtivos(){
        try {
            tipoMaterias = tipoMateriaService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar TipoMateria");
        }
    }
}
