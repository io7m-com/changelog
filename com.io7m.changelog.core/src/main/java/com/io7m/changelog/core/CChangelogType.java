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

package com.io7m.changelog.core;

import com.io7m.immutables.styles.ImmutablesStyleType;
import com.io7m.jaffirm.core.Preconditions;
import io.vavr.collection.Map;
import io.vavr.collection.SortedMap;
import org.immutables.value.Value;
import org.immutables.vavr.encodings.VavrEncodingEnabled;

import static org.immutables.value.Value.Immutable;

/**
 * The type of changelogs.
 */

@ImmutablesStyleType
@VavrEncodingEnabled
@Immutable
public interface CChangelogType
{
  /**
   * @return The project name
   */

  @Value.Parameter
  CProjectName project();

  /**
   * @return The list of releases
   */

  @Value.Parameter
  SortedMap<CVersionType, CRelease> releases();

  /**
   * @return The ticket systems
   */

  @Value.Parameter
  Map<String, CTicketSystem> ticketSystems();

  /**
   * Check preconditions for the type.
   */

  @Value.Check
  default void checkPreconditions()
  {
    final Map<String, CTicketSystem> systems = this.ticketSystems();

    this.releases().forEach(
      (version, release) ->
      {
        final String system_id = release.ticketSystemID();
        Preconditions.checkPrecondition(
          system_id,
          systems.containsKey(system_id),
          s -> "Release must refer to a defined ticket system");
      });

    Preconditions.checkPrecondition(
      systems,
      systems.values().filter(CTicketSystem::isDefault).size() <= 1,
      x -> "At most one ticket system may be declared as being the default");
  }
}