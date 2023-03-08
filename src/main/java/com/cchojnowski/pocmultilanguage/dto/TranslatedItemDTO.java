/**
 * 08 mar 2023 14:22:43
 */
package com.cchojnowski.pocmultilanguage.dto;

import lombok.Builder;
import lombok.Value;

/**
 * @author cchojnowski
 */
@Value
@Builder
public class TranslatedItemDTO {
    Long itemId;
    String translatedName;
}
