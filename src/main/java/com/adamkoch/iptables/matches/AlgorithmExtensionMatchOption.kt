package com.adamkoch.iptables.matches;

/**
 * For use with {@link StringExtensionMatch}
 *
 * @author aakoch
 * @since 0.1.0
 */
public class AlgorithmExtensionMatchOption extends GenericExtensionMatchOption {

  public static final String BOYER_MOORE = "bm";
  public static final String KNUTH_PRATT_MORRIS = "kmp";

  @SuppressWarnings("StaticVariableOfConcreteClass")
  public static final AlgorithmExtensionMatchOption BOYER_MOORE_OPTION = new AlgorithmExtensionMatchOption(BOYER_MOORE);

  @SuppressWarnings("StaticVariableOfConcreteClass")
  public static final AlgorithmExtensionMatchOption KNUTH_PRATT_MORRIS_OPTION = new AlgorithmExtensionMatchOption(
      KNUTH_PRATT_MORRIS);
  private final String algorithm;

  public AlgorithmExtensionMatchOption() {
    this(BOYER_MOORE);
  }

  public AlgorithmExtensionMatchOption(String algorithm) {
    this.algorithm = algorithm;
  }

  @Override
  public String asString() {
    return "--algo " + algorithm;
  }
}
