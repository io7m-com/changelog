/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.changelog.cmdline;

import com.beust.jcommander.IStringConverter;

/**
 * A converter for {@link CLLogLevel} values.
 */

public final class CLLogLevelConverter implements
  IStringConverter<CLLogLevel>
{
  /**
   * Construct a new converter.
   */

  public CLLogLevelConverter()
  {

  }

  @Override
  public CLLogLevel convert(final String value)
  {
    for (final CLLogLevel v : CLLogLevel.values()) {
      if (value.equals(v.getName())) {
        return v;
      }
    }

    throw new CLLogLevelUnrecognized(
      "Unrecognized verbosity level: " + value);
  }
}