package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TipoAcao;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;

import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.models.MesaModelo;
import br.leg.rr.al.supleg.repositories.MesaModeloRepository;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.util.List;
import javax.inject.Inject;

@ViewScoped
public class MesaModeloService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private MesaModeloRepository repository;
    
    @Inject
    private LogService logService;


    //__________________________________________________________________________
    public void salvar(MesaModelo mesaModelo) {
        try {
            if (mesaModelo.getId() != null) {
                repository.atualizar(mesaModelo);
                MensagensUtils.addInfoMessageFlashScoped("Mesa atualizada com sucesso.");
                logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou a mesa.", "", mesaModelo.toString());
            } else {
                repository.salvar(mesaModelo);
                MensagensUtils.addInfoMessageFlashScoped("Mesa salva com sucesso.");
                logService.salvar(TipoAcao.SALVOU, "O usuário salvou a mesa.", "", mesaModelo.toString());
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar MesaModelo: " + e.getLocalizedMessage());
        }
    }
    
    public void excluir(MesaModelo mesaModelo) {
        try {
            repository.excluir(mesaModelo);
            MensagensUtils.addInfoMessageFlashScoped("Mesa excluída com sucesso.");
            logService.salvar(TipoAcao.APAGOU, "O usuário excluiu a mesa", mesaModelo.toString(), "");
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir MesaModelo: " + e.getLocalizedMessage());
        }
    }

    public List<MesaModelo> listarPorStatus(Boolean status) {
        try {
            return repository.listarPorStatus(status);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar MesaModeloes: " + e.getLocalizedMessage());
        }
    }
    
    public MesaModelo buscarUltimaPorStatus(Boolean status) {
        try {
            return repository.buscarUltimaPorStatus(status);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar MesaModelo: " + e.getLocalizedMessage());
        }
    }
}
