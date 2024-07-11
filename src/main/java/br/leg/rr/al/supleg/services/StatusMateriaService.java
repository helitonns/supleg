package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TipoAcao;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;

import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.models.StatusMateria;
import br.leg.rr.al.supleg.repositories.StatusMateriaRepository;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.util.List;
import javax.inject.Inject;

@ViewScoped
public class StatusMateriaService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private StatusMateriaRepository repository;

    @Inject
    private LogService logService;

    //__________________________________________________________________________
    public void salvar(StatusMateria statusMateria) {
        try {

            if (statusMateria.getId() != null) {
                repository.atualizar(statusMateria);
                MensagensUtils.addInfoMessageFlashScoped("Status Matéria atualizada com sucesso.");
                logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou o status matéria.", "", statusMateria.toString());
            } else {
                if (!validar(statusMateria)) {
                    MensagensUtils.addWarnMessageFlashScoped("Já um StatusMateria cadastrado com esses dados.");
                } else {
                    repository.salvar(statusMateria);
                    MensagensUtils.addInfoMessageFlashScoped("Status Matéria salva com sucesso.");
                    logService.salvar(TipoAcao.SALVOU, "O usuário salvou o status matéria.", "", statusMateria.toString());
                }
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar StatusMateria: " + e.getLocalizedMessage());
        }
    }

    public void excluir(StatusMateria statusMateria) {
        try {
            repository.excluir(statusMateria);
            MensagensUtils.addInfoMessageFlashScoped("Status excluído com sucesso.");
            logService.salvar(TipoAcao.APAGOU, "O usuário excluiu o status matéria.", statusMateria.toString(), "");
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir StatusMateria: " + e.getLocalizedMessage());
        }
    }

    public List<StatusMateria> listarPorStatus(Boolean status) {
        try {
            return repository.listarPorStatus(status);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar StatusMaterias: " + e.getLocalizedMessage());
        }
    }

    public StatusMateria buscarPorNome(String nome) {
        try {
            return repository.buscarNome(nome).orElse(null);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar StatusMaterias: " + e.getLocalizedMessage());
        }
    }

    private Boolean validar(StatusMateria model) {
        var sessao = repository.verificarDuplicidade(model);
        var duplicidade = !sessao.isEmpty();

        return !duplicidade;
    }
}
