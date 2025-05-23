package org.magnuschase.pkchart.interfaces;

import lombok.Data;

@Data
public class ChangePasswordDTO {
  private String currentPassword;
  private String newPassword;
}
