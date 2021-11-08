package br.com.alura.forum.controller.form;

import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;
import com.sun.istack.NotNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

public class TopicoFrmAtualizacao {

    @com.sun.istack.NotNull
    @NotEmpty
    @Length(min = 2, max = 50)
    private String titulo;
    @NotNull
    @NotEmpty
    @Length(min = 2, max = 500)
    private String mensagem;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Topico atualizar(Long id, TopicoRepository repository) {
        Topico topico = repository.getOne(id);
        topico.setTitulo(this.titulo);
        topico.setMensagem(this.mensagem);
        return topico;
    }
}
