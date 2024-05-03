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

package com.io7m.changelog.tests.core;

import com.io7m.changelog.core.CVersion;
import com.io7m.changelog.core.CVersionQualifier;
import com.io7m.changelog.core.CVersions;
import com.io7m.jaffirm.core.PreconditionViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CVersionStandardTest
{
  @Test
  public void testInvalid_0()
  {
    Assertions.assertThrows(PreconditionViolationException.class, () -> {
      CVersion.of(new BigInteger("-1"), ZERO, ZERO);
    });
  }

  @Test
  public void testInvalid_1()
  {
    Assertions.assertThrows(PreconditionViolationException.class, () -> {
      CVersion.of(ZERO, new BigInteger("-1"), ZERO);
    });
  }

  @Test
  public void testInvalid_2()
  {
    Assertions.assertThrows(PreconditionViolationException.class, () -> {
      CVersion.of(ZERO, ZERO, new BigInteger("-1"));
    });
  }

  @Test
  public void testOrdering()
    throws IOException
  {
    final CVersion p000 = CVersions.parse("0.0.0");
    final CVersion p010 = CVersions.parse("0.1.0");
    final CVersion p011 = CVersions.parse("0.1.1");
    final CVersion p100 = CVersions.parse("1.0.0");
    final CVersion p110 = CVersions.parse("1.1.0");
    final CVersion p111 = CVersions.parse("1.1.1");

    final List<CVersion> numbers = new ArrayList<CVersion>();
    numbers.add(p000);
    numbers.add(p010);
    numbers.add(p011);
    numbers.add(p100);
    numbers.add(p110);
    numbers.add(p111);

    for (int index = 0; index < numbers.size(); ++index) {
      for (int j = 0; j < numbers.size(); ++j) {
        final CVersion left = numbers.get(index);
        final CVersion right = numbers.get(j);

        System.out.println("Compare "
                           + left
                           + " "
                           + right
                           + " == "
                           + left.compareTo(right));

        if (j == index) {
          assertEquals(0L, left.compareTo(right));
        }
        if (j < index) {
          assertEquals(1L, left.compareTo(right));
          assertEquals(-1L, right.compareTo(left));
        }
        if (j > index) {
          assertEquals(-1L, left.compareTo(right));
          assertEquals(1L, right.compareTo(left));
        }
      }
    }

    assertEquals(0L, p000.compareTo(p000));
    assertEquals(-1L, p000.compareTo(p100));
    assertEquals(1L, p100.compareTo(p000));

    assertEquals(0L, p010.compareTo(p010));
    assertEquals(-1L, p000.compareTo(p010));
    assertEquals(-1L, p010.compareTo(p110));
    assertEquals(1L, p010.compareTo(p000));

    assertEquals(0L, p111.compareTo(p111));
    assertEquals(-1L, p011.compareTo(p111));
    assertEquals(-1L, p010.compareTo(p110));
    assertEquals(1L, p010.compareTo(p000));
  }

  @Test
  public void testParseNull()
  {
    Assertions.assertThrows(NullPointerException.class, () -> {
      CVersions.parse(null);
    });
  }

  @Test
  public void testValid_1()
    throws IOException
  {
    final CVersion vs = CVersions.parse("1.2.3");
    assertEquals(1L, vs.major().longValue());
    assertEquals(2L, vs.minor().longValue());
    assertEquals(3L, vs.patch().longValue());
  }

  @Test
  public void testValid_2()
    throws IOException
  {
    final CVersion vs = CVersions.parse("1.2.3-beta0001");
    assertEquals(1L, vs.major().longValue());
    assertEquals(2L, vs.minor().longValue());
    assertEquals(3L, vs.patch().longValue());
    assertEquals("beta0001", vs.qualifier().orElseThrow().text());
  }

  private static DynamicTest invalidVersionTestOf(
    final String text)
  {
    return DynamicTest.dynamicTest(
      "testInvalidVersion_%s".formatted(text),
      () -> {
        final var ex =
          assertThrows(IOException.class, () -> {
            CVersions.parse(text);
          });
        ex.printStackTrace(System.err);
      });
  }

  private static DynamicTest validVersionTestOf(
    final String text)
  {
    return DynamicTest.dynamicTest(
      "testValidVersion_%s".formatted(text),
      () -> {
        final var p0 = CVersions.parse(text);
        final var p1 = CVersions.parse(text);
        assertEquals(p0, p1);
        assertEquals(p0, p0);
        assertEquals(p0.hashCode(), p1.hashCode());
        assertEquals(p0.toString(), p1.toString());
        assertEquals(text, "%s".formatted(p0));
        assertEquals(0, p0.compareTo(p1));
        assertNotEquals(p0, Integer.valueOf(23));
      });
  }


  /**
   * Ensure that the ordering relation matches the semver spec.
   */

  @Test
  public void testSpecOrdering0()
  {
    final var names =
      List.of(
        "1.0.0-SNAPSHOT",
        "1.0.0-alpha",
        "1.0.0-alpha.1",
        "1.0.0-alpha.beta",
        "1.0.0-beta",
        "1.0.0-beta.2",
        "1.0.0-beta.11",
        "1.0.0-rc.1",
        "1.0.0"
      );

    final var versions =
      names.stream()
        .map(x -> {
          try {
            return CVersions.parse(x);
          } catch (final IOException e) {
            throw new RuntimeException(e);
          }
        }).toList();

    final var versionsSorted =
      versions.stream()
        .sorted()
        .toList();

    assertEquals(versions, versionsSorted);
  }

  /**
   * Ensure that the ordering relation matches the semver spec.
   *
   * @throws Exception On errors
   */

  @Test
  public void testSpecOrdering1()
    throws Exception
  {
    final var v = CVersions.parse("1.0.0-SNAPSHOT");
    final var q = v.qualifier().get();
    assertEquals(0, q.compareTo(q));
  }

  /**
   * @return A set of invalid version tests
   */

  @TestFactory
  public Stream<DynamicTest> testInvalidVersions()
  {
    return Stream.of(
      "",
      "1",
      "1.0",
      "1.a",
      "1.0.0-",
      "1.0.0-β",
      "v1.2.3"
    ).map(CVersionStandardTest::invalidVersionTestOf);
  }

  /**
   * @return A set of valid version tests
   */

  @TestFactory
  public Stream<DynamicTest> testValidVersions()
  {
    return Stream.of(
      "1.0.0",
      "1.0.0-SNAPSHOT",
      "1.0.0-alpha",
      "1.0.0-alpha.1",
      "1.0.0-alpha.beta",
      "1.0.0-beta",
      "1.0.0-beta.2",
      "1.0.0-beta.11",
      "1.0.0-rc.1",
      "1.0.0",
      "1.0.0-0.3.7",
      "1.0.0-x.7.z.92",
      "1.0.0-x-y-z.-"
    ).map(CVersionStandardTest::validVersionTestOf);
  }

  /**
   * An invalid version.
   */

  @Test
  public void testInvalidQualifier0()
  {
    assertThrows(IllegalArgumentException.class, () -> {
      CVersion.builder()
        .setMajor(ONE)
        .setMinor(ZERO)
        .setPatch(ZERO)
        .setQualifier(new CVersionQualifier("β"))
        .build();
    });
  }
}
