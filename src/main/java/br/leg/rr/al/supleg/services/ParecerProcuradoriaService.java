package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TipoAcao;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;

import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.models.ParecerProcuradoria;
import br.leg.rr.al.supleg.repositories.ParecerProcuradoriaRepository;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.util.List;
import javax.inject.Inject;

@ViewScoped
public class ParecerProcuradoriaService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ParecerProcuradoriaRepository repository;

    @Inject
    private LogService logService;

    //__________________________________________________________________________
    public void salvar(ParecerProcuradoria model) {
        try {

            if (model.getId() != null) {
                repository.atualizar(model);
                MensagensUtils.addInfoMessageFlashScoped("Parecer atualizado com sucesso.");
                logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou o parecer.", "", model.toString());
            } else {
                if (!validar(model)) {
                    MensagensUtils.addWarnMessageFlashScoped("Já um Parecer Procuradoria cadastrado com esses dados.");
                } else {
                    repository.salvar(model);
                    MensagensUtils.addInfoMessageFlashScoped("Parecer salvo com sucesso.");
                    logService.salvar(TipoAcao.SALVOU, "O usuário salvou o parecer.", "", model.toString());
                }
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar ParecerProcuradoria: " + e.getLocalizedMessage());
        }
    }

    public void excluir(ParecerProcuradoria parecerProcuradoria) {
        try {
            repository.excluir(parecerProcuradoria);
            MensagensUtils.addInfoMessageFlashScoped("Status excluído com sucesso.");
            logService.salvar(TipoAcao.APAGOU, "O usuário excluiu o parecer.", parecerProcuradoria.toString(), "");
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir ParecerProcuradoria: " + e.getLocalizedMessage());
        }
    }

    public List<ParecerProcuradoria> listarPorStatus(Boolean status) {
        try {
            return repository.listarPorStatus(status);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar ParecerProcuradorias: " + e.getLocalizedMessage());
        }
    }

    public ParecerProcuradoria buscarPorNome(String nome) {
        try {
            return repository.buscarNome(nome).orElse(null);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar StatusMaterias: " + e.getLocalizedMessage());
        }
    }

    private Boolean validar(ParecerProcuradoria model) {
        var sessao = repository.verificarDuplicidade(model);
        var duplicidade = !sessao.isEmpty();

        return !duplicidade;
    }
}
