package br.com.dio.controller;

import br.com.dio.model.Block;
import br.com.dio.persistence.dao.BlockDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/blocks")
public class BlockController {
    private final BlockDAO blockDAO;

    public BlockController(BlockDAO blockDAO) {
        this.blockDAO = blockDAO;
    }

    @GetMapping
    public ResponseEntity<List<Block>> findAllBlocks() {
        List<Block> blocks = blockDAO.findAll();
        return ResponseEntity.ok(blocks);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBlock(@RequestBody Block block) {
        blockDAO.save(block);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Block> update(@PathVariable Long id, @RequestBody Block block) {
        if (!blockDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        block.setId(id);
        return blockDAO.update(block)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
}
