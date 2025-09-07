package dev.mednikov.accounting.journals.services;

import dev.mednikov.accounting.journals.dto.JournalDto;

import java.util.List;
import java.util.Optional;

public interface JournalService {

    JournalDto createJournal(JournalDto journalDto);

    JournalDto updateJournal(JournalDto journalDto);

    void deleteJournal (Long journalId);

    List<JournalDto> getJournals (Long organizationId);

    Optional<JournalDto> getJournal (Long journalId);

}
