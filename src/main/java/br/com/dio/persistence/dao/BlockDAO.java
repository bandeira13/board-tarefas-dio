package br.com.dio.persistence.dao;

import br.com.dio.model.Block;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BlockDAO {

    private final ConcurrentHashMap<Long, Block> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public Block save(Block block) {
        if (block == null) {
            throw new IllegalArgumentException("Block cannot be null");
        }
        if (block.getId() == null) {
            long id = idGenerator.incrementAndGet();
            block.setId(id);
        }
        store.put(block.getId(), block);
        return block;
    }

    public Optional<Block> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(store.get(id));
    }

    public List<Block> findAll() {
        return new ArrayList<>(store.values());
    }

    public boolean existsById(Long id) {
        return id != null && store.containsKey(id);
    }

    public Optional<Block> update(Block block) {
        if (block == null || block.getId() == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(store.computeIfPresent(block.getId(), (k, v) -> block));
    }

    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        return store.remove(id) != null;
    }

    public void deleteAll() {
        store.clear();
    }
}
