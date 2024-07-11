package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TipoAcao;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;

import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.models.Fechamento;
import br.leg.rr.al.supleg.repositories.FechamentoRepository;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.util.List;
import javax.inject.Inject;

@ViewScoped
public class FechamentoService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private FechamentoRepository repository;

    @Inject
    private LogService logService;

    //__________________________________________________________________________
    public void salvar(Fechamento model) {
        try {
            if (model.getId() != null) {
                repository.atualizar(model);
                MensagensUtils.addInfoMessageFlashScoped("Fechamento atualizado com sucesso.");
                logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou o fechamento.", "", model.toString());
            } else {
                if (!validar(model)) {
                    MensagensUtils.addWarnMessageFlashScoped("Já um Fechamento cadastrada com esses dados.");
                } else {
                    repository.salvar(model);
                    MensagensUtils.addInfoMessageFlashScoped("Fechamento salvo com sucesso.");
                    logService.salvar(TipoAcao.SALVOU, "O usuário salvou o fechamento.", "", model.toString());
                }
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar Fechamento: " + e.getLocalizedMessage());
        }
    }

    public void excluir(Fechamento fechamento) {
        try {
            repository.excluir(fechamento);
            MensagensUtils.addInfoMessageFlashScoped("Fechamento excluído com sucesso.");
            logService.salvar(TipoAcao.APAGOU, "O usuário excluiu o fechamento.", fechamento.toString(), "");
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir Fechamento: " + e.getLocalizedMessage());
        }
    }

    public List<Fechamento> listarPorStatus(Boolean status) {
        try {
            return repository.listarPorStatus(status);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar Fechamentoes: " + e.getLocalizedMessage());
        }
    }

    private Boolean validar(Fechamento model) {
        var sessao = repository.verificarDuplicidade(model);
        var duplicidade = !sessao.isEmpty();

        return !duplicidade;
    }
}
