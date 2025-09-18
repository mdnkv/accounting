package dev.mednikov.accounting.shared.bootstrap;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.journals.models.Journal;
import dev.mednikov.accounting.journals.repositories.JournalRepository;
import dev.mednikov.accounting.organizations.events.OrganizationCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JournalBootstrapService {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    private final JournalRepository journalRepository;

    public JournalBootstrapService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @EventListener
    public void onOrganizationCreatedEventListener(OrganizationCreatedEvent e) {
        List<Journal> journals = new ArrayList<>();

        Journal generalJournal = new Journal();
        generalJournal.setId(snowflakeGenerator.next());
        generalJournal.setOrganization(e.getOrganization());
        generalJournal.setName("General");
        generalJournal.setActive(true);
        journals.add(generalJournal);

        Journal salesJournal = new Journal();
        salesJournal.setId(snowflakeGenerator.next());
        salesJournal.setOrganization(e.getOrganization());
        salesJournal.setName("Sales");
        salesJournal.setActive(true);
        journals.add(salesJournal);

        Journal purchaseJournal = new Journal();
        purchaseJournal.setId(snowflakeGenerator.next());
        purchaseJournal.setOrganization(e.getOrganization());
        purchaseJournal.setName("Purchases");
        purchaseJournal.setActive(true);
        journals.add(purchaseJournal);

        this.journalRepository.saveAll(journals);
    }


}
