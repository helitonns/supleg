package br.leg.rr.al.supleg.services;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.exceptions.NotFoundException;
import br.leg.rr.al.supleg.models.DocumentoAcessorio;
import br.leg.rr.al.supleg.models.Ordem;
import br.leg.rr.al.supleg.models.OrdemDia;
import br.leg.rr.al.supleg.models.Roteiro;
import br.leg.rr.al.supleg.models.RoteiroSecretario;
import br.leg.rr.al.supleg.repositories.RoteiroRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ViewScoped
public class RoteiroService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private RoteiroRepository repository;
    

    //___ROTEIRO________________________________________________________________
    public void salvar(Roteiro roteiro) {
        try {
            if (roteiro.getId() != null) {
                repository.atualizar(roteiro);
            } else {
                repository.salvar(roteiro);
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar Roteiro: " + e.getLocalizedMessage());
        }
    }

    public void excluir(Roteiro roteiro) {
        try {
            repository.excluir(roteiro);
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir Roteiro: " + e.getLocalizedMessage());
        }
    }

    public List<Roteiro> listarUltimos(Integer quantidade) {
        try {
            return repository.listarUltimos(quantidade);
        } catch (Exception e) {
            throw new DAOException("Erro ao Listar Roteiro: " + e.getLocalizedMessage());
        }
    }
    
    public List<Roteiro> listarPorIntervalo(LocalDateTime data) {
        try {
            var dataInicial = LocalDateTime.of(data.getYear(), data.getMonth(), 1, 0, 0);
            var dataFinal = LocalDateTime.of(data.getYear(), data.getMonth(), data.getMonth().length(data.toLocalDate().isLeapYear()), 23, 59);
            
            return repository.listarPorIntervalo(dataInicial, dataFinal);
        } catch (Exception e) {
            throw new DAOException("Erro ao Listar Roteiro: " + e.getLocalizedMessage());
        }
    }

    public Roteiro buscarPorId(Long id) {
        try {
            var roteiro = repository.buscarPorId(id).orElseThrow(()-> new NotFoundException("Roteiro com id %d não encontrado".formatted(id)));
            
            var modeloRoteiroAbertura = repository.buscarAberturaModeloRoteiroPorId(roteiro.getModeloRoteiro().getId()).orElse(null);
            var modeloRoteiroFechamento = repository.buscarFechamentoModeloRoteiroPorId(roteiro.getModeloRoteiro().getId()).orElse(null);
            
            if(modeloRoteiroAbertura != null){
                roteiro.getModeloRoteiro().setAbertura(modeloRoteiroAbertura.getAbertura());
            }
            
            if(modeloRoteiroFechamento != null){
                roteiro.getModeloRoteiro().setFechamento(modeloRoteiroFechamento.getFechamento());
            }
            
            return roteiro;
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar Roteiro: " + e.getLocalizedMessage());
        }
    }
    
    public void atualizarDataRoteiro(Long idRoteiro, LocalDate dataSessao){
        try {
            repository.atualizarDataRoteiro(idRoteiro, dataSessao);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar Roteiro: " + e.getLocalizedMessage());
        }
    }
    
    public void atualizarRevisaoRoteiro(){
        try {
            repository.atualizarRevisaoRoteiro();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar Roteiro: " + e.getLocalizedMessage());
        }    
    }

    //___ROTEIRO SECRETARIO_____________________________________________________
    public void salvarSecretario(RoteiroSecretario roteiro) {
        try {
            if (roteiro.getId() != null) {
                repository.atualizarRoteiroSecretario(roteiro);
            } else {
                repository.salvarRoteiroSecretario(roteiro);
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar Roteiro: " + e.getLocalizedMessage());
        }
    }

    public RoteiroSecretario listarRoteiroSecretarioPorIdRoteiro(Long idRoteiro) {
        try {
            var roteiros = repository.listarRoteiroSecretarioPorIdRoteiro(idRoteiro);
            if(roteiros == null){
                System.out.println("ENTROU NO listarRoteiroSecretarioPorIdRoteiro2");
                
                roteiros = repository.listarRoteiroSecretarioPorIdRoteiro2(idRoteiro);
                
                System.out.println("listarRoteiroSecretarioPorIdRoteiro - ROTEIROS: " + roteiros);
            }
            
            if(!roteiros.isEmpty()){
                var roteiroSecretario = roteiros.get(0);
            
                roteiroSecretario.getDocumentos().forEach(doc -> {
                    try {
                        var docPesquisado = repository.buscarDocumentoAcessorioPorId(doc.getId());
                        doc.setLinks(docPesquisado.getLinks() != null ? docPesquisado.getLinks() : null);
                    } catch (Exception e) {
                    }

                });
                return roteiroSecretario;
            }
            
            return null;
        } catch (Exception e) {
            throw new DAOException("Erro ao Listar RoteiroSecretario: " + e.getLocalizedMessage());
        }
    }
    
    public RoteiroSecretario listarRoteiroSecretarioPorIdRoteiro2(Long idRoteiro) {
        try {
            var roteiros = repository.listarRoteiroSecretarioPorIdRoteiro2(idRoteiro);
            
            if(!roteiros.isEmpty()){
                var roteiroSecretario = roteiros.get(0);
            
                roteiroSecretario.getDocumentos().forEach(doc -> {
                    try {
                        var docPesquisado = repository.buscarDocumentoAcessorioPorId(doc.getId());
                        doc.setLinks(docPesquisado.getLinks() != null ? docPesquisado.getLinks() : null);
                    } catch (Exception e) {
                    }

                });
                return roteiroSecretario;
            }
            
            return null;
        } catch (Exception e) {
            throw new DAOException("Erro ao Listar RoteiroSecretario: " + e.getLocalizedMessage());
        }
    }
    
    public int excluirRoteiroSecretarioPorIdRoteiro(Long idRoteiro) {
        try {
            return repository.excluirRoteiroSecretarioPorIdRoteiro(idRoteiro);
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir RoteiroSecretario: " + e.getLocalizedMessage());
        } 
    }
    
    //___DOCUMENTO ACESSORIO____________________________________________________
    public void salvarDocumentoAcessorio(DocumentoAcessorio doc) {
        try {
            if (doc.getId() != null) {
                repository.atualizarDocumentoAcessorio(doc);
            } else {
                repository.atualizarDocumentoAcessorio(doc);
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar Documento Acessório: " + e.getLocalizedMessage());
        }
    }
    
    public int excluirDocumentoAcessorioPorIdRoteiroSecretario(Long idRoteiroSecretaio) {
        try {
            return repository.excluirDocumentoAcessorioPorIdRoteiroSecretario(idRoteiroSecretaio);
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir DocumentoAcessorio: " + e.getLocalizedMessage());
        } 
    }
    
    public List<DocumentoAcessorio> listarDocumentoAcessorioPorRoteiroSecretario(RoteiroSecretario rs) {
        try {
            return repository.listarDocumentoAcessorioPorRoteiroSecretario(rs);
        } catch (Exception e) {
            throw new DAOException("Erro ao Listar DocumentoAcessorio: " + e.getLocalizedMessage());
        } 
    }
    
    public DocumentoAcessorio listarDocumentoAcessorioPorOrdem(Ordem ordem) {
        try {
            return repository.buscarDocumentoAcessorioPorOrdem(ordem);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar DocumentoAcessorio: " + e.getLocalizedMessage());
        } 
    }
    
    //___LINK___________________________________________________________________
    public int excluirLinkPorDocumentoAcessorio(DocumentoAcessorio doc) {
        try {
            return repository.excluirLinkPorDocumentoAcessorio(doc);
        } catch (Exception e) {
            throw new DAOException("Erro ao Excluir Link: " + e.getLocalizedMessage());
        } 
    }

    //___ORDEM__________________________________________________________________
    public List<Ordem> buscarOrdemPorOrdemDiaESequencia(OrdemDia ordemDia){
        try {
            var listaOrdemDia = repository.buscarOrdemPorOrdemDiaESequencia(ordemDia);
            return (listaOrdemDia != null) ? listaOrdemDia.get(0).getOrdens() : null;
        } catch (Exception e) {
            throw new DAOException("Erro ao buscarOrdemPorOrdemDiaESequencia: " + e.getLocalizedMessage());
        }
    }
}
