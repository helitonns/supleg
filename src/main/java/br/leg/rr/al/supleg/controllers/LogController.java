package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.Log;
import br.leg.rr.al.supleg.models.Usuario;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.services.UsuarioService;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Heliton
 */
@Named
@ViewScoped
public class LogController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private UsuarioService usuarioService;
    
    @Inject
    private LogService logService;
    
    @Getter
    private List<Usuario> usuarios;
    
    @Getter
    private List<Log> logs;
    
    @Getter
    @Setter
    private LocalDate dataInicial;
    
    @Getter
    @Setter
    private LocalDate dataFinal;
    
    @Getter
    @Setter
    private TipoAcao tipoAcao;
    
    @Getter
    @Setter
    private Usuario usuario;
    
    @Getter
    private final String serverTimeZone = ZoneId.systemDefault().toString();

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        iniciar();
    }

    public void pesquisarLogs(){
        try {
            logs = logService.pesquisarLogs(dataInicial, dataFinal, usuario, tipoAcao);
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
    }
    
    public String excluirLogs(){
        try {
            logService.excluirPorData(dataInicial, dataFinal);
            MensagensUtils.addInfoMessageFlashScoped("Logs excluídos com sucesso!");
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
        return "log.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "log.xhtml" + "?faces-redirect=true";
    }

    public TipoAcao[] getTiposAcoes(){
        return TipoAcao.values();
    }
    //__________________________________________________________________________
    private void iniciar() {
        dataInicial = LocalDate.now();
        dataFinal = LocalDate.now().plusDays(1);
        
        logs = new ArrayList<>();
        
        usuarios = new ArrayList<>();
        listarUsuarios();
    }

    private void listarUsuarios(){
        try {
            usuarios = usuarioService.listarTodos();
        } catch (Exception e) {
            MensagensUtils.addErrorMessageFlashScoped(e.getLocalizedMessage());
        }
    }
    
}
