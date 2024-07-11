package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.RoteiroMateria;
import br.leg.rr.al.supleg.models.TipoMateria;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.services.RoteiroMateriaService;
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
public class RoteiroMateriaController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private LogService logService;
    
    @Inject
    private RoteiroMateriaService roteiroMateriaService;
    
    @Inject
    private TipoMateriaService tipoMateriaService;
    
    @Getter
    private List<RoteiroMateria> roteiroMaterias;
    
    @Getter
    private List<TipoMateria> tiposMaterias;
    
    @Getter
    @Setter
    private RoteiroMateria roteiroMateria;

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        iniciar();
        
        logService.salvar(TipoAcao.ACESSOU, "O usuário acessou a página: " + FacesUtils.getURL(), "","");
    }

    public String salvar(){
        try {
            roteiroMateriaService.salvar(roteiroMateria);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "roteiroMateria.xhtml" + "?faces-redirect=true";
    }

    public String excluir(){
        try {
            roteiroMateriaService.excluir(roteiroMateria);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "roteiroMateria.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "roteiroMateria.xhtml" + "?faces-redirect=true";
    }

    //__________________________________________________________________________
    private void iniciar() {
        roteiroMateria = new RoteiroMateria();
        roteiroMateria.setStatus(Boolean.TRUE);
        
        listarRoteiroMateriasAtivas();
        listarTiposMateriasAtivas();
    }

    private void listarRoteiroMateriasAtivas(){
        try {
            roteiroMaterias = roteiroMateriaService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar RoteiroMateria");
        }
    }
    
    private void listarTiposMateriasAtivas(){
        try {
            tiposMaterias = tipoMateriaService.listarPorStatus(Boolean.TRUE);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao Listar Tipo Materia");
        }
    }
}
