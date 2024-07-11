package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TipoAcao;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;

import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.models.Parlamentar;
import br.leg.rr.al.supleg.repositories.ParlamentarRepository;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.util.List;
import javax.inject.Inject;

@ViewScoped
public class ParlamentarService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ParlamentarRepository repository;

    @Inject
    private LogService logService;

    //__________________________________________________________________________
    public void salvar(Parlamentar parlamentar) {
        try {

            if (parlamentar.getId() != null) {
                repository.atualizar(parlamentar);
                MensagensUtils.addInfoMessageFlashScoped("Parlamentar atualizado com sucesso.");
                logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou o parlamentar.", "", parlamentar.toString());
            } else {
                if (!validar(parlamentar)) {
                    MensagensUtils.addWarnMessageFlashScoped("Já um Parlamentar cadastrado com esses dados.");
                } else {
                    repository.salvar(parlamentar);
                    MensagensUtils.addInfoMessageFlashScoped("Parlamentar salvo com sucesso.");
                    logService.salvar(TipoAcao.SALVOU, "O usuário salvou o parlamentar..", "", parlamentar.toString());
                }
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar Parlamentar: " + e.getLocalizedMessage());
        }
    }

    public void excluir(Parlamentar parlamentar) {
        try {
            repository.excluir(parlamentar);
            MensagensUtils.addInfoMessageFlashScoped("Parlamentar excluído com sucesso.");
            logService.salvar(TipoAcao.APAGOU, "O usuário excluiu o parlamentar.", parlamentar.toString(), "");
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir Parlamentar: " + e.getLocalizedMessage());
        }
    }

    public List<Parlamentar> listarPorStatus(Boolean status) {
        try {
            return repository.listarPorStatus(status);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar Parlamentares: " + e.getLocalizedMessage());
        }
    }

    private Boolean validar(Parlamentar parlamentar) {
        var sessao = repository.verificarDuplicidade(parlamentar);
        var duplicidade = !sessao.isEmpty();

        return !duplicidade;
    }
}
