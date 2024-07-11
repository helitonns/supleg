package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.dtos.IdMateriaDTO;
import br.leg.rr.al.supleg.enums.TurnoVotacao;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.models.DocumentoAcessorio;
import br.leg.rr.al.supleg.models.Link;
import br.leg.rr.al.supleg.models.Ordem;
import br.leg.rr.al.supleg.models.OrdemDia;
import br.leg.rr.al.supleg.repositories.OrdemDoDiaRepository;
import br.leg.rr.al.supleg.repositories.ParecerProcuradoriaRepository;
import br.leg.rr.al.supleg.repositories.RoteiroMateriaRepository;
import br.leg.rr.al.supleg.repositories.RoteiroRepository;
import br.leg.rr.al.supleg.repositories.StatusMateriaRepository;
import br.leg.rr.al.supleg.repositories.StatusTramitacaoRepository;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import br.leg.rr.al.supleg.utils.StringUtils;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@ViewScoped
public class OrdemDoDiaService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private OrdemDoDiaRepository repository;

    @EJB
    private StatusMateriaRepository repositoryStatusMateria;

    @EJB
    private StatusTramitacaoRepository repositoryStatusTramitacao;

    @EJB
    private ParecerProcuradoriaRepository repositoryParecerProcuradoria;

    @EJB
    private RoteiroMateriaRepository repositoryRoteiroMateria;
    
    @EJB
    private RoteiroRepository roteiroRepository;

    private final String BASE_URL = "https://sapl.al.rr.leg.br";
    private final String BASE_URL_ORDEM = "https://sapl.al.rr.leg.br/sessao/%d/ordemdia";
    private final String BASE_URL_MATERIA = "https://sapl.al.rr.leg.br/materia/%d";
    private final String BASE_URL_DOC_ACESSORIO = "https://sapl.al.rr.leg.br/materia/%d/documentoacessorio";
    private final String BASE_URL_AUTORIA = "https://sapl.al.rr.leg.br/materia/%d/autoria";
    private final String BASE_URL_PESQUISA_MATERIA = "https://sapl.al.rr.leg.br/materia/pesquisar-materia?tipo=%s&numero=%s&ano=%s";
    
    //__________________________________________________________________________
    public void salvar(LocalDateTime dataSessao) {
        try {
            var ordens = pesquisarSessao(dataSessao.toLocalDate());
            var ordemDia = new OrdemDia()
                    .builder()
                    .ordens(ordens)
                    .dataSessao(dataSessao)
                    .build();

            ordens.forEach(ordem -> {
                if (ordem.getId() != null) {
                    repository.atualizar(ordemDia);
                } else {
                    repository.salvar(ordemDia);
                }
            });

        } catch (Exception e) {
            throw new DAOException("Erro ao salvar Ordens do dia: " + e.getLocalizedMessage());
        }
    }

    public List<OrdemDia> listarUltimos(Integer quantidade) {
        try {
            return repository.listarUltimos(quantidade);
        } catch (Exception e) {
            throw new DAOException("Erro ao Listar Ordens do dia: " + e.getLocalizedMessage());
        }
    }

    public void excluir(OrdemDia ordemDia) {
        try {
            repository.excluir(ordemDia);
        } catch (Exception e) {
            throw new DAOException("Erro ao Ordem dia: " + e.getLocalizedMessage());
        }
    }

    public List<Ordem> pesquisarSessao(LocalDate dataSessao) {
        try {
            var ordens = new ArrayList<Ordem>();

            var dataDTO = SessaoPlenariaService.buscaSessaoPorData(dataSessao);

            if (dataDTO.getResults().isEmpty()) {
                MensagensUtils.addWarnMessageFlashScoped("Não foi encontrada nenhuma ordem do dia para a data informada");
            } else {
                var URL = String.format(BASE_URL_ORDEM, dataDTO.getResults().get(0).getCodReuniao());

                Document document = Jsoup.connect(URL)
                        .header("User-Agent", "Mozilla/5.0") // Exemplo de User-Agent
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .timeout(30000)
                        .get();
                Element tabela = document.select("table").first();

                // Verifica se a tabela foi encontrada
                if (tabela != null) {

                    // Encontra todas as linhas (<tr>) na tabela
                    Elements linhas = tabela.select("tr");

                    // Itera sobre as linhas da tabela
                    for (Element linha : linhas) {
                        // Encontra todas as colunas (<td>) na linha
                        Elements colunas = linha.select("td");

                        // Verifica se a linha tem as colunas desejadas
                        if (colunas.size() >= 4) {
                            String ordem = colunas.get(0).select("a").text();

                            Element materiaCell = colunas.get(1);
                            Element link = materiaCell.selectFirst("a");
                            String idDoLink = link.attr("id");
                            String materia = link.text();
                            String autor = materiaCell.selectFirst("span[id^=autor] b:contains(Autor)").nextSibling().toString().trim();
                            String protocolo = materiaCell.selectFirst("b:contains(Protocolo)").nextSibling().toString().trim();
                            String turno = materiaCell.selectFirst("b:contains(Turno)").nextSibling().toString().trim();
                            String processo = materiaCell.selectFirst("b:contains(Processo)").nextSibling().toString().trim();

                            String ementa = colunas.get(2).select("div.dont-break-out").first().text();
                            String resultado = colunas.get(3).text();
                            
                            var ordemDia = new Ordem()
                                    .builder()
                                    .ordem(Integer.parseInt(ordem))
                                    .idMateria(idDoLink)
                                    .materia(materia)
                                    .processo(processo)
                                    .autor(autor)
                                    .protocolo(protocolo)
                                    .turno(turno)
                                    .ementa(ementa)
                                    .resultado(resultado)
                                    .turnoVotacao(TurnoVotacao.TURNO_UNICO)
                                    .build();
                            
                            var idMateriaPrincipal = new IdMateriaDTO();
                            var idMateriaAtual = new IdMateriaDTO();
                            
                            if(ordemDia.getMateria().contains("MENSAGEM GOVERNAMENTAL")){
                                idMateriaPrincipal = pesquisarMateriaPrincipal(Integer.valueOf(idDoLink));
                                idMateriaPrincipal = pesquisarAutoria(idMateriaPrincipal);
                                
                                idMateriaAtual.setId(idDoLink);
                                idMateriaAtual = pesquisarAutoria(idMateriaAtual);
                                
                                ordemDia.setVeto(Boolean.TRUE);
                                if(idMateriaPrincipal.getId() != null) ordemDia.setIdMateriaPrincipal(idMateriaPrincipal.getId());
                                if(idMateriaPrincipal.getAutor() != null) ordemDia.setAutorMateriaPrincipal(idMateriaPrincipal.getAutor());
                                if(idMateriaPrincipal.getMateria() != null) ordemDia.setMateriaMateriaPrincipal(idMateriaPrincipal.getMateria());
                                if(idMateriaPrincipal.getTipoAutor() != null) ordemDia.setTipoAutorMateriaPrincipal(idMateriaPrincipal.getTipoAutor());
                                if(idMateriaAtual.getTipoAutor() != null) ordemDia.setTipoAutor(idMateriaAtual.getTipoAutor());
                            }else{
                                idMateriaPrincipal.setId(idDoLink);
                                idMateriaPrincipal = pesquisarAutoria(idMateriaPrincipal);
                                ordemDia.setTipoAutor(idMateriaPrincipal.getTipoAutor());
                                ordemDia.setVeto(Boolean.FALSE);
                            }
                            
                            inserirValoresPadroesNaOrdem(ordemDia);
                            pesquisarRoteiroMateria(ordemDia);
                            ordens.add(ordemDia);
                        }
                    }
                    return ordens;
                } else {
                    System.out.println("Tabela não encontrada na página.");
                }
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            MensagensUtils.addErrorMessageFlashScoped("Erro ao salvar pesquisa!");
        }
        return null;
    }
    
    public IdMateriaDTO pesquisarMateriaPrincipal(Integer idMateria) {
        var idMateriaDTO = new IdMateriaDTO();
        try {
            var URL = String.format(BASE_URL_MATERIA, idMateria);
            Document document = Jsoup.connect(URL)
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .timeout(30000)
                .get(); 
            
            Element divMateriaPrincipal = document.getElementById("div_id_materia_anexada_set__materia_principal");
            Elements links = divMateriaPrincipal.select("a");
            
            for (Element link : links) {
                idMateriaDTO.setId(link.attr("href").split("/")[2]);
                idMateriaDTO.setMateria(link.text());
            }
        } catch (IOException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        return idMateriaDTO;
    }
    
    public IdMateriaDTO pesquisarAutoria(IdMateriaDTO idMateria) {
        try {
            var URL = String.format(BASE_URL_AUTORIA, Integer.valueOf(idMateria.getId()));
            Document document = Jsoup.connect(URL)
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .timeout(30000)
                .get(); 
            
            Element tabela = document.select("table").first();
            Elements linhas = tabela.select("tr");
            
            for (Element linha : linhas) {
                Elements colunas = linha.select("td");
                if(colunas.size() >=3 ){
                    idMateria.setAutor(colunas.get(0).select("a").text());
                    idMateria.setTipoAutor(colunas.get(1).text());
                }
            }
        } catch (IOException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        return idMateria;
    }

    public List<DocumentoAcessorio> pesquisarParecer(List<Ordem> ordens) {
        var documentos = new ArrayList<DocumentoAcessorio>();

        ordens.forEach(ordem -> {
            try {
                var idMateria = Integer.parseInt(ordem.getIdMateria());
                var URL = String.format(BASE_URL_DOC_ACESSORIO, idMateria);

                Document document = Jsoup.connect(URL)
                        .header("User-Agent", "Mozilla/5.0") // Exemplo de User-Agent
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .timeout(30000)
                        .get();
                Element tabela = document.select("table").first();

                var documento = new DocumentoAcessorio();
                documento.setOrdem(ordem);
                
                var links = new ArrayList<Link>();
                var contadorDeParecer = 0;
                
                if (tabela != null) {
                    Elements linhas = tabela.select("tr");
                    
                    for (Element linha : linhas) {
                        Elements colunas = linha.select("td");
                        if (colunas.size() >= 4) {
                            String nome = colunas.get(0).select("a").text();
                            String tipo = colunas.get(1).text();
                            String data = colunas.get(2).text();
                            String autor = colunas.get(3).text();
                            Element textoCell = colunas.get(4);
                            Element link = textoCell.selectFirst("a");
                            String linkTexto = link.attr("href");

                            if(verificarSeHaParecer(nome, tipo)){
                                var linkEntity = new Link();
                                linkEntity.setNome("PARECER CCJ");
                                linkEntity.setUrl(BASE_URL + linkTexto);
                                linkEntity.setParecer(Boolean.TRUE);
                                links.add(linkEntity);
                                contadorDeParecer++;
                            }
                            
                            if(tipo.equalsIgnoreCase("EMENDA")){
                                var linkEntity = new Link();
                                linkEntity.setNome(nome.toUpperCase());
                                linkEntity.setUrl(BASE_URL + linkTexto);
                                linkEntity.setParecer(Boolean.FALSE);
                                links.add(linkEntity);
                            }
                        }
                    }
                } else {
                    System.out.println("Tabela não encontrada na página.");
                }
                
                if(contadorDeParecer > 1){
                    var linkEncontrado = links.stream()
                            .filter(link -> link.getParecer().equals(true))
                            .findFirst()
                            .get();
                    links.removeIf(link -> link.getUrl().equals(linkEncontrado.getUrl()));
                }
                
                documento.setLinks(links);
                documentos.add(documento);
            } catch (IOException | NumberFormatException e) {
                System.out.println(e.getLocalizedMessage());
                MensagensUtils.addErrorMessageFlashScoped("Erro ao salvar pesquisa!");
            }
        });
        return documentos;
    }
    
    public DocumentoAcessorio pesquisarParecer(Ordem ordem) {
        //var documentos = new ArrayList<DocumentoAcessorio>();
        var documento = new DocumentoAcessorio();

        try {
            var idMateria = Integer.parseInt(ordem.getIdMateria());
            var URL = String.format(BASE_URL_DOC_ACESSORIO, idMateria);

            Document document = Jsoup.connect(URL)
                    .header("User-Agent", "Mozilla/5.0") // Exemplo de User-Agent
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .timeout(30000)
                    .get();
            Element tabela = document.select("table").first();

            documento.setOrdem(ordem);

            var links = new ArrayList<Link>();
            var contadorDeParecer = 0;

            if (tabela != null) {
                Elements linhas = tabela.select("tr");

                for (Element linha : linhas) {
                    Elements colunas = linha.select("td");
                    if (colunas.size() >= 4) {
                        String nome = colunas.get(0).select("a").text();
                        String tipo = colunas.get(1).text();
                        String data = colunas.get(2).text();
                        String autor = colunas.get(3).text();
                        Element textoCell = colunas.get(4);
                        Element link = textoCell.selectFirst("a");
                        String linkTexto = link.attr("href");

                        if(verificarSeHaParecer(nome, tipo)){
                            var linkEntity = new Link();
                            linkEntity.setNome("PARECER CCJ");
                            linkEntity.setUrl(BASE_URL + linkTexto);
                            linkEntity.setParecer(Boolean.TRUE);
                            links.add(linkEntity);
                            contadorDeParecer++;
                        }

                        if(tipo.equalsIgnoreCase("EMENDA")){
                            var linkEntity = new Link();
                            linkEntity.setNome(nome.toUpperCase());
                            linkEntity.setUrl(BASE_URL + linkTexto);
                            linkEntity.setParecer(Boolean.FALSE);
                            links.add(linkEntity);
                        }
                    }
                }
            } 
//            else {
//                System.out.println("Tabela não encontrada na página.");
//            }

            if(contadorDeParecer > 1){
                var linkEncontrado = links.stream()
                        .filter(link -> link.getParecer().equals(true))
                        .findFirst()
                        .get();
                links.removeIf(link -> link.getUrl().equals(linkEncontrado.getUrl()));
            }

            documento.setLinks(links);
        } catch (IOException | NumberFormatException e) {
            System.out.println(e.getLocalizedMessage());
            MensagensUtils.addErrorMessageFlashScoped("Erro ao salvar pesquisa!");
        }

        return documento;
    }

    private boolean verificarSeHaParecer(String nome, String tipo){
        var nomeParametro = "FAVORAVEL " + "NAO FAVORAVEL " + "PARECER FAVORAVEL - CCJ " + "FAVORAVEL - CCJ " + "PARECER DO RELATOR " + "FAVORAVEL COM EMENDA " + "PARECER CCJ ";
        var tipoParametro = "PARECER " + "PARECER DE COMISSAO " + "RELATORIO";
                
        nome = StringUtils.removerAcentos(nome.toUpperCase());
        tipo = StringUtils.removerAcentos(tipo.toUpperCase());
        
        return (nomeParametro.contains(nome) && tipoParametro.contains(tipo));
    }
    
    public void inserirValoresPadroesNaOrdem(Ordem ordem) {
        try {
            var statusMateria = repositoryStatusMateria.buscarNome("EM ANDAMENTO").orElse(null);
            ordem.setStatusMateria(statusMateria);
        } catch (NoResultException ex) {
        }

        try {
            var statusTramitacao = repositoryStatusTramitacao.buscarNome("DELIBERADO EM COMISSÃO").orElse(null);
            ordem.setStatusTramitacao(statusTramitacao);
        } catch (Exception e) {
        }

        try {
            var parecerProcuradoria = repositoryParecerProcuradoria.buscarNome("PELA CONSTITUCIONALIDADE").orElse(null);
            ordem.setParecerProcuradoria(parecerProcuradoria);
        } catch (Exception e) {
        }

    }
    
    public void pesquisarRoteiroMateria(Ordem ordem){
        try {
            var nomePesquisa = "";
            if(ordem.getMateria().contains("º")){
                nomePesquisa = ordem.getMateria().toUpperCase().split("Nº")[0].trim();
            }
            if(ordem.getMateria().contains("°")){
                nomePesquisa = ordem.getMateria().toUpperCase().split("N°")[0].trim();
            }
            
            var roteiroMateria = repositoryRoteiroMateria.buscarPorTipoMateriaNome(nomePesquisa).orElse(null);
            ordem.setRoteiroMateria(roteiroMateria);
        } catch (Exception e) {
        }
    }
    
    public void verficarSeEVeto(Ordem ordemDia){
        if(ordemDia.getMateria().toUpperCase().contains("MENSAGEM GOVERNAMENTAL")){
            ordemDia.setVeto(Boolean.TRUE);
        }else{
            ordemDia.setVeto(Boolean.FALSE);
        }
    }
    
    public void organizarSequenciaDosItens(List<Ordem> ordens, Ordem ordem){
        if((ordem.getOrdem() - 1) == ordens.size()){
            ordens.add(ordem);
        }else if((ordem.getOrdem() - 1) > ordens.size()){
            ordem.setOrdem(ordens.size() + 1);
            ordens.add(ordem);
        }else{
            ordens.add(ordem.getOrdem() - 1, ordem);
            
            for (int i = 0; i < ordens.size(); i++) {
                ordens.get(i).setOrdem(i + 1);
            }
        }
    }
    
    public void removerItem(List<Ordem> ordens, Integer item){
        var result = ordens.stream().filter(ordem -> ordem.getOrdem().equals(item)).findFirst().orElse(null);
        
        if(result != null){
            ordens.removeIf(ordem -> ordem.getOrdem().equals(item));
//            try {
                var doc = roteiroRepository.buscarDocumentoAcessorioPorOrdem(result);
                roteiroRepository.excluirLinkPorDocumentoAcessorio(doc);
                roteiroRepository.excluirDocumentoAcessorioPorIdOrdem(result.getId());
//            } catch (Exception e) {
//            }
        }
        
        for (int i = 0; i < ordens.size(); i++) {
            ordens.get(i).setOrdem(i + 1);
        }
    }
    
    public void adicionarEtiqueta(List<Ordem> ordens, Integer item, String etiqueta, String corEtiqueta){
        var result = ordens.stream().filter(ordem -> ordem.getOrdem().equals(item)).findFirst().orElse(null);
        
        if(result != null){
            if(!etiqueta.isBlank() && !etiqueta.isEmpty() && etiqueta != null){
                result.setEtiqueta(etiqueta);
                result.setCorEtiqueta(corEtiqueta);
            }
            else result.setEtiqueta(null);
        }
        
    }
    
    public void salvarDocumentoAcessorio(Long idOrdem, Long idRoteiroSecretario) {
        try {
            roteiroRepository.salvarDocumentoAcessorio(idOrdem, idRoteiroSecretario);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar Ordens do dia: " + e.getLocalizedMessage());
        }
    }

    public void pesquisarMateria(Ordem ordem, String tipoMateria, String numeroMateria, String ano) {
        try {
            var URL = String.format(BASE_URL_PESQUISA_MATERIA, tipoMateria, numeroMateria, ano);
            Document doc = Jsoup.connect(URL)
                    .header("User-Agent", "Mozilla/5.0") // Exemplo de User-Agent
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .timeout(30000)
                    .get();
            
            Element table = doc.select("table").first();
            Element linkElement = table.select("a[href^=/materia/]").first();
            Element ementaElement = table.select("strong:contains(Ementa)").first().nextElementSibling();
            Element autorElement = table.select("strong:contains(Autor)").first();
            
            //ID MATERIA
            String link = linkElement.attr("href");
            
            //MATERIA
            String linkText = linkElement.text();
            var result1 = linkText.split(" - ");
            var result2 = result1[0].split(" ");
            var result3 = result2[1].split("/");
            var nomePrimeiraParte = result1[1];
            var nomeSegundaParte = result3[0];
            var nomeTerceiraParte = result3[1];
            
            var materia = (nomePrimeiraParte + " nº " + nomeSegundaParte + " de " + nomeTerceiraParte).toUpperCase();
            var idMateria = link.replace("/materia/", "");
            var ementa = ementaElement.text();
            var autor = autorElement.nextSibling().toString().replace("&nbsp;", "");
 
            ordem.setIdMateria(idMateria);
            ordem.setMateria(materia);
            ordem.setEmenta(ementa);
            ordem.setAutor(autor);
            
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            MensagensUtils.addErrorMessageFlashScoped("Erro ao salvar pesquisa!");
        }
    }
}
