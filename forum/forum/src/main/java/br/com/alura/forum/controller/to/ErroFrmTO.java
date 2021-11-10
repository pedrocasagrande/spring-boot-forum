package br.com.alura.forum.controller.to;

public class ErroFrmTO {
    private String campo;
    private String erro;

    public ErroFrmTO(String campo, String erro) {
        this.campo = campo;
        this.erro = erro;
    }

    public String getCampo() {
        return campo;
    }

    public String getErro() {
        return erro;
    }
}
