package com.example.mymarvel.domain.character;

import com.example.mymarvel.api.dtos.UpdatedCharacter;
import com.example.mymarvel.exceptions.CharacterNotFoundException;
import com.example.mymarvel.domain.comic.Comic;
import com.example.mymarvel.domain.comic.ComicService;
import com.example.mymarvel.exceptions.ComicAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final ComicService comicService;

    @Transactional
    public Character getCharacter(Long id) {
        return characterRepository.findById(id).orElseThrow(() -> new CharacterNotFoundException("Not found..."));
    }

    @Transactional
    public List<Character> getAll() {
        return characterRepository.findAll();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public Character save(Character character) {
        Comic comic = comicService.getComic(character.getComics().get(0).getId());
        character.setComics(Collections.singletonList(comic));
        isNameUnique(character.getName());
       return characterRepository.save(character);
    }

    public void isNameUnique(String name) {
        Optional<Character> characterOptional = characterRepository.findByName(name);

        if (characterOptional.isPresent()) {
            throw new ComicAlreadyExistException("Unique failed error.");
        }
    }

    @Transactional
    public void delete(Character character) {
        characterRepository.findById(character.getId()).
                orElseThrow(() -> new CharacterNotFoundException("Not found"));
        characterRepository.delete(character);
    }

    @Transactional
    public void update(UpdatedCharacter updatedCharacter) {
        Character character = characterRepository.
                findById(updatedCharacter.getId()).
                orElseThrow(() -> new CharacterNotFoundException("Not found..."));
        isNameUnique(updatedCharacter.getNewName());
        character.setName(updatedCharacter.getNewName());
        characterRepository.save(character);
    }
}

