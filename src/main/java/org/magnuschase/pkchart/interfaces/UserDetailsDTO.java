package org.magnuschase.pkchart.interfaces;

import lombok.Data;

@Data
public class UserDetailsDTO {
  private String email;
  private String username;
  private String displayName;
}
