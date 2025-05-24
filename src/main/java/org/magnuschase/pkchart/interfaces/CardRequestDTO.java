package org.magnuschase.pkchart.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Data;
import org.magnuschase.pkchart.model.CardRarity;
import org.magnuschase.pkchart.model.Language;

@Data
public class CardRequestDTO {
  @Schema(example = "Pikachu with Grey Felt Hat", description = "Card Name")
  private String name;

  @Schema(example = "Promo", description = "Card Rarity")
  private CardRarity rarity;

  @Schema(example = "ENG", description = "Language")
  private Language language;

  @Schema(example = "85", description = "Language")
  private Short number;

  @Schema(example = "[ONLY FOR REMOVAL REQUESTS] Enter reason for removal.", description = "Reason")
  @Nullable
  private String reason;

  @Schema(example = "SVP Black Star Promo", description = "Set Name")
  private String setName;
}
