package tech.ada.mercado.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import tech.ada.mercado.model.Mercado;

import java.util.List;

public interface MercadoRepository extends ReactiveMongoRepository<Mercado, String> {
    Flux<Mercado> findByNomeIn(List<String> nomes);
}
