package com.booster.vocabulary.controller;

import com.booster.vocabulary.config.security.UserDetailsImpl;
import com.booster.vocabulary.dto.VocabularyEntryDto;
import com.booster.vocabulary.dto.request.VocabularyEntryRequestDto;
import com.booster.vocabulary.dto.response.VocabularyEntryResponseId;
import com.booster.vocabulary.service.VocabularyEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/vocabulary-entry")
public class VocabularyEntryController {

    private final VocabularyEntryService vocabularyEntryService;

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<VocabularyEntryResponseId> create(@RequestBody VocabularyEntryRequestDto vocabularyEntryRequestDto) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        vocabularyEntryRequestDto.setUserId(userId);

        Long vocabularyEntryId = vocabularyEntryService.create(vocabularyEntryRequestDto);
        return ResponseEntity.ok(new VocabularyEntryResponseId(vocabularyEntryId));
    }

    @GetMapping("/list")
    ResponseEntity<List<VocabularyEntryDto>> list() {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<VocabularyEntryDto> vocabularyEntryDtoList = vocabularyEntryService.findAllForUserId(userId);
        return ResponseEntity.ok(vocabularyEntryDtoList);
    }

    @GetMapping("/{vocabularyEntryId}")
    ResponseEntity<VocabularyEntryDto> getById(@PathVariable Long vocabularyEntryId) {
        VocabularyEntryDto vocabularyEntryDto = vocabularyEntryService.findById(vocabularyEntryId);
        return ResponseEntity.ok(vocabularyEntryDto);
    }

}
