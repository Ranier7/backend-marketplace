package com.teste.marketplace.Controller;

import com.teste.marketplace.model.Produto;
import com.teste.marketplace.service.ProdutoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<Produto>> salvarProduto(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.createProduto(produto);
        EntityModel<Produto> produtoModel = toEntityModel(novoProduto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoModel);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Produto>>> listarProdutos() {
        List<EntityModel<Produto>> produtos = produtoService.listarProdutos().stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(ProdutoController.class).withSelfRel();
        CollectionModel<EntityModel<Produto>> response = CollectionModel.of(produtos, selfLink);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Produto>> obterProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.getProduto(id);
        if (produto.isPresent()) {
            EntityModel<Produto> produtoModel = toEntityModel(produto.get());
            return ResponseEntity.ok(produtoModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Produto>> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {
        Optional<Produto> produtoExistente = produtoService.getProduto(id);
        if (produtoExistente.isPresent()) {
            Produto produtoAtualizado = produtoService.updateProduto(id, produto);
            EntityModel<Produto> produtoModel = toEntityModel(produtoAtualizado);
            return ResponseEntity.ok(produtoModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        boolean produtoExcluido = produtoService.deleteProduto(id);
        if (produtoExcluido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private EntityModel<Produto> toEntityModel(Produto produto) {
        Link selfLink = WebMvcLinkBuilder.linkTo(ProdutoController.class)
                .slash(produto.getId())
                .withSelfRel();
        return EntityModel.of(produto, selfLink);
    }
}
