package com.booster.vocabulary.controller;

import com.booster.vocabulary.config.security.UserDetailsImpl;
import com.booster.vocabulary.dto.LanguageToLearnDto;
import com.booster.vocabulary.dto.request.LanguageToLearnRequestDto;
import com.booster.vocabulary.service.LanguageToLearnService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/language-to-learn")
public class LanguageToLearnController {

    private final LanguageToLearnService languageToLearnService;

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<LanguageToLearnDto> create(@RequestBody LanguageToLearnRequestDto languageToLearnRequestDto) {
        String userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        languageToLearnRequestDto.setUserId(userId);
        LanguageToLearnDto languageToLearnDto = languageToLearnService.create(languageToLearnRequestDto);
        return ResponseEntity.ok(languageToLearnDto);
    }

    @GetMapping("/list")
    ResponseEntity<LanguageToLearnDtoList> findAll() {
        String userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<LanguageToLearnDto> languageToLearnDtos = languageToLearnService.findAllByUserId(userId);
        return ResponseEntity.ok(new LanguageToLearnDtoList(languageToLearnDtos));
    }

    // todo: security: an authenticated user can obtain an id and get someone's languageToLearn
    @GetMapping("/{id}")
    ResponseEntity<LanguageToLearnDto> findById(@PathVariable String id) {
        LanguageToLearnDto languageToLearnDto = languageToLearnService.findById(id);
        return ResponseEntity.ok(languageToLearnDto);
    }

    // todo: security: an authenticated user can obtain an id and delete someone's languageToLearn
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteById(@PathVariable String id) {
        languageToLearnService.deleteById(id);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LanguageToLearnDtoList {
        List<LanguageToLearnDto> languageToLearnDtoList;
    }

}
