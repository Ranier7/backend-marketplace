package com.teste.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teste.marketplace.Repository.CompraRepository;
import com.teste.marketplace.model.Compra;

@Service
@Transactional
public class CompraService {

    private final CompraRepository compraRepository;

    @Autowired
    public CompraService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    public Compra createCompra(Compra compra) {
        return compraRepository.save(compra);
    }

    public List<Compra> findAllCompras() {
        return compraRepository.findAll();
    }

    public Optional<Compra> getCompra(Long id) {
        return compraRepository.findById(id);
    }

    public Compra updateCompra(Long id, Compra compra) {
        Compra existingCompra = compraRepository.findById(id).orElse(null);
        if (existingCompra != null) {
            existingCompra.setCliente(compra.getCliente());
            existingCompra.setProduto(compra.getProduto());
            existingCompra.setDataCompra(compra.getDataCompra());
            return compraRepository.save(existingCompra);
        }
        return null;
    }

    public boolean deleteCompra(Long id) {
        try {
            compraRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getClienteMaisComprasPorMes(int mes) {
        return compraRepository.findClienteMaisComprasPorMes(mes);
    }
    
}
