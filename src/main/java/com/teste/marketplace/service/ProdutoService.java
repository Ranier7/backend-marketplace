package com.teste.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teste.marketplace.Repository.ProdutoRepository;
import com.teste.marketplace.model.Produto;

@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto createProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> getProduto(Long id) {
        return produtoRepository.findById(id);
    }

    public Produto updateProduto(Long id, Produto produto) {
        Produto existingProduto = produtoRepository.findById(id).orElse(null);
        if (existingProduto != null) {
            existingProduto.setNome(produto.getNome());
            existingProduto.setPreco(produto.getPreco());
            existingProduto.setDescricao(produto.getDescricao());
            return produtoRepository.save(existingProduto);
        }
        return null;
    }

    public boolean deleteProduto(Long id) {
        try {
            produtoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}
