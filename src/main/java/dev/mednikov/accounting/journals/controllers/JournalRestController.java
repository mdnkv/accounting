package dev.mednikov.accounting.journals.controllers;

import dev.mednikov.accounting.journals.dto.JournalDto;
import dev.mednikov.accounting.journals.services.JournalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/journals")
public class JournalRestController {

    private final JournalService journalService;

    public JournalRestController(JournalService journalService) {
        this.journalService = journalService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('journals:create') and hasAuthority(#body.organizationId)")
    public @ResponseBody JournalDto createJournal(@RequestBody JournalDto body) {
        return this.journalService.createJournal(body);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('journals:update') and hasAuthority(#body.organizationId)")
    public @ResponseBody JournalDto updateJournal(@RequestBody JournalDto body) {
        return this.journalService.updateJournal(body);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('journals:delete')")
    public void deleteJournal(@PathVariable Long id) {
        this.journalService.deleteJournal(id);
    }

    @GetMapping("/organization/{organizationId}")
    @PreAuthorize("hasAuthority('journals:view') and hasAuthority(#organizationId)")
    public @ResponseBody List<JournalDto> getJournals(@PathVariable Long organizationId) {
        return this.journalService.getJournals(organizationId);
    }

    @GetMapping("/journal/{id}")
    @PreAuthorize("hasAuthority('journals:view')")
    public ResponseEntity<JournalDto> getJournal(@PathVariable Long id) {
        Optional<JournalDto> result = this.journalService.getJournal(id);
        return ResponseEntity.of(result);
    }

}
