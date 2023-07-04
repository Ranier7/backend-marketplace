package com.teste.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teste.marketplace.Repository.ClienteRepository;
import com.teste.marketplace.model.Cliente;

@Service
@Transactional
public class ClienteService {
    
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente createCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    public List<Cliente> findAllClientes() {
        return clienteRepository.findAll();
    }
    
    public Optional<Cliente> getCliente(Long id) {
        return clienteRepository.findById(id);
    }
    
    public Cliente updateCliente(Long id, Cliente clienteAtualizado) {
        Cliente existingCliente = clienteRepository.findById(id).orElse(null);
        if (existingCliente != null) {
            existingCliente.setNome(clienteAtualizado.getNome());
            existingCliente.setDataNascimento(clienteAtualizado.getDataNascimento());
            return clienteRepository.save(existingCliente);
        } else {
            return null;
        }
    }
    
    public boolean deleteCliente(Long id) {
        try {
            clienteRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
