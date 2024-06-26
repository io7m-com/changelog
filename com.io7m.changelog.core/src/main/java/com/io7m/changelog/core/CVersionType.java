/*
 * Copyright © 2020 Mark Raynsford <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.changelog.core;

import com.io7m.immutables.styles.ImmutablesStyleType;
import com.io7m.jaffirm.core.Preconditions;
import org.immutables.value.Value;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Formattable;
import java.util.Formatter;
import java.util.Optional;

/**
 * A version number.
 */

@Value.Immutable
@ImmutablesStyleType
public interface CVersionType extends Comparable<CVersionType>, Formattable
{
  /**
   * @return The major version
   */

  @Value.Parameter
  BigInteger major();

  /**
   * @return The minor version
   */

  @Value.Parameter
  BigInteger minor();

  /**
   * @return The patch version
   */

  @Value.Parameter
  BigInteger patch();

  /**
   * @return The version qualifier
   */

  Optional<CVersionQualifier> qualifier();

  @Override
  default int compareTo(
    final CVersionType other)
  {
    return Comparator.comparing(CVersionType::major)
      .thenComparing(CVersionType::minor)
      .thenComparing(CVersionType::patch)
      .thenComparing(
        CVersionType::qualifier,
        QVersionQualifiers.COMPARE_QUALIFIER)
      .compare(this, other);
  }

  /**
   * Check preconditions for the type.
   */

  @Value.Check
  default void checkPreconditions()
  {
    Preconditions.checkPreconditionV(
      this.major().compareTo(BigInteger.ZERO) >= 0,
      "Major version must be non-negative");
    Preconditions.checkPreconditionV(
      this.minor().compareTo(BigInteger.ZERO) >= 0,
      "Minor version must be non-negative");
    Preconditions.checkPreconditionV(
      this.patch().compareTo(BigInteger.ZERO) >= 0,
      "Patch version must be non-negative");
  }

  @Override
  default void formatTo(
    final Formatter formatter,
    final int flags,
    final int width,
    final int precision)
  {
    if (this.qualifier().isPresent()) {
      formatter.format(
        "%s.%s.%s-%s",
        this.major(),
        this.minor(),
        this.patch(),
        this.qualifier().get()
      );
    } else {
      formatter.format("%s.%s.%s", this.major(), this.minor(), this.patch());
    }
  }
}
