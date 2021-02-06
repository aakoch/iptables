package com.adamkoch.iptables.matches;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WebStringExtensionMatchTest {

  @Test
  void cannotUseSpaces() {
    assertThrows(IllegalArgumentException.class, () -> {
      new WebStringExtensionMatch("adam is cool");
    });
  }

  @Test
  void basicUse() {
    assertEquals("-m webstr --url pornhub", new WebStringExtensionMatch("pornhub").asString());
  }

}