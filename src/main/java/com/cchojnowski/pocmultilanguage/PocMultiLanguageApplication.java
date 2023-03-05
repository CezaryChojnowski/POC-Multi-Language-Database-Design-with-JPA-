package com.cchojnowski.pocmultilanguage;

import com.cchojnowski.pocmultilanguage.dto.TranslatedItem;
import com.cchojnowski.pocmultilanguage.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class PocMultiLanguageApplication implements CommandLineRunner {

    private final ItemRepository itemRepository;

    public static void main(String[] args) {
        SpringApplication.run(PocMultiLanguageApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<TranslatedItem> translatedItemList = itemRepository.getTranslatedItemByNameAndLanguageIsoCode("an", "en");
        int a =0;
    }
}
