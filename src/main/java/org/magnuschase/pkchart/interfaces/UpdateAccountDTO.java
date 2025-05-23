package org.magnuschase.pkchart.interfaces;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateAccountDTO {
  @Nullable private String displayName;
  @Nullable private String email;
}
