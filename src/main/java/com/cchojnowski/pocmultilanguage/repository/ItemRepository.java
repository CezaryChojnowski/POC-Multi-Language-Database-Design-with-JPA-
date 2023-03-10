package com.cchojnowski.pocmultilanguage.repository;

import com.cchojnowski.pocmultilanguage.dto.TranslatedItem;
import com.cchojnowski.pocmultilanguage.entity.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author cchojnowski
 */
public interface ItemRepository extends CrudRepository<Item, Long> {

    @Query(value = """
            SELECT * FROM (
                SELECT
                  i.id AS "itemId",
                  i."name" AS "translatedName"
                FROM
                  item.item i
                  INNER JOIN "translation".text_content tc ON tc.id = i.text_content_id
                  INNER JOIN "translation"."language" l ON l.id = tc.language_id
                WHERE
                  LOWER(i."name") LIKE LOWER(CONCAT('%',:itemName,'%'))
                  AND l.iso_code LIKE :languageIsoCode
                UNION
                SELECT
                  i.id AS "itemId",
                  t."translation" AS "translatedName"
                FROM
                  "translation"."translation" t
                  INNER JOIN "translation"."language" l2 ON l2.id = t.language_id
                  INNER JOIN "translation".text_content tc2 ON tc2.id = t.text_content_id
                  INNER JOIN item.item i ON i.text_content_id = tc2.id
                WHERE
                  l2.iso_code LIKE :languageIsoCode
                  AND t.text_content_id in (
                    SELECT
                      tc.id
                    FROM
                      item.item i
                      INNER JOIN "translation".text_content tc ON tc.id = i.text_content_id
                      INNER JOIN "translation"."language" l ON l.id = tc.language_id
                      INNER JOIN "translation"."translation" t on t.text_content_id = tc.id
                    WHERE
                      LOWER(t."translation") LIKE LOWER(CONCAT('%',:itemName,'%'))
                      )
            )
            as items order by "translatedName"
            """, nativeQuery = true)
    List<TranslatedItem> getTranslatedItemByNameAndLanguageIsoCode(@Param("itemName") String itemName,
                                                                   @Param("languageIsoCode") String languageIsoCode);
}
