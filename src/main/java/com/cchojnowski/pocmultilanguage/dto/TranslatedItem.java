package com.cchojnowski.pocmultilanguage.dto;

/**
 * @author cchojnowski
 */
public interface TranslatedItem {
    Long getItemId();
    String getTranslatedName();

    default TranslatedItemDTO map() {
       return TranslatedItemDTO.builder()
               .itemId(this.getItemId())
               .translatedName(this.getTranslatedName())
               .build();
    }
}
