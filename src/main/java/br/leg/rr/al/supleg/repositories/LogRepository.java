package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.Log;
import br.leg.rr.al.supleg.models.Usuario;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class LogRepository{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Log o){
        em.persist(o);
    }

    public Log atualizar(Log o){
        return em.merge(o);
    }

    public void excluir(Log model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<Log> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery("SELECT o FROM Log o WHERE o.id = :id", Log.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public List<Log> pesquisarLogs(LocalDate dataInicial,LocalDate dataFinal, Usuario usuario, TipoAcao tipoAcao){
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT l from Log l WHERE l.dataAcao BETWEEN :dataInicial AND :dataFinal ");
        
        if(usuario != null){
            sb.append("AND l.usuario = :usuario ");
        }
        
        if(tipoAcao != null){
            sb.append("AND l.tipoAcao = :tipoAcao ");
        }
        
        sb.append("ORDER BY l.dataAcao DESC");
        
        Query query = em.createQuery(sb.toString(), Log.class);
        
        query.setParameter("dataInicial", dataInicial.atTime(0, 0, 1));
        query.setParameter("dataFinal", dataFinal.atTime(23, 59, 59));
        
        if(usuario != null){
            query.setParameter("usuario", usuario);
        }
        
        if(tipoAcao != null){
            query.setParameter("tipoAcao", tipoAcao);
        }
        
        return query.getResultList();
    }
    
    public void excluirPorData(LocalDateTime dataInicial, LocalDateTime dataFinal){
        em.createQuery("DELETE FROM Log o WHERE o.dataAcao BETWEEN :dataInicial AND :dataFinal")
                .setParameter("dataInicial", dataInicial)
                .setParameter("dataFinal", dataFinal)
                .executeUpdate();
    }
}
