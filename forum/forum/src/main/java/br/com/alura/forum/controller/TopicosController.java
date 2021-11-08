package br.com.alura.forum.controller;

import br.com.alura.forum.controller.form.TopicoFrm;
import br.com.alura.forum.controller.form.TopicoFrmAtualizacao;
import br.com.alura.forum.controller.to.DetalheTopicoTO;
import br.com.alura.forum.controller.to.TopicoTO;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    private final TopicoRepository repository;

    private final CursoRepository repositoryCurso;

    public TopicosController(TopicoRepository repository, CursoRepository repositoryCurso) {
        this.repository = repository;
        this.repositoryCurso = repositoryCurso;
    }

    @GetMapping
    @Cacheable(value = "listaDeTopicos")
    public Page<TopicoTO> lista(@RequestParam(required = false) String nomeCurso,
                                @PageableDefault(sort = "id", direction = Sort.Direction.ASC, page = 0, size = 10)
                                        Pageable paginacao) {

        Page<Topico> topicos;
        if (nomeCurso == null) {
            topicos = repository.findAll(paginacao);
        } else {
            topicos = repository.findByCursoNome(nomeCurso, paginacao);
        }
        return TopicoTO.converter(topicos);
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoTO> cadastrar(@RequestBody @Valid TopicoFrm frm, UriComponentsBuilder uriBuilder) {
        Topico topico = frm.converter(repositoryCurso);
        repository.save(topico);
        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoTO(topico));
    }

    @GetMapping("/{id}")
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<DetalheTopicoTO> detalhar(@PathVariable Long id) {
        Optional<Topico> topico = repository.findById(id);
        return topico.map(value -> ResponseEntity.ok(new DetalheTopicoTO(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoTO> atualizar(@PathVariable Long id, @RequestBody @Valid TopicoFrmAtualizacao frm) {
        Optional<Topico> topico = repository.findById(id);
        if (topico.isPresent()) {
            Topico atualizaTopico = frm.atualizar(id, repository);
            return ResponseEntity.ok(new TopicoTO(atualizaTopico));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Optional<Topico> topico = repository.findById(id);
        if (topico.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
