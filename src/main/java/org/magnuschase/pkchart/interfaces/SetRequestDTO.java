package org.magnuschase.pkchart.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.magnuschase.pkchart.model.Language;

@Data
public class SetRequestDTO {
  @Schema(example = "SVP Black Star Promo", description = "Set / Expansion Name")
  private String name;

  @Schema(example = "SVP", description = "Code / Symbol")
  private String symbol;

  @Schema(example = "ENG", description = "Language")
  private Language language;

  @Schema(example = "0", description = "Size")
  private Short size;
}
