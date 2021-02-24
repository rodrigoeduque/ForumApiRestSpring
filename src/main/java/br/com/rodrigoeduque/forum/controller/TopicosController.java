package br.com.rodrigoeduque.forum.controller;

import br.com.rodrigoeduque.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.rodrigoeduque.forum.controller.dto.TopicoDto;
import br.com.rodrigoeduque.forum.controller.form.AtualizacaoTopicoForm;
import br.com.rodrigoeduque.forum.controller.form.TopicoForm;
import br.com.rodrigoeduque.forum.modelo.Topico;
import br.com.rodrigoeduque.forum.repository.CursoRepository;
import br.com.rodrigoeduque.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<TopicoDto> lista( String nomeCurso){

        if ( nomeCurso == null ) {
            System.out.println("Filtro : vazio");
            List<Topico> topicos = topicoRepository.findAll();
            return TopicoDto.converter(topicos);
        } else {
            System.out.println("Filtro : " + nomeCurso);
            List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
            return TopicoDto.converter(topicos);
        }


    }

    @PostMapping
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar (@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);
        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }


    @GetMapping("/{id}")
    public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {

        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id,@RequestBody @Valid AtualizacaoTopicoForm form){
        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()) {
            Topico topico = form.atualizar(id,topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));
        }else {
            return ResponseEntity.notFound().build();
        }




    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover (@PathVariable Long id){
        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()) {
            topicoRepository.deleteById(id);
            System.out.println("DELETANDO ID: " + id);
            System.out.println("RESPOSTA: " + ResponseEntity.ok().build());
            return ResponseEntity.ok().build();
        }else {
            System.out.println("ID INEXISTENTE");
            System.out.println("RESPOSTA: " + ResponseEntity.notFound().build());
            return ResponseEntity.notFound().build();
        }




    }
}
