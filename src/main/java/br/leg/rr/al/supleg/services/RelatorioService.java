package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TurnoVotacao;
import static br.leg.rr.al.supleg.enums.TurnoVotacao.TURNO_1;
import static br.leg.rr.al.supleg.enums.TurnoVotacao.TURNO_UNICO;
import br.leg.rr.al.supleg.models.MesaModelo;
import br.leg.rr.al.supleg.models.Ordem;
import br.leg.rr.al.supleg.models.Roteiro;
import br.leg.rr.al.supleg.models.SessaoLegislativa;
import br.leg.rr.al.supleg.utils.StringUtils;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.BorderRadius;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.ejb.Asynchronous;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Heliton
 */
@ViewScoped
public class RelatorioService implements Serializable {

    private static final long serialVersionUID = 1L;

    private ByteArrayOutputStream baos;
    private final HttpServletResponse response;
    private final FacesContext context;
    private PdfFont pdfFontVerdanaRegular;
    private PdfFont pdfFontVerdanaBold;

    //__________________________________________________________________________
    public RelatorioService() {
        this.context = FacesContext.getCurrentInstance();
        this.response = (HttpServletResponse) context.getExternalContext().getResponse();

        configurarFontes();
    }

    @Asynchronous
    public void gerarRelatorioOrdemDoDia(Roteiro roteiro) throws IOException {
        // PRE-CONFIGURACOES
        baos = new ByteArrayOutputStream();
        var writer = new PdfWriter(baos);
        var pdfDocument = new PdfDocument(writer);
        var document = new Document(pdfDocument, PageSize.A4);
        document.setFont(pdfFontVerdanaRegular);

        // Adicionando evento para adicionar cabeçalho em todas as páginas
        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new HeaderEventHandler(document));
        document.setMargins(110, 60, 50, 60);

        criarCabecalho1(document);
        criarCabecalho2(pdfDocument, document);

        var dataSessao = roteiro.getDataSessao();
        var dataMes = (dataSessao.getMonthValue() < 10 ? "0" + dataSessao.getMonthValue() : dataSessao.getMonthValue()) + "";
        var dataSessao1 = dataSessao.getDayOfMonth() + "/" + dataMes + "/" + dataSessao.getYear();
        var dataSessao2 = dataSessao.getDayOfMonth() + "-" + dataMes + "-" + dataSessao.getYear();

        
        criarTituloOrdemDia(document, dataSessao1);
        
        roteiro.getOrdemDia().getOrdens().sort(Comparator.comparingInt(ordem -> ordem.getOrdem()));
        List<Ordem> ordensVotacao1Turno = new ArrayList<>();
        List<Ordem> ordensVotacao2Turno = new ArrayList<>();
        List<Ordem> ordensVotacaoTurnoUnico = new ArrayList<>();

        roteiro.getOrdemDia().getOrdens().forEach(ordem -> {
            switch (ordem.getTurnoVotacao()) {
                case TURNO_UNICO -> ordensVotacaoTurnoUnico.add(ordem);
                case TURNO_1 -> ordensVotacao1Turno.add(ordem);
                default -> ordensVotacao2Turno.add(ordem);
            }
        });
        
        if(!ordensVotacao2Turno.isEmpty()){
            criarTituloVotacao(document, TurnoVotacao.TURNO_2);
            criarTabelasOrdens(document, ordensVotacao2Turno);
        }
        
        if(!ordensVotacao1Turno.isEmpty()){
            criarTituloVotacao(document, TurnoVotacao.TURNO_1);
            criarTabelasOrdens(document, ordensVotacao1Turno);
        }
        
        if(!ordensVotacaoTurnoUnico.isEmpty()){
            criarTituloVotacao(document, TurnoVotacao.TURNO_UNICO);
            criarTabelasOrdens(document, ordensVotacaoTurnoUnico);
        }
        
