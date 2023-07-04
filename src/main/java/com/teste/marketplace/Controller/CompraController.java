package com.teste.marketplace.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.teste.marketplace.model.Compra;
import com.teste.marketplace.service.CompraService;

@RestController
@RequestMapping("/compras")
public class CompraController {

    private final CompraService compraService;

    @Autowired
    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<Compra>> criarCompra(@RequestBody Compra compra) {
        Compra novaCompra = compraService.createCompra(compra);
        EntityModel<Compra> compraModel = toEntityModel(novaCompra);
        return ResponseEntity.status(HttpStatus.CREATED).body(compraModel);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Compra>>> listarCompras() {
        List<Compra> compras = compraService.findAllCompras();
        List<EntityModel<Compra>> compraModels = compras.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(CompraController.class).withSelfRel();
        CollectionModel<EntityModel<Compra>> collectionModel = CollectionModel.of(compraModels, selfLink);
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Compra>> obterCompraPorId(@PathVariable Long id) throws NotFoundException {
        Optional<Compra> compra = compraService.getCompra(id);
        if (compra.isPresent()) {
            EntityModel<Compra> compraModel = toEntityModel(compra.get());
            return ResponseEntity.ok(compraModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Compra>> atualizarCompra(@PathVariable Long id, @RequestBody Compra compra) throws NotFoundException {
        Optional<Compra> compraExistente = compraService.getCompra(id);
        if (compraExistente.isPresent()) {
            Compra compraAtualizada = compraService.updateCompra(id, compra);
            EntityModel<Compra> compraModel = toEntityModel(compraAtualizada);
            return ResponseEntity.ok(compraModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCompra(@PathVariable Long id) {
        boolean compraExcluida = compraService.deleteCompra(id);
        if (compraExcluida) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/mais-compras")
    public ResponseEntity<List<String>> getClienteMaisComprasPorMes(@RequestParam int mes) {
        List<String> clientes = compraService.getClienteMaisComprasPorMes(mes);
        return ResponseEntity.ok(clientes);
    }

    private EntityModel<Compra> toEntityModel(Compra compra) {
        Link selfLink = WebMvcLinkBuilder.linkTo(CompraController.class)
                .slash(compra.getId())
                .withSelfRel();
        return EntityModel.of(compra, selfLink);
    }

}
