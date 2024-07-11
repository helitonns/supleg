package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TipoAcao;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.models.ModeloRoteiro;
import br.leg.rr.al.supleg.repositories.ModeloRoteiroRepository;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.util.List;
import javax.inject.Inject;

@ViewScoped
public class ModeloRoteiroService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ModeloRoteiroRepository repository;
    
    @Inject
    private LogService logService;

    //__________________________________________________________________________
    public void salvar(ModeloRoteiro model) {
        try {
            
            if (model.getId() != null) {
                repository.atualizar(model);
                logService.salvar(TipoAcao.ATUALIZOU, "O usuário atualizou o Modelo Roteiro.", "", model.toString());
            } else {
                if(!validar(model)){
                    MensagensUtils.addWarnMessageFlashScoped("Já um Modelo Roteiro cadastrado com esses dados.");
                }else{
                    repository.salvar(model);
                    logService.salvar(TipoAcao.SALVOU, "O usuário salvou o Modelo Roteiro.", "", model.toString());
                }
            }
            MensagensUtils.addInfoMessageFlashScoped("Modelo Roteiro salvo com sucesso");
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar Roteiro: " + e.getLocalizedMessage());
        }
    }
    
    public void excluir(ModeloRoteiro model) {
        try {
            repository.excluir(model);
            MensagensUtils.addInfoMessageFlashScoped("Modelo Roteiro excluído com sucesso");
            logService.salvar(TipoAcao.APAGOU, "O usuário excluíu o Modelo Roteiro.", model.toString(), "");
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir Roteiro: " + e.getLocalizedMessage());
        }
    }

    public List<ModeloRoteiro> listarPorStatus(Boolean status) {
        try {
            return repository.listarPorStatus(status);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar Roteiros: " + e.getLocalizedMessage());
        }
    }
    
    private Boolean validar(ModeloRoteiro model){
        var sessao = repository.verificarDuplicidade(model);
        var duplicidade = !sessao.isEmpty();
        
        return !duplicidade;
    }
}
