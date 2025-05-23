package org.magnuschase.pkchart.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class RegisterRequestDTO {
  @Schema(example = "misty.waterflower@cerulean.com", description = "User email")
  private String email;

  @Schema(example = "misty", description = "Username")
  private String username;

  @Schema(example = "psyduck321", description = "User password")
  private String password;

  @Schema(example = "Misty Waterflower", description = "Display name")
  @Nullable
  private String displayName;
}