        String nomeArquivo = "ordem-do-dia-" + dataSessao2;
        document.close();
        response(baos, nomeArquivo);
    }

    @Asynchronous
    public void gerarRelatorioRoteiro(Roteiro roteiro, MesaModelo mesa, SessaoLegislativa sessaoLegislativa) throws IOException {
        // PRE-CONFIGURACOES
        baos = new ByteArrayOutputStream();
        var writer = new PdfWriter(baos);
        var pdfDocument = new PdfDocument(writer);
        var document = new Document(pdfDocument, PageSize.A4);
        document.setFont(pdfFontVerdanaRegular);

        // Adicionando evento para adicionar cabeçalho em todas as páginas
        //pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new HeaderEventHandler(document));
        document.setMargins(110, 60, 50, 60);

        criarCabecalho1(document);
        criarCabecalho2(pdfDocument, document);

        var dataSessao = roteiro.getDataSessao();
        var dataMes = (dataSessao.getMonthValue() < 10 ? "0" + dataSessao.getMonthValue() : dataSessao.getMonthValue()) + "";
        var dataSessao1 = dataSessao.getDayOfMonth() + "/" + dataMes + "/" + dataSessao.getYear();
        var dataSessao2 = dataSessao.getDayOfMonth() + "-" + dataMes + "-" + dataSessao.getYear();
        
        // Criando um formatter personalizado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
        // Formatando a data
        String dataFormatada = roteiro.getDataSessao().format(formatter);

        criarTituloRoteiro(document, dataFormatada);
        criarTabelaMesaDiretora(document, mesa);
        criarSeparador(document);
        criarTituloSessao(document, roteiro, sessaoLegislativa, dataFormatada);
        criarSeparador(document);
        criarAbertura(document, roteiro);
        criarSeparador(document);
        
        if(roteiro.getOrdemDia() != null){
            criarTituloOrdemDia(document, dataSessao1);
            
            roteiro.getOrdemDia().getOrdens().sort(Comparator.comparingInt(ordem -> ordem.getOrdem()));
            List<Ordem> ordensVotacao1Turno = new ArrayList<>();
            List<Ordem> ordensVotacao2Turno = new ArrayList<>();
            List<Ordem> ordensVotacaoTurnoUnico = new ArrayList<>();

            roteiro.getOrdemDia().getOrdens().forEach(ordem -> {
                switch (ordem.getTurnoVotacao()) {
                    case TURNO_UNICO -> ordensVotacaoTurnoUnico.add(ordem);
                    case TURNO_1 -> ordensVotacao1Turno.add(ordem);
                    default -> ordensVotacao2Turno.add(ordem);
                }
            });

            if(!ordensVotacao2Turno.isEmpty()){
                criarTituloVotacao(document, TurnoVotacao.TURNO_2);
                criarTabelasOrdensRoteiro(document, ordensVotacao2Turno);
            }

            if(!ordensVotacao1Turno.isEmpty()){
                criarTituloVotacao(document, TurnoVotacao.TURNO_1);
                criarTabelasOrdensRoteiro(document, ordensVotacao1Turno);
            }

            if(!ordensVotacaoTurnoUnico.isEmpty()){
                criarTituloVotacao(document, TurnoVotacao.TURNO_UNICO);
                criarTabelasOrdensRoteiro(document, ordensVotacaoTurnoUnico);
            }
        }
        
        //criarSeparador(document);
        criarFechamento(document, roteiro);
        
        String nomeArquivo = "roteiro-da-sessao-" + dataSessao2;
        document.close();
        response(baos, nomeArquivo);
    }
    //__________________________________________________________________________
    
    private void criarTituloOrdemDia(Document document, String data){
        var tableData = new Table(1).useAllAvailableWidth();
        var cellTableData = new Cell().add(new Paragraph("ORDEM DO DIA " + data).setFont(pdfFontVerdanaBold));
        cellTableData.setBackgroundColor(new DeviceRgb(229, 229, 229));
        cellTableData.setTextAlignment(TextAlignment.CENTER);
        tableData.addCell(cellTableData);
        tableData.setMarginTop(30);
        tableData.setMarginBottom(10);
        document.add(tableData);
    }
    
    private void criarTituloRoteiro(Document document, String dataFormatada){
        Text texto1 = new Text("ROTEIRO").setFont(pdfFontVerdanaBold);
        Text texto2 = new Text("DE").setFont(pdfFontVerdanaBold);
        Text textoData = new Text(dataFormatada).setFont(pdfFontVerdanaBold).setFontColor(new DeviceRgb(255, 0, 0));
        Text textoArtigo = new Text("Art. 136, II, do Regimento Interno").setFont(pdfFontVerdanaBold).setUnderline();
        
        Paragraph paragrafo = new Paragraph();
            paragrafo
                .add(texto1)
                .add("\n")
                .add(texto2)
                .add("\n")
                .add(textoData)
                .add("\n")
                .add(textoArtigo)
                .setFont(pdfFontVerdanaBold)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(paragrafo);
    }
    
    private void criarTituloSessao(Document document, Roteiro roteiro, SessaoLegislativa sessaoLegislativa, String dataFormatada){
        Table table1 = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        Text nomeParte1 = new Text(roteiro.getNomeParte1().toUpperCase() + " ").setFontColor(new DeviceRgb(255,0,0)).setFont(pdfFontVerdanaBold);
        Text nomeParte2 = new Text(roteiro.getNomeParte2().toUpperCase()).setFont(pdfFontVerdanaBold);
        Text nomeParte2b = new Text(" DA").setFont(pdfFontVerdanaBold);
        
        Text sessao = new Text(sessaoLegislativa.getSessaoLegislativa().toString() + "ª").setFontColor(new DeviceRgb(255,0,0)).setFont(pdfFontVerdanaBold);
        Text sessaoB = new Text(" SESSÃO LEGISLATIVA ORDINÁRIA DA").setFont(pdfFontVerdanaBold);
        Text legislatura = new Text(sessaoLegislativa.getLegislatura().toString() + "ª").setFontColor(new DeviceRgb(255,0,0)).setFont(pdfFontVerdanaBold);
        Text legislaturab = new Text(" LEGISLATURA, EM ").setFont(pdfFontVerdanaBold);
        Text data = new Text(dataFormatada.toUpperCase()).setFont(pdfFontVerdanaBold);
        
        Cell x = new Cell().add(
                new Paragraph()
                        .add(nomeParte1)
                        .add(nomeParte2).add(nomeParte2b)
                        .add("\n")
                        .add(sessao).add(sessaoB)
                        .add("\n")
                        .add(legislatura).add(legislaturab)
                        .add(data)
                        .setMargin(0)
                        .setTextAlignment(TextAlignment.CENTER)
        )
        .setBackgroundColor(new DeviceRgb(207,226,243))
        .setBorder(Border.NO_BORDER)
        .setPadding(3);
        table1.addCell(x);
        table1.setMarginTop(10);
        document.add(table1);
    }
    
    private void criarAbertura(Document document, Roteiro roteiro){
        var abertura = new StringBuilder();
        roteiro.getModeloRoteiro().getAbertura().getTrechos().forEach(trecho -> {
            abertura.append(trecho.getTexto());
        });
        
        Paragraph paragrafo = preFormatacaoAbertura(abertura.toString());
        paragrafo.setTextAlignment(TextAlignment.CENTER)
        .setFont(pdfFontVerdanaBold);
        document.add(paragrafo);
    }
    
    private void criarFechamento(Document document, Roteiro roteiro){
        roteiro.getModeloRoteiro().getFechamento().getTrechos().forEach(trecho -> {
            Paragraph paragrafo0 = new Paragraph();
            
            Paragraph paragrafo = preFormatacaoFechamento(trecho.getTexto()).setBackgroundColor(hexToRgb(trecho.getCorDeFundo()));
            paragrafo.setTextAlignment(TextAlignment.CENTER).setFont(pdfFontVerdanaBold);
            
            document.add(paragrafo0);
            document.add(paragrafo);
        });
    }
    
    private Paragraph preFormatacaoAbertura(String html){
        var trechos = StringUtils.removerTagsHtml(html, "\n").split("\n");
        
        Paragraph paragrafo = new Paragraph();
        
        for (int i = 0; i < trechos.length; i++) {
            if(trechos[i].toUpperCase().contains("APROVADA")){
                Text t = new Text(trechos[i]).setUnderline().setFontColor(new DeviceRgb(56, 118, 29));
                paragrafo.add(t).add("\n").add("\n");
            }else if(!trechos[i].isEmpty()){
                paragrafo.add(trechos[i]).add("\n").add("\n");
            }
        }
        
        return paragrafo;
    }
    
    private Paragraph preFormatacaoFechamento(String html){
        var trechos = StringUtils.removerTagsHtml(html, "\n").split("\n");
        
        Paragraph paragrafo = new Paragraph();
        paragrafo.add("\n");

        for (int i = 0; i < trechos.length; i++) {
            if(!trechos[i].isEmpty()){
                paragrafo.add(trechos[i]).add("\n");
            }
        }
                
        paragrafo.add("\n");
        
        return paragrafo;
    }
    
    private Paragraph preFormatacaoRoteiroMateria(String html, String nomeMateria) {
        var trechos = StringUtils.removerTagsHtml(html, "")
                .replace("&#xfeff;", "")
                .replace(".", ".\n")
                .split("\n");

        Paragraph paragrafo = new Paragraph();
        var temVotacaoNominal = false;
        var temVotacaoSimbolica = false;
        
        for (int i = 0; i < trechos.length; i++) {
            if (!trechos[i].isEmpty()) {
                if (trechos[i].toUpperCase().contains("A VOTAÇÃO SERÁ NOMINAL/ELETRÔNICA") && !nomeMateria.toUpperCase().contains("MENSAGEM")) {
                    Text texto1 = new Text(trechos[i]).setFont(pdfFontVerdanaBold).setBackgroundColor(new DeviceRgb(204, 232, 204));
                    paragrafo.add(texto1).add("\n");
                    temVotacaoNominal = true;
                    
                } else if (trechos[i].toUpperCase().contains("A VOTAÇÃO SERÁ SIMBÓLICA")) {
                    Text texto1 = new Text(trechos[i]).setFont(pdfFontVerdanaBold).setBackgroundColor(new DeviceRgb(255, 194, 102));
                    paragrafo.add(texto1).add("\n");
                    temVotacaoSimbolica = true;
                    
                } else if (
                        trechos[i].toUpperCase().contains("VOTANDO “SIM”, OS DEPUTADOS APROVAM A MATÉRIA, VOTANDO “NÃO”") ||
                        trechos[i].toUpperCase().contains("VOTANDO “SIM”, OS DEPUTADOS MANTÉM O VETO, VOTANDO “NÃO”")
                    ) {
                    var tt1 = trechos[i].split("“SIM”");
                    var tt2 = tt1[1].split("“NÃO”");

                    Text texto1 = new Text("* " + tt1[0] + "“");
                    Text texto2 = new Text("SIM").setBold().setUnderline().setFontColor(new DeviceRgb(56, 118, 29)).setFont(pdfFontVerdanaBold);
                    Text texto3 = new Text("”" + tt2[0] + "“");
                    Text texto4 = new Text("NÃO").setBold().setUnderline().setFontColor(new DeviceRgb(204, 0, 0)).setFont(pdfFontVerdanaBold);
                    Text texto5 = new Text("”" + tt2[1]);

                    if(temVotacaoNominal && !nomeMateria.toUpperCase().contains("MENSAGEM")) {
                        texto1.setBackgroundColor(new DeviceRgb(204, 232, 204));
                        texto2.setBackgroundColor(new DeviceRgb(204, 232, 204));
                        texto3.setBackgroundColor(new DeviceRgb(204, 232, 204));
                        texto4.setBackgroundColor(new DeviceRgb(204, 232, 204));
                        texto5.setBackgroundColor(new DeviceRgb(204, 232, 204));
                    }
                    
                    paragrafo.add(texto1).add(texto2).add(texto3).add(texto4).add(texto5);
                    
                    if(temVotacaoNominal) {
                        paragrafo.add("\n");
                    }else{
                        paragrafo.add("\n").add("\n");
                    }
                    
                } else if (trechos[i].contains("{NOME-MATERIA}")) {
                    var tt0 = trechos[i].replace("{NOME-MATERIA}.", "");
                    var tt1 = tt0.split("APROVADO");
                    
                    var tt2 = tt1[1].split("REJEITADO");

                    Text texto1 = new Text("* " + tt1[0]);
                    Text texto2 = new Text("APROVADO").setBold().setUnderline().setFontColor(new DeviceRgb(56, 118, 29)).setFont(pdfFontVerdanaBold);
                    Text texto3 = new Text(" " + tt2[0] + " ");
                    Text texto4 = new Text("REJEITADO").setBold().setUnderline().setFontColor(new DeviceRgb(204, 0, 0)).setFont(pdfFontVerdanaBold);
                    Text texto5 = new Text(tt2[1]);
                    Text texto6 = new Text(nomeMateria.toUpperCase() + ".").setFont(pdfFontVerdanaBold);

                    paragrafo.add(texto1).add(texto2).add(texto3).add(texto4).add(texto5).add(texto6).add("\n");
                    
                }  
                else {
                    if(temVotacaoNominal) {
                        Text texto1 = new Text("* " + trechos[i]).setBackgroundColor(new DeviceRgb(204, 232, 204));
                        paragrafo.add(texto1).add("\n").add("\n");
                        temVotacaoNominal = false;
                    }else if(temVotacaoSimbolica) {
                        Text texto1 = new Text("* " + trechos[i]).setBackgroundColor(new DeviceRgb(255, 194, 102));
                        paragrafo.add(texto1).add("\n").add("\n");
                        temVotacaoSimbolica = false;
                    }else {
                        paragrafo.add("* " + trechos[i]).add("\n").add("\n");
                    }
                }
            }
        }
        return paragrafo;
    }
    
    private void criarSeparador(Document document){
        Paragraph paragrafo = new Paragraph();
        paragrafo.add("_________________________________________________").setTextAlignment(TextAlignment.CENTER);
        document.add(paragrafo);
    }
    
    private void criarTabelaMesaDiretora(Document document, MesaModelo mesa){
        Paragraph paragrafo = new Paragraph();
            paragrafo
                .add("MESA DIRETORA")
                .setFontColor(new DeviceRgb(255, 0, 0)).setBold().setUnderline()
                .setFont(pdfFontVerdanaBold)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10);
        document.add(paragrafo);
        
        Table table1 = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        Text t1a = new Text(mesa.getPresidente().getNome()).setFontSize(8).setFont(pdfFontVerdanaBold);
        Text t1b = new Text("Presidente").setFontSize(8);
        Cell x = new Cell().add(
                new Paragraph()
                        .add(t1a)
                        .add("\n")
                        .add(t1b)
                        .setMargin(0)
                        .setTextAlignment(TextAlignment.CENTER)
        )
        .setPadding(3);
        table1.addCell(x);
        table1.setMarginTop(10);
        document.add(table1);
        
        Table table2 = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        Text t2a = new Text(mesa.getVicePresidente1().getNome()).setFontSize(8).setFont(pdfFontVerdanaBold);
        Text t2b = new Text("1º Vice-Presidente").setFontSize(8);
        Cell y1 = new Cell().add(
                new Paragraph()
                        .add(t2a)
                        .add("\n")
                        .add(t2b)
                        .setMargin(0)
                        .setTextAlignment(TextAlignment.CENTER)
        )
        .setBorderTop(Border.NO_BORDER)
        .setPadding(3);
        table2.addCell(y1);
                
        Text t3a = new Text(mesa.getVicePresidente2().getNome()).setFontSize(8).setFont(pdfFontVerdanaBold);
        Text t3b = new Text("2º Vice-Presidente").setFontSize(8);
        Cell y2 = new Cell().add(
                new Paragraph()
                        .add(t3a)
                        .add("\n")
                        .add(t3b)
                        .setMargin(0)
                        .setTextAlignment(TextAlignment.CENTER)
        )
        .setBorderTop(Border.NO_BORDER)
        .setPadding(3);
        table2.addCell(y2);
             
        Text t4a = new Text(mesa.getVicePresidente3().getNome()).setFontSize(8).setFont(pdfFontVerdanaBold);
        Text t4b = new Text("3º Vice-Presidente").setFontSize(8);
        Cell y3 = new Cell().add(
                new Paragraph()
                        .add(t4a)
                        .add("\n")
                        .add(t4b)
                        .setMargin(0)
                        .setTextAlignment(TextAlignment.CENTER)
        )
        .setBorderTop(Border.NO_BORDER)
        .setPadding(3);
        table2.addCell(y3);
        document.add(table2);
        
        Table table3 = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        Text t5a = new Text(mesa.getSecretario1().getNome()).setFontSize(8).setFont(pdfFontVerdanaBold);
        Text t5b = new Text("1º Secretário").setFontSize(8);
        Cell z1 = new Cell().add(
                new Paragraph()
                        .add(t5a)
                        .add("\n")
                        .add(t5b)
                        .setMargin(0)
                        .setTextAlignment(TextAlignment.CENTER)
        )
        .setBorderTop(Border.NO_BORDER)
        .setPadding(3);
        
        Text t6a = new Text(mesa.getSecretario2().getNome()).setFontSize(8).setFont(pdfFontVerdanaBold);
        Text t6b = new Text("2º Secretário").setFontSize(8);
        Cell z2 = new Cell().add(
                new Paragraph()
                        .add(t6a)
                        .add("\n")
                        .add(t6b)
                        .setMargin(0)
                        .setTextAlignment(TextAlignment.CENTER)
        )
        .setBorderTop(Border.NO_BORDER)
        .setPadding(3);
        
        Text t7a = new Text(mesa.getSecretario3().getNome()).setFontSize(8).setFont(pdfFontVerdanaBold);
        Text t7b = new Text("3º Secretário").setFontSize(8);
        Cell z3 = new Cell().add(
                new Paragraph()
                        .add(t7a)
                        .add("\n")
                        .add(t7b)
                        .setMargin(0)
                        .setTextAlignment(TextAlignment.CENTER)
        )
        .setBorderTop(Border.NO_BORDER)
        .setPadding(3);
        
        Text t8a = new Text(mesa.getSecretario4().getNome()).setFontSize(8).setFont(pdfFontVerdanaBold);
        Text t8b = new Text("4º Secretário").setFontSize(8);
        Cell z4 = new Cell().add(
                new Paragraph()
                        .add(t8a)
                        .add("\n")
                        .add(t8b)
                        .setMargin(0)
                        .setTextAlignment(TextAlignment.CENTER)
        )
        .setBorderTop(Border.NO_BORDER)
        .setPadding(3);
        table3.addCell(z1);
        table3.addCell(z2);
        table3.addCell(z3);
        table3.addCell(z4);
        document.add(table3);
        
        Table table4 = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        Text t9a = new Text(mesa.getCorregedor().getNome()).setFontSize(8).setFont(pdfFontVerdanaBold);
        Text t9b = new Text("Corregedor").setFontSize(8);
        Cell w = new Cell().add(
                new Paragraph()
                        .add(t9a)
                        .add("\n")
                        .add(t9b)
                        .setMargin(0)
                        .setTextAlignment(TextAlignment.CENTER)
        )
        .setBorderTop(Border.NO_BORDER)
        .setPadding(3);
        
        table4.addCell(w);
        document.add(table4);
    }
    
    private void criarTituloVotacao(Document document, TurnoVotacao turnoVotacao){
        // Texto com destaque
        String text = turnoVotacao.getDescricao().toUpperCase();
        // Criando o parágrafo com texto destacado
        Paragraph paragraph = new Paragraph(text)
                .setFont(pdfFontVerdanaBold)
                .setUnderline()
                .setFontSize(12)
                .setPadding(0)
                .setMargin(0)
                .setTextAlignment(TextAlignment.CENTER);

        // Criando o div com texto destacado
        Div div = new Div();
                div.setBackgroundColor(new DeviceRgb(255, 255, 0));
                div.add(paragraph)
                .setHeight(18); 

        // Definindo a largura máxima do div com base no tamanho do texto
        div.setMaxWidth(pdfFontVerdanaBold.getWidth(text, 12));
        div.setHorizontalAlignment(HorizontalAlignment.CENTER);
        div.setMarginBottom(10);
        
        document.add(div);
    }
    
    private void criarTabelasOrdens(Document document, List<Ordem> ordens){
        //LOOP DA ORDEM DO DIA
        ordens.forEach(ordem -> {
            //TABELA DA ORDEM DO DIA
            Table tableOrdem = new Table(1).useAllAvailableWidth();

            Text textoItem = new Text("");
            textoItem.setText("ITEM " + ordem.getOrdemCompleta() + ": " + ordem.getNomeMateria());
            textoItem.setFont(pdfFontVerdanaBold);
            
            Text textoAutoria = new Text("");
            textoAutoria.setText("AUTORIA: " + ordem.getNomeAutor());
            textoAutoria.setFontColor(new DeviceRgb(0, 112, 192));
            textoAutoria.setFont(pdfFontVerdanaBold);
            
            Text textoObservacao = new Text("");
            textoObservacao.setText("OBSERVAÇÃO: " + ordem.getEtiquetasRelatorio());
            textoObservacao.setFontColor(new DeviceRgb(255, 0, 0));
            textoObservacao.setFont(pdfFontVerdanaBold);

            Paragraph paragrafoItem = new Paragraph();
            paragrafoItem
                    .add(textoItem)
                    .add("\n")
                    .add(textoAutoria)
                    .setFont(pdfFontVerdanaRegular);
            
            if(!ordem.getEtiquetasRelatorio().isBlank() && !ordem.getEtiquetasRelatorio().isEmpty()){
                paragrafoItem.add("\n").add(textoObservacao);
            }
            
            Cell cellTableOrdem1 = new Cell().add(paragrafoItem).setPadding(3);

            Text textoEmenta = new Text("Ementa: ").setFont(pdfFontVerdanaBold);
            Paragraph paragrafoEmenta = new Paragraph();
            Cell cellTableOrdem2 = new Cell().add(paragrafoEmenta
                    .add(textoEmenta)
                    .add(ordem.getEmenta())
                    .setTextAlignment(TextAlignment.JUSTIFIED)
                    .setFont(pdfFontVerdanaRegular)
            ).setBorder(Border.NO_BORDER);

            Text textoSituacaoAtual = new Text("Situação Atual: ").setFont(pdfFontVerdanaBold);
            Paragraph paragrafoSituacaoAtual = new Paragraph();
            Cell cellTableOrdem3 = new Cell().add(paragrafoSituacaoAtual
                    .add(textoSituacaoAtual)
                    .add(ordem.getStatusTramitacao().getNome())
                    .setFont(pdfFontVerdanaRegular)
            ).setBorder(Border.NO_BORDER);

            Text textoParecerProcuradoria = new Text("Parecer da Procuradoria: ").setFont(pdfFontVerdanaBold);
            Paragraph paragrafoParecerProcuradoria = new Paragraph();
            Cell cellTableOrdem4 = new Cell().add(paragrafoParecerProcuradoria
                    .add(textoParecerProcuradoria)
                    .add(ordem.getParecerProcuradoria().getNome())
                    .setFont(pdfFontVerdanaRegular)
            ).setBorder(Border.NO_BORDER);

            tableOrdem.addCell(cellTableOrdem1);
            tableOrdem.addCell(cellTableOrdem2);
            tableOrdem.addCell(cellTableOrdem3);
            tableOrdem.addCell(cellTableOrdem4);
            document.add(tableOrdem);

            Paragraph paragrafoSeparador = new Paragraph("**************************************************************").setFont(pdfFontVerdanaRegular);
            document.add(paragrafoSeparador);
        });
    }

    private void criarTabelasOrdensRoteiro(Document document, List<Ordem> ordens){
        //LOOP DA ORDEM DO DIA
        ordens.forEach(ordem -> {
            //TABELA DA ORDEM DO DIA
            Table tableOrdem = new Table(1).useAllAvailableWidth();

            Text textoItem = new Text("");
            textoItem.setText("ITEM " + ordem.getOrdemCompleta() + ": " + ordem.getNomeMateria());
            textoItem.setFont(pdfFontVerdanaBold);
            
            Text textoAutoria = new Text("");
            textoAutoria.setText("AUTORIA: " + ordem.getNomeAutor());
            textoAutoria.setFontColor(new DeviceRgb(0, 112, 192));
            textoAutoria.setFont(pdfFontVerdanaBold);
            
            Text textoAndamento = new Text("");
            textoAndamento.setText(ordem.getStatusMateria().getNome());
            //textoAndamento.setBackgroundColor(hexToRgb(ordem.getStatusMateria().getBackground()));
            textoAndamento.setBorderRadius(new BorderRadius(4));
            textoAndamento.setFont(pdfFontVerdanaBold);
            
            Text textoObservacao = new Text("");
            textoObservacao.setText("OBSERVAÇÃO: " + ordem.getEtiquetasRelatorio());
            textoObservacao.setFontColor(new DeviceRgb(255, 0, 0));
            textoObservacao.setFont(pdfFontVerdanaBold);

            Paragraph paragrafoItem = new Paragraph();
            paragrafoItem
                    .add(textoItem)
                    .add("\n")
                    .add(textoAutoria)
                    .setFont(pdfFontVerdanaRegular);
            
            Paragraph paragrafoAndamento = new Paragraph();
            paragrafoAndamento.add(textoAndamento);
            
            if(!ordem.getEtiquetasRelatorio().isBlank() && !ordem.getEtiquetasRelatorio().isEmpty()){
                paragrafoItem.add("\n").add(textoObservacao);
            }
            
            Cell cellTableOrdem1 = new Cell().add(paragrafoItem).setPadding(3);
            Cell cellTableStatusMateria = new Cell().add(paragrafoAndamento).setPadding(3);

            Text textoEmenta = new Text("Ementa: ").setFont(pdfFontVerdanaBold);
            Paragraph paragrafoEmenta = new Paragraph();
            Cell cellTableOrdem2 = new Cell().add(paragrafoEmenta
                    .add(textoEmenta)
                    .add(ordem.getEmenta())
                    .setTextAlignment(TextAlignment.JUSTIFIED)
                    .setFont(pdfFontVerdanaRegular)
            ).setBorder(Border.NO_BORDER);

            Text textoSituacaoAtual = new Text("Situação Atual: ").setFont(pdfFontVerdanaBold);
            Paragraph paragrafoSituacaoAtual = new Paragraph();
            Cell cellTableOrdem3 = new Cell().add(paragrafoSituacaoAtual
                    .add(textoSituacaoAtual)
                    .add(ordem.getStatusTramitacao().getNome())
                    .setFont(pdfFontVerdanaRegular)
            ).setBorder(Border.NO_BORDER);

            Text textoParecerProcuradoria = new Text("Parecer da Procuradoria: ").setFont(pdfFontVerdanaBold);
            Paragraph paragrafoParecerProcuradoria = new Paragraph();
            Cell cellTableOrdem4 = new Cell().add(paragrafoParecerProcuradoria
                    .add(textoParecerProcuradoria)
                    .add(ordem.getParecerProcuradoria().getNome())
                    .setFont(pdfFontVerdanaRegular)
            )
            .setBorder(Border.NO_BORDER);

            //Text textoParecerProcuradoria = new Text("Parecer da Procuradoria: ").setFont(pdfFontVerdanaBold);
            Paragraph paragrafo = new Paragraph();
            paragrafo.add("_________________________________________________")
                    .setMarginTop(-10)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(paragrafo);
        
            Paragraph roteiroMateria = null;
            Cell cellTableOrdem5 = null;
            if(ordem.getRoteiroMateria() != null && ordem.getMateria() != null){
                roteiroMateria = preFormatacaoRoteiroMateria(ordem.getRoteiroMateria().getTexto(), ordem.getMateria());
                cellTableOrdem5 = new Cell().add(paragrafo).add(roteiroMateria.setFont(pdfFontVerdanaRegular)).setBorder(Border.NO_BORDER);
            }
            
            
            tableOrdem.addCell(cellTableOrdem1).setMarginTop(5);
            tableOrdem.addCell(cellTableStatusMateria);
            tableOrdem.addCell(cellTableOrdem2);
            tableOrdem.addCell(cellTableOrdem3);
            tableOrdem.addCell(cellTableOrdem4);
            
            if(cellTableOrdem5 != null){
                tableOrdem.addCell(cellTableOrdem5);
            }
            
            document.add(tableOrdem);
        });
        
        Paragraph separador = new Paragraph().add("_________________________________________________")
                .setMarginTop(-30)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(separador);
    }
    
    private void configurarFontes() {
        try {
            var regularVerdana = context.getExternalContext().getRealPath("/resources/fonts/verdana.ttf");
            var boldVerdana = context.getExternalContext().getRealPath("/resources/fonts/verdanab.ttf");

            FontProgram regularProgram = FontProgramFactory.createFont(regularVerdana);
            FontProgram boldProgram = FontProgramFactory.createFont(boldVerdana);

            pdfFontVerdanaRegular = PdfFontFactory.createFont(regularProgram, PdfEncodings.WINANSI);
            pdfFontVerdanaBold = PdfFontFactory.createFont(boldProgram, PdfEncodings.WINANSI);
        } catch (IOException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    private void response(ByteArrayOutputStream baos, String nomeArquivo) {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/pdf");
        response.setContentLength(baos.size());
        response.setHeader("Content-disposition", "attachment; filename=" + nomeArquivo + ".pdf");

        try {
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void criarCabecalho1(Document document) throws IOException {
        InputStream caminhoLogoEsquerda = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagens/logo-esquerda.png");
        InputStream caminhoLogoDireitra = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagens/logo-direita.png");

        var imagemLogoEsquerda = new Image(ImageDataFactory.create(caminhoLogoEsquerda.readAllBytes()));
        var imagemLogoDireitra = new Image(ImageDataFactory.create(caminhoLogoDireitra.readAllBytes()));

        imagemLogoEsquerda.setWidth(100);
        imagemLogoEsquerda.setHeight(50);
        imagemLogoEsquerda.setFixedPosition(50, 750);

        imagemLogoDireitra.setWidth(110);
        imagemLogoDireitra.setHeight(50);
        imagemLogoDireitra.setFixedPosition(440, 750);

        document.add(imagemLogoEsquerda);
        document.add(imagemLogoDireitra);
    }

    private void criarCabecalho2(PdfDocument pdfDocument, Document document) throws IOException {
        var paragrafoCabecalho = new Paragraph("ASSEMBELIA LEGISLATIVA DO ESTADO DE RORAIMA").setFontSize(10).setFont(pdfFontVerdanaRegular);
        var pageSize = pdfDocument.getDefaultPageSize();
        var x = pageSize.getWidth() / 2;
        var y = pageSize.getTop() - 70;

        document.showTextAligned(paragrafoCabecalho, x, y, TextAlignment.CENTER, VerticalAlignment.MIDDLE);
    }

    private static class HeaderEventHandler implements IEventHandler {

        private Document document;

        public HeaderEventHandler(Document document) {
            this.document = document;
        }

        @Override
        public void handleEvent(Event event) {
            try {
                PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
                PdfDocument pdfDocument = docEvent.getDocument();
                int pageNumber = pdfDocument.getPageNumber(docEvent.getPage());

                criarCabecalho1(document);
                criarCabecalho2(pdfDocument, document, pageNumber);
            } catch (IOException e) {
                System.out.println("ERRO: " + e.getMessage());
            }
        }

        private void criarCabecalho1(Document document) throws IOException {
            InputStream caminhoLogoEsquerda = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagens/logo-esquerda.png");
            InputStream caminhoLogoDireitra = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagens/logo-direita.png");

            var imagemLogoEsquerda = new Image(ImageDataFactory.create(caminhoLogoEsquerda.readAllBytes()));
            var imagemLogoDireitra = new Image(ImageDataFactory.create(caminhoLogoDireitra.readAllBytes()));

            imagemLogoEsquerda.setWidth(100);
            imagemLogoEsquerda.setHeight(50);
            imagemLogoEsquerda.setFixedPosition(50, 750);

            imagemLogoDireitra.setWidth(110);
            imagemLogoDireitra.setHeight(50);
            imagemLogoDireitra.setFixedPosition(440, 750);

            document.add(imagemLogoEsquerda);
            document.add(imagemLogoDireitra);
        }

        public void criarCabecalho2(PdfDocument pdfDocument, Document document, int pageNumber) throws IOException {
            var paragrafoCabecalho = new Paragraph("ASSEMBELIA LEGISLATIVA DO ESTADO DE RORAIMA").setFontSize(10);
            Rectangle pageSize = pdfDocument.getPage(pageNumber).getPageSize();
            var x = pageSize.getWidth() / 2;
            var y = pageSize.getTop() - 70;
            document.showTextAligned(paragrafoCabecalho, x, y, pageNumber, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
            document.setMargins(110, 60, 50, 60);
        }
    }

    public static Color hexToRgb(String colorStr) {
        return new DeviceRgb(
            Integer.valueOf(colorStr.substring(1, 3), 16),
            Integer.valueOf(colorStr.substring(3, 5), 16),
            Integer.valueOf(colorStr.substring(5, 7), 16));
    }
}
