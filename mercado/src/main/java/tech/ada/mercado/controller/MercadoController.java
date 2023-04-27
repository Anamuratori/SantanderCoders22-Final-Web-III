package tech.ada.mercado.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.ada.mercado.model.Mercado;
import tech.ada.mercado.model.Moeda;
import tech.ada.mercado.service.MercadoService;

@RestController
@RequestMapping("/mercado")
@Slf4j
public class MercadoController {

    private MercadoService service;

    public MercadoController (MercadoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Mercado>> salvar(@RequestBody Mercado mercado) {
        return service.salvar(mercado)
                .map(atual -> ResponseEntity.ok().body(atual));
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<Mercado>>> listar() {
        return service.listar()
                .collectList()
                .map(mercados -> ResponseEntity.ok().body(Flux.fromIterable(mercados)) )
                .switchIfEmpty(Mono.just(ResponseEntity.noContent().build()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mercado>> getById(@PathVariable String id) {
        return service.buscarPorId(id)
                .map(atual -> ResponseEntity.ok().body(atual))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

/*    @GetMapping("/nomes")
    public Flux<ResponseEntity<Mercado>> getById(@RequestParam("nome1") String nome1, @RequestParam("nome2") String nome2) {
        return service.buscarPorNomes(nome1, nome2)
                .map(atual -> ResponseEntity.ok().body(atual))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }*/

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Mercado>> atualizar(@RequestBody MercadoRequest mercado, @PathVariable String id) {
        return service.atualizar(mercado.create(), id)
                .map(atual -> ResponseEntity.ok().body(atual))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> remover(@PathVariable String id) {
        return service.remover(id)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @GetMapping("/cotacao")
    public Mono<ResponseEntity<Flux<Moeda>>> consultarCotacao(@RequestParam("moeda") String moeda) {

        return service.consultarCotacao(moeda).collectList()
                .map(m -> ResponseEntity.ok().body(Flux.fromIterable(m)));

    }
}
