package br.com.dio.service;

import br.com.dio.model.Block;
import br.com.dio.persistence.dao.BlockDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlockService {

    private final BlockDAO blockDAO;

    public BlockService(BlockDAO blockDAO) {
        this.blockDAO = blockDAO;
    }

    public Block create(Block block) {
        if (block == null) {
            throw new IllegalArgumentException("Block cannot be null");
        }
        if (block.getTitle() == null || block.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Block title cannot be empty");
        }
        return blockDAO.save(block);
    }

    public Optional<Block> findById(Long id) {
        return blockDAO.findById(id);
    }

    public List<Block> findAll() {
        return blockDAO.findAll();
    }

    public Optional<Block> update(Long id, Block block) {
        if (!blockDAO.existsById(id)) {
            return Optional.empty();
        }
        block.setId(id);
        return blockDAO.update(block);
    }

    public boolean delete(Long id) {
        return blockDAO.deleteById(id);
    }

    public void deleteAll() {
        blockDAO.deleteAll();
    }

    public boolean exists(Long id) {
        return blockDAO.existsById(id);
    }
}
