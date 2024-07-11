package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.models.DocumentoAcessorio;
import br.leg.rr.al.supleg.models.ModeloRoteiro;
import br.leg.rr.al.supleg.models.Ordem;
import br.leg.rr.al.supleg.models.OrdemDia;
import br.leg.rr.al.supleg.models.Roteiro;
import br.leg.rr.al.supleg.models.RoteiroSecretario;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class RoteiroRepository{

    @PersistenceContext
    protected EntityManager em;

    //___ROTEIRO________________________________________________________________
    public void salvar(Roteiro o){
        em.persist(o);
    }
    
    public Roteiro atualizar(Roteiro o){
        return em.merge(o);
    }

    public void excluir(Roteiro model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<Roteiro> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery(
                "SELECT o FROM Roteiro o "
                        + "JOIN FETCH o.modeloRoteiro "
//                        + "LEFT JOIN FETCH m.abertura a "
//                        + "LEFT JOIN FETCH m.fechamento f "
//                        + "LEFT JOIN FETCH a.trechos "
//                        + "LEFT JOIN FETCH f.trechos "
                        + "WHERE o.id = :id", Roteiro.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public Optional<ModeloRoteiro> buscarAberturaModeloRoteiroPorId(Long id){
        return Optional.ofNullable(em.createQuery(
                "SELECT o FROM ModeloRoteiro o "
                        + "JOIN FETCH o.abertura a "
                        + "JOIN FETCH a.trechos "
                        + "WHERE o.id = :id", ModeloRoteiro.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public Optional<ModeloRoteiro> buscarFechamentoModeloRoteiroPorId(Long id){
        return Optional.ofNullable(em.createQuery(
                "SELECT o FROM ModeloRoteiro o "
                        + "JOIN FETCH o.fechamento f "
                        + "JOIN FETCH f.trechos "
                        + "WHERE o.id = :id", ModeloRoteiro.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public List<Roteiro> listarUltimos(Integer quantidade){
        return em.createQuery("SELECT o FROM Roteiro o ORDER BY o.dataSessao DESC", Roteiro.class)
                .setMaxResults(quantidade)
                .getResultList();
    }
    
    public List<Roteiro> listarPorIntervalo(LocalDateTime dataInicial, LocalDateTime dataFinal){
        return em.createQuery("SELECT o FROM Roteiro o WHERE o.dataSessao BETWEEN :dataInicial AND :dataFinal ORDER BY o.dataSessao DESC", Roteiro.class)
                .setParameter("dataInicial", dataInicial)
                .setParameter("dataFinal", dataFinal)
                .getResultList();
    }
    
    public int atualizarDataRoteiro(Long idRoteiro, LocalDate dataSessao){
        return em.createNativeQuery("UPDATE sup_leg.roteiro SET data_sessao = :dataSessao WHERE id = :idRoteiro")
                .setParameter("idRoteiro", idRoteiro)
                .setParameter("dataSessao", dataSessao)
                .executeUpdate();
    }
    
    public int atualizarRevisaoRoteiro(){
        return em.createNativeQuery("UPDATE sup_leg.roteiro SET roteiro_presidente_revisado=true, roteiro_secretario_revisado=true")
                .executeUpdate();
    }
    
    //___ROTEIRO SECRETARIO_____________________________________________________
    public void salvarRoteiroSecretario(RoteiroSecretario o){
        em.persist(o);
    }

    public RoteiroSecretario atualizarRoteiroSecretario(RoteiroSecretario o){
        return em.merge(o);
    }
    
    public List<RoteiroSecretario> listarRoteiroSecretarioPorIdRoteiro(Long idRoteiro){
        return em.createQuery("SELECT o FROM RoteiroSecretario o LEFT JOIN FETCH o.documentos WHERE o.roteiro.id = :idRoteiro", RoteiroSecretario.class)
                .setParameter("idRoteiro", idRoteiro)
                .getResultList();
    }
    
    public List<RoteiroSecretario> listarRoteiroSecretarioPorIdRoteiro2(Long idRoteiro){
        return em.createQuery("SELECT o FROM RoteiroSecretario o WHERE o.roteiro.id = :idRoteiro", RoteiroSecretario.class)
                .setParameter("idRoteiro", idRoteiro)
                .getResultList();
    }
    
    public int excluirRoteiroSecretarioPorIdRoteiro(Long idRoteiro){
        return em.createNativeQuery("DELETE FROM sup_leg.roteiro_secretario rs WHERE rs.id_roteiro = :idRoteiro")
                .setParameter("idRoteiro", idRoteiro)
                .executeUpdate();
    }
    
    //___DOCUMENTO ACESSORIO____________________________________________________
    public void salvarDocumentoAcessorio(DocumentoAcessorio o){
        em.persist(o);
    }
    
    public DocumentoAcessorio atualizarDocumentoAcessorio(DocumentoAcessorio o){
        return em.merge(o);
    }
    
    public DocumentoAcessorio buscarDocumentoAcessorioPorId(Long id){
        return em.createQuery("SELECT d FROM DocumentoAcessorio d LEFT JOIN FETCH d.links WHERE d.id = :id", DocumentoAcessorio.class)
                .setParameter("id", id)
                .getSingleResult();
    }
    
    public DocumentoAcessorio buscarDocumentoAcessorioPorOrdem(Ordem ordem){
        return em.createQuery("SELECT d FROM DocumentoAcessorio d WHERE d.ordem.id = :idOrdem", DocumentoAcessorio.class)
                .setParameter("idOrdem", ordem.getId())
                .getSingleResult();
    }
    
    public int excluirDocumentoAcessorioPorIdOrdem(Long idOrdem){
        return em.createNativeQuery("DELETE FROM sup_leg.documento_acessorio WHERE id_ordem = :idOrdem")
                .setParameter("idOrdem", idOrdem)
                .executeUpdate();
    }
    
    public int excluirDocumentoAcessorioPorIdRoteiroSecretario(Long idRoteiroSecretario){
        return em.createNativeQuery("DELETE FROM sup_leg.documento_acessorio da WHERE da.roteiro_secretario = :idRoteiroSecretario")
                .setParameter("idRoteiroSecretario", idRoteiroSecretario)
                .executeUpdate();
    }
    
    public List<DocumentoAcessorio> listarDocumentoAcessorioPorRoteiroSecretario(RoteiroSecretario rs){
        return em.createQuery("SELECT d FROM DocumentoAcessorio d WHERE d.roteiroSecretario = :rs", DocumentoAcessorio.class)
                .setParameter("rs", rs)
                .getResultList();
    }
    
    public void salvarDocumentoAcessorio(Long idOrdem, Long idRoteiroSecretario){
        em.createNativeQuery("INSERT INTO sup_leg.documento_acessorio(id_ordem, roteiro_secretario) VALUES (" + idOrdem+", " + idRoteiroSecretario +")")
            .executeUpdate();
    }
    //___LINK___________________________________________________________________
    public int excluirLinkPorDocumentoAcessorio(DocumentoAcessorio doc){
        return em.createNativeQuery("DELETE FROM sup_leg.link l WHERE l.documento_acessorio_id = :idDoc")
                .setParameter("idDoc", doc.getId())
                .executeUpdate();
    }
    
    //___ORDEM__________________________________________________________________
    public List<OrdemDia> buscarOrdemPorOrdemDiaESequencia(OrdemDia ordemDia){
        return em.createQuery("SELECT o FROM OrdemDia o JOIN FETCH o.ordens os WHERE o = :ordemDia", OrdemDia.class)
                .setParameter("ordemDia", ordemDia)
                .getResultList();
    }
}
