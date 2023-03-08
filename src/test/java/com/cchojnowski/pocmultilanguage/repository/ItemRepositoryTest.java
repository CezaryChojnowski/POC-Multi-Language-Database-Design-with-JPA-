package com.cchojnowski.pocmultilanguage.repository;

import com.cchojnowski.pocmultilanguage.dto.TranslatedItem;
import com.cchojnowski.pocmultilanguage.dto.TranslatedItemDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;


/**
 * @author cchojnowski
 */

@Profile("integration-test")
@SpringBootTest
class ItemRepositoryTest{

    @Autowired
    ItemRepository itemRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.5")
            .withUsername("user")
            .withPassword("password")
            .withDatabaseName("test_db");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.liquibase.contexts", () -> "!prod");
    }


    @SqlGroup({
            @Sql(value = "classpath:/test_data/item_data_1678001653.177821.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    @Transactional
    @ParameterizedTest(name = "{index} item name: {0}, language iso code: {1}")
    @MethodSource("expectedResultAndParameters")
    void shouldFindExpectedTranslatedItemList(String itemName, String languageIsoCode, List<TranslatedItemDTO> expectedTranslatedItem) {
        List<TranslatedItemDTO> actualTranslatedItem = itemRepository.getTranslatedItemByNameAndLanguageIsoCode(itemName, languageIsoCode).stream().map(TranslatedItem::map).collect(Collectors.toList());
        assertThat(expectedTranslatedItem).hasSameElementsAs(actualTranslatedItem);
    }


    private static Stream<Arguments> expectedResultAndParameters() {
        return Stream.of(
                Arguments.of("banan", "pl", List.of(
                        TranslatedItemDTO.builder().translatedName("banan").itemId(19L).build(),
                        TranslatedItemDTO.builder().translatedName("banan czerwony").itemId(44L).build(),
                        TranslatedItemDTO.builder().translatedName("banan lady finger").itemId(43L).build(),
                        TranslatedItemDTO.builder().translatedName("banan plantain").itemId(42L).build()
                )),
                Arguments.of("apple", "en", List.of(
                        TranslatedItemDTO.builder().translatedName("apple").itemId(136L).build(),
                        TranslatedItemDTO.builder().translatedName("apple juice").itemId(199L).build(),
                        TranslatedItemDTO.builder().translatedName("pineapple").itemId(164L).build()
                )),
                Arguments.of("apple", "pl", List.of(
                        TranslatedItemDTO.builder().translatedName("ananas").itemId(136L).build(),
                        TranslatedItemDTO.builder().translatedName("jabłko").itemId(199L).build(),
                        TranslatedItemDTO.builder().translatedName("sok jabłkowy").itemId(164L).build()
                )),
                Arguments.of("apple", "de", List.of(
                        TranslatedItemDTO.builder().translatedName("ananas").itemId(136L).build(),
                        TranslatedItemDTO.builder().translatedName("apfel").itemId(199L).build(),
                        TranslatedItemDTO.builder().translatedName("apfelsaft").itemId(164L).build()
                ))
        );
    }
}