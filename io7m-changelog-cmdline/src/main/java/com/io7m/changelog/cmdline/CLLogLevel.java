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

import ch.qos.logback.classic.Level;
import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * Log level.
 */

public enum CLLogLevel
{
  /**
   * @see ch.qos.logback.classic.Level#TRACE
   */

  LOG_TRACE("trace"),

  /**
   * @see ch.qos.logback.classic.Level#DEBUG
   */

  LOG_DEBUG("debug"),

  /**
   * @see ch.qos.logback.classic.Level#INFO
   */

  LOG_INFO("info"),

  /**
   * @see ch.qos.logback.classic.Level#WARN
   */

  LOG_WARN("warn"),

  /**
   * @see ch.qos.logback.classic.Level#ERROR
   */

  LOG_ERROR("error");


  private final String name;

  CLLogLevel(final String in_name)
  {
    this.name = NullCheck.notNull(in_name);
  }

  @Override
  public String toString()
  {
    return this.name;
  }

  /**
   * @return The short name of the level
   */

  public String getName()
  {
    return this.name;
  }

  Level toLevel()
  {
    switch (this) {
      case LOG_TRACE:
        return Level.TRACE;
      case LOG_DEBUG:
        return Level.DEBUG;
      case LOG_INFO:
        return Level.INFO;
      case LOG_WARN:
        return Level.WARN;
      case LOG_ERROR:
        return Level.ERROR;
    }

    throw new UnreachableCodeException();
  }
}
