package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TipoAcao;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;

import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.models.RoteiroMateria;
import br.leg.rr.al.supleg.repositories.RoteiroMateriaRepository;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.util.List;
import javax.inject.Inject;

@ViewScoped
public class RoteiroMateriaService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private RoteiroMateriaRepository repository;

    @Inject
    private LogService logService;

    //__________________________________________________________________________
    public void salvar(RoteiroMateria model) {
        try {

            if (model.getId() != null) {
                repository.atualizar(model);
                MensagensUtils.addInfoMessageFlashScoped("Roteiro Matéria atualizado com sucesso.");
                logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou o roteiro matéria.", "", model.toString());
            } else {
                if (!validar(model)) {
                    MensagensUtils.addWarnMessageFlashScoped("Já um Roteiro Matéria cadastrado com esses dados.");
                } else {
                    repository.salvar(model);
                    MensagensUtils.addInfoMessageFlashScoped("Roteiro Matéria salvo com sucesso.");
                    logService.salvar(TipoAcao.SALVOU, "O usuário salvou o roteiro matéria.", "", model.toString());
                }
            }

        } catch (Exception e) {
            throw new DAOException("Erro ao salvar RoteiroMateria: " + e.getLocalizedMessage());
        }
    }

    public void excluir(RoteiroMateria roteiroMateria) {
        try {
            repository.excluir(roteiroMateria);
            MensagensUtils.addInfoMessageFlashScoped("Roteiro Matéria excluído com sucesso.");
            logService.salvar(TipoAcao.APAGOU, "O usuário excluiu o roteiro matéria.", roteiroMateria.toString(), "");
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir RoteiroMateria: " + e.getLocalizedMessage());
        }
    }

    public List<RoteiroMateria> listarPorStatus(Boolean status) {
        try {
            return repository.listarPorStatus(status);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar RoteiroMateriaes: " + e.getLocalizedMessage());
        }
    }

    private Boolean validar(RoteiroMateria model) {
        var sessao = repository.verificarDuplicidade(model);
        var duplicidade = !sessao.isEmpty();

        return !duplicidade;
    }
}
