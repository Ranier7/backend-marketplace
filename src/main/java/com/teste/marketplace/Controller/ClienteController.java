package com.teste.marketplace.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.teste.marketplace.model.Cliente;
import com.teste.marketplace.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<Cliente>> adicionarCliente(@RequestBody Cliente cliente) {
        Cliente novoCliente = clienteService.createCliente(cliente);
        EntityModel<Cliente> clienteModel = toEntityModel(novoCliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteModel);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> listarClientes() {
        List<Cliente> clientes = clienteService.findAllClientes();
        List<EntityModel<Cliente>> clienteModels = clientes.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(ClienteController.class).withSelfRel();
        CollectionModel<EntityModel<Cliente>> collectionModel = CollectionModel.of(clienteModels, selfLink);
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> obterClientePorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.getCliente(id);
        if (cliente.isPresent()) {
            EntityModel<Cliente> clienteModel = toEntityModel(cliente.get());
            return ResponseEntity.ok(clienteModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> atualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Optional<Cliente> clienteExistente = clienteService.getCliente(id);
        if (clienteExistente.isPresent()) {
            Cliente clienteAtualizado = clienteService.updateCliente(id, cliente);
            EntityModel<Cliente> clienteModel = toEntityModel(clienteAtualizado);
            return ResponseEntity.ok(clienteModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long id) {
        boolean clienteExcluido = clienteService.deleteCliente(id);
        if (clienteExcluido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private EntityModel<Cliente> toEntityModel(Cliente cliente) {
        Link selfLink = WebMvcLinkBuilder.linkTo(ClienteController.class)
                .slash(cliente.getId())
                .withSelfRel();
        return EntityModel.of(cliente, selfLink);
    }

}
