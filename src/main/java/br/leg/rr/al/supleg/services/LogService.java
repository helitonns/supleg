package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TipoAcao;
import java.io.Serializable;

import javax.ejb.EJB;

import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.models.Log;
import br.leg.rr.al.supleg.models.Usuario;
import br.leg.rr.al.supleg.repositories.LogRepository;
import br.leg.rr.al.supleg.utils.FacesUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.faces.view.ViewScoped;

@ViewScoped
public class LogService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private LogRepository repository;

    //__________________________________________________________________________
    public void salvar(Log log) {
        try {
            if (log.getId() != null) {
                repository.atualizar(log);
            } else {
                repository.salvar(log);
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar Log: " + e.getLocalizedMessage());
        }
    }

    public void salvar(TipoAcao tipoAcao, String mensagem, String estadoInicial, String estadoFinal) {
        try {
            var usuario = (Usuario) FacesUtils.getBean("usuario");
            if (usuario != null) {
                var log = Log.builder()
                        .dataAcao(LocalDateTime.now())
                        .usuario(usuario)
                        .tipoAcao(tipoAcao)
                        .ip(FacesUtils.getIP())
                        .mensagem(mensagem)
                        .build();
                if (estadoInicial != null) {
                    log.setEstadoInicial(estadoInicial);
                }
                if (estadoFinal != null) {
                    log.setEstadoFinal(estadoFinal);
                }

                repository.salvar(log);
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar Log: " + e.getLocalizedMessage());
        }
    }

    public void excluir(Log log) {
        try {
            repository.excluir(log);
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir Log: " + e.getLocalizedMessage());
        }
    }

    public List<Log> pesquisarLogs(LocalDate dataInicial, LocalDate dataFinal, Usuario usuario, TipoAcao tipoAcao) {
        try {
            return repository.pesquisarLogs(dataInicial, dataFinal, usuario, tipoAcao);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar Log: " + e.getLocalizedMessage());
        }
    }
    
    public void excluirPorData(LocalDate dataInicial, LocalDate dataFinal){
        try {
            
            repository.excluirPorData(dataInicial.atTime(0, 0, 0), dataFinal.atTime(23, 59, 59));
        } catch (Exception e) {
            throw new DAOException("Erro ao excluir Log: " + e.getLocalizedMessage());
        }
    }
}
