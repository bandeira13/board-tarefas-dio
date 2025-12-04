package br.com.dio.controller;

import br.com.dio.model.Block;
import br.com.dio.service.BlockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/blocks")
public class BlockController {
    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @GetMapping
    public ResponseEntity<List<Block>> findAllBlocks() {
        List<Block> blocks = blockService.findAll();
        return ResponseEntity.ok(blocks);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBlock(@RequestBody Block block) {
        blockService.create(block);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Block> update(@PathVariable Long id, @RequestBody Block block) {
        return blockService.update(id, block)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

        }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        boolean deleted = blockService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
