package com.example.mymarvel.domain.comic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComicService {
    private final ComicRepository comicRepository;

    public Comic getComic(Long id) {
        return comicRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public void save(Comic comic) {
        comicRepository.save(comic);
    }

    public void delete(Comic comic) { comicRepository.delete(comic);}

    public List<Comic> getAll() {
        return comicRepository.findAll();
    }

}
