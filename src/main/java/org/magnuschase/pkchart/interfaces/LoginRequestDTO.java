package org.magnuschase.pkchart.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequestDTO {
  @Schema(example = "ash.ketchum@palettetown.com", description = "User email")
  private String email;

  @Schema(example = "pikachu123", description = "User password")
  private String password;
}
