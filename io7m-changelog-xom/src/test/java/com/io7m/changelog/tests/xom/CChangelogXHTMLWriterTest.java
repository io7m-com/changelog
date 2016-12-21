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

package com.io7m.changelog.tests.xom;

import com.io7m.changelog.core.CChangeType;
import com.io7m.changelog.core.CChangelog;
import com.io7m.changelog.core.CItem;
import com.io7m.changelog.core.CRelease;
import com.io7m.changelog.core.CVersionStandard;
import com.io7m.changelog.core.CVersions;
import com.io7m.changelog.xom.CChangelogXHTMLWriter;
import com.io7m.changelog.xom.CChangelogXMLReader;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import nu.xom.ValidityException;
import org.junit.Assume;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CChangelogXHTMLWriterTest
{
  @SuppressWarnings("static-method")
  @Test
  public void testLog()
    throws MalformedURLException,
    ValidityException,
    IOException,
    SAXException,
    ParserConfigurationException,
    ParsingException,
    URISyntaxException,
    ParseException
  {
    Assume.assumeTrue(false);

    final File file = new File("../README-CHANGES.xml");
    final CChangelog cc0 = CChangelogXMLReader.readFromURI(file.toURI());
    final Element e = CChangelogXHTMLWriter.writeElement(cc0);
    final Document d = new Document(e);
    final Serializer s = new Serializer(System.out);
    s.setIndent(2);
    s.write(d);
  }

  @SuppressWarnings("static-method")
  @Test
  public void testPlain()
    throws URISyntaxException,
    ParseException,
    IOException
  {
    final CChangelog.Builder b = CChangelog.builder();
    b.setProject("example");
    b.putTicketSystems("t", new URI("http://example.com/tickets/"));
    final LocalDate date = LocalDate.parse("2014-01-01");
    final List<CItem> items = new ArrayList<>();

    items.add(CItem.of(
      date,
      "Summary 0",
      new ArrayList<>(),
      CChangeType.CHANGE_TYPE_CODE_FIX));

    final ArrayList<String> tickets = new ArrayList<>();
    tickets.add("23");
    tickets.add("48");
    items.add(CItem.of(
      date,
      "Summary 1",
      tickets,
      CChangeType.CHANGE_TYPE_CODE_CHANGE));

    items.add(CItem.of(
      date,
      "Summary 2",
      new ArrayList<>(),
      CChangeType.CHANGE_TYPE_CODE_NEW));

    b.addReleases(CRelease.of(
      date,
      items,
      "t",
      CVersions.parse("1.0.0")));

    final Element e = CChangelogXHTMLWriter.writeElement(b.build());
    final Document d = new Document(e);
    final Serializer s = new Serializer(System.out);
    s.setIndent(2);
    s.write(d);
  }
}
