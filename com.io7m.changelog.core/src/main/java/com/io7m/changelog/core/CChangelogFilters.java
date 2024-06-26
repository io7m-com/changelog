/*
 * Copyright © 2017 <code@io7m.com> http://io7m.com
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

import com.io7m.junreachable.UnreachableCodeException;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Functions to transform and filter changelogs.
 */

public final class CChangelogFilters
{
  private CChangelogFilters()
  {
    throw new UnreachableCodeException();
  }

  /**
   * @param changelog The current changelog
   * @param version   The release version
   * @param count     The maximum number of releases to display
   *
   * @return A version of {@code changelog} featuring at most {@code count}
   * releases up to and including {@code version}, or nothing if the changelog
   * does not contain {@code version}
   */

  public static Optional<CChangelog> upToAndIncluding(
    final CChangelog changelog,
    final CVersion version,
    final int count)
  {
    Objects.requireNonNull(changelog, "Changelog");
    Objects.requireNonNull(version, "Version");

    final var releases = changelog.releases();
    if (!releases.containsKey(version)) {
      return Optional.empty();
    }

    final var versions =
      changelog.releaseVersions()
        .stream()
        .filter(v -> v.compareTo(version) >= 0)
        .limit(count)
        .toList();

    final var releasesFiltered =
      new TreeMap<CVersion, CRelease>();
    for (final var currentVersion : versions) {
      releasesFiltered.put(currentVersion, releases.get(version));
    }

    final var newChangelog =
      CChangelog.builder()
        .from(changelog)
        .setReleases(releasesFiltered)
        .build();

    return Optional.of(newChangelog);
  }

  /**
   * Filter the changelog to only contain the {@code size} most recent releases.
   *
   * @param changelog The changelog
   * @param size      The limit
   *
   * @return A new changelog containing only the filtered releases
   */

  public static CChangelog limit(
    final CChangelog changelog,
    final long size)
  {
    Objects.requireNonNull(changelog, "changelog");

    final var versions =
      changelog.releaseVersions()
        .stream()
        .sorted(Comparator.reverseOrder())
        .limit(size)
        .collect(Collectors.toSet());

    final var newReleases =
      new TreeMap<CVersion, CRelease>();

    changelog.releases()
      .entrySet()
      .stream()
      .filter(e -> versions.contains(e.getKey()))
      .forEach(entry -> {
        newReleases.put(entry.getKey(), entry.getValue());
      });

    return CChangelog.builder()
      .from(changelog)
      .setReleases(newReleases)
      .build();
  }
}
