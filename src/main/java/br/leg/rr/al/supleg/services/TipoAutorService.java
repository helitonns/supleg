package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TipoAcao;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;

import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.models.TipoAutor;
import br.leg.rr.al.supleg.repositories.TipoAutorRepository;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.util.List;
import javax.inject.Inject;

@ViewScoped
public class TipoAutorService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private TipoAutorRepository repository;

    @Inject
    private LogService logService;

    //__________________________________________________________________________
    public void salvar(TipoAutor tipoAutor) {
        try {

            if (tipoAutor.getId() != null) {
                repository.atualizar(tipoAutor);
                MensagensUtils.addInfoMessageFlashScoped("Tipo Autor atualizada com sucesso.");
                logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou o status matéria.", "", tipoAutor.toString());
            } else {
                if (!validar(tipoAutor)) {
                    MensagensUtils.addWarnMessageFlashScoped("Já um TipoAutor cadastrado com esses dados.");
                } else {
                    repository.salvar(tipoAutor);
                    MensagensUtils.addInfoMessageFlashScoped("Tipo Autor salva com sucesso.");
                    logService.salvar(TipoAcao.SALVOU, "O usuário salvou o status matéria.", "", tipoAutor.toString());
                }
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar TipoAutor: " + e.getLocalizedMessage());
        }
    }

    public void excluir(TipoAutor tipoAutor) {
        try {
            repository.excluir(tipoAutor);
            MensagensUtils.addInfoMessageFlashScoped("Status excluído com sucesso.");
            logService.salvar(TipoAcao.APAGOU, "O usuário excluiu o status matéria.", tipoAutor.toString(), "");
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir TipoAutor: " + e.getLocalizedMessage());
        }
    }

    public List<TipoAutor> listarPorStatus(Boolean status) {
        try {
            return repository.listarPorStatus(status);
        } catch (Exception e) {
            throw new DAOException("Erro ao listar TipoAutors: " + e.getLocalizedMessage());
        }
    }

    public TipoAutor buscarPorNome(String nome) {
        try {
            return repository.buscarNome(nome).orElse(null);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar TipoAutors: " + e.getLocalizedMessage());
        }
    }

    private Boolean validar(TipoAutor model) {
        var sessao = repository.verificarDuplicidade(model);
        var duplicidade = !sessao.isEmpty();

        return !duplicidade;
    }
}
