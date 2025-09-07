package dev.mednikov.accounting.shared.bootstrap;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.journals.models.Journal;
import dev.mednikov.accounting.journals.repositories.JournalRepository;
import dev.mednikov.accounting.organizations.events.OrganizationCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class JournalBootstrapService {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    private final JournalRepository journalRepository;

    public JournalBootstrapService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @EventListener
    public void onOrganizationCreatedEventListener(OrganizationCreatedEvent e) {
        Journal journal = new Journal();
        journal.setId(snowflakeGenerator.next());
        journal.setOrganization(e.getOrganization());
        journal.setName("General");
        journal.setActive(true);

        this.journalRepository.save(journal);
    }


}
