/*
 * {{{ header & license
 * GeneralUtil.java
 * Copyright (c) 2004, 2005 Patrick Wright
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * }}}
 */
package com.openhtmltopdf.util;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


/**
 * Description of the Class
 *
 * @author Patrick Wright
 */
public class GeneralUtil {

	public static boolean ciEquals(final String a, final String b)
	{
		if (a == null)
			return b == null;
		
		if (b == null)
			return false;
		
		return a.toLowerCase(Locale.US).equals(b.toLowerCase(Locale.US));
	}
	
	/**
     * Used to format an Object's hashcode into a 0-padded 10 char String, e.g.
     * for 24993066 returns "0024993066"
     */
    public final static java.text.DecimalFormat PADDED_HASH_FORMAT = new java.text.DecimalFormat("0000000000");

    /**
     * Description of the Method
     *
     * @param obj      PARAM
     * @param resource PARAM
     * @return Returns
     */
    public static InputStream openStreamFromClasspath(final Object obj, final String resource) {
        InputStream readStream = null;
        try {
            final ClassLoader loader = obj.getClass().getClassLoader();
            if (loader == null) {
                readStream = ClassLoader.getSystemResourceAsStream(resource);
            } else {
                readStream = loader.getResourceAsStream(resource);
            }
            if (readStream == null) {
                final URL stream = resource.getClass().getResource(resource);
                if (stream != null) readStream = stream.openStream();
            }
        } catch (final Exception ex) {
            //LOGGER.error("Could not open stream from CLASSPATH: " + resource, ex);
        }
        return readStream;
    }

    /**
     * Dumps an exception to the console, only the last 5 lines of the stack
     * trace.
     *
     * @param ex PARAM
     */
    public static void dumpShortException(final Exception ex) {
        String s = ex.getMessage();
        if (s == null || s.trim().equals("null")) {
            s = "{no ex. message}";
        }
        System.out.println(s + ", " + ex.getClass());
        final StackTraceElement[] stes = ex.getStackTrace();
        for (int i = 0; i < stes.length && i < 5; i++) {
            final StackTraceElement ste = stes[i];
            System.out.println("  " + ste.getClassName() + "." + ste.getMethodName() + "(ln " + ste.getLineNumber() + ")");
        }
    }

    /**
     * Returns a String tracking the last n method calls, from oldest to most
     * recent. You can use this as a simple tracing mechanism to find out the
     * calls that got to where you execute the <code>trackBack()</code> call
     * from. Example:
     * <pre>
     * // called from Box.calcBorders(), line 639
     * String tback = GeneralUtil.trackBack(6);
     * System.out.println(tback);
     * </pre> produces
     * <pre>
     * Boxing.layoutChildren(ln 204)
     * BlockBoxing.layoutContent(ln 81)
     * Boxing.layout(ln 72)
     * Boxing.layout(ln 133)
     * Box.totalLeftPadding(ln 306)
     * Box.calcBorders(ln 639)
     * </pre>
     * The <code>trackBack()</code> method itself is always excluded from the dump.
     * Note the output may not be useful if HotSpot has been optimizing the
     * code.
     *
     * @param cnt How far back in the call tree to go; if call tree is smaller, will
     *            be limited to call tree.
     * @return see desc
     */
    public static String trackBack(int cnt) {
        final Exception ex = new Exception();
        StringBuffer sb = new StringBuffer();
        final List<String> list = new ArrayList<String>(cnt);
        final StackTraceElement[] stes = ex.getStackTrace();
        if (cnt >= stes.length) {
            cnt = stes.length - 1;
        }

        // >= 1 to not include this method
        for (int i = cnt; i >= 1; i--) {
            final StackTraceElement ste = stes[i];
            sb.append(GeneralUtil.classNameOnly(ste.getClassName()));
            sb.append(".");
            sb.append(ste.getMethodName());
            sb.append("(ln ").append(ste.getLineNumber()).append(")");
            list.add(sb.toString());
            sb = new StringBuffer();
        }

        final Iterator<String> iter = list.iterator();
        final StringBuffer padding = new StringBuffer("");
        final StringBuffer trackback = new StringBuffer();
        while (iter.hasNext()) {
            final String s = iter.next();
            trackback.append(padding).append(s).append("\n");
            padding.append("   ");
        }
        return trackback.toString();
    }


    /**
     * Given an Object instance, returns just the classname with no package
     *
     * @param o PARAM
     * @return Returns
     */
    public static String classNameOnly(final Object o) {
        String s = "[null object ref]";
        if (o != null) {
            s = classNameOnly(o.getClass().getName());
        }
        return s;
    }

    /**
     * Given a String classname, returns just the classname with no package
     *
     * @param cname PARAM
     * @return Returns
     */
    public static String classNameOnly(final String cname) {
        String s = "[null object ref]";
        if (cname != null) {
            s = cname.substring(cname.lastIndexOf('.') + 1);
        }
        return s;
    }

    /**
     * Description of the Method
     *
     * @param o PARAM
     * @return Returns
     */
    public static String paddedHashCode(final Object o) {
        String s = "0000000000";
        if (o != null) {
            s = PADDED_HASH_FORMAT.format(o.hashCode());
        }
        return s;
    }

    public static boolean isMacOSX() {
        try {
            if (System.getProperty("os.name").toLowerCase().startsWith("mac os x")) {
                return true;
            }
        } catch (final SecurityException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return false;
    }

    public static StringBuffer htmlEscapeSpace(final String uri) {
        final StringBuffer sbURI = new StringBuffer((int) (uri.length() * 1.5));
        char ch;
        for (int i = 0; i < uri.length(); ++i) {
            ch = uri.charAt(i);
            if (ch == ' ') {
                sbURI.append("%20");
            } else if (ch == '\\') {
                sbURI.append('/');
            } else {
                sbURI.append(ch);
            }
        }
        return sbURI;
    }

    /**
     * Reads all content from a given InputStream into a String using the default platform encoding.
     *
     * @param is the InputStream to read from. Must already be open, and will NOT be closed by this function. Failing to
     * close this stream after the call will result in a resource leak.
     *
     * @return String containing contents read from the stream
     * @throws IOException if the stream could not be read
     */
    public static String inputStreamToString(final InputStream is) throws IOException {
        final InputStreamReader isr = new InputStreamReader(is);
        final BufferedReader br = new BufferedReader(isr);
        final StringWriter sw = new StringWriter();
        final char c[] = new char[1024];
        while (true) {
            final int n = br.read(c, 0, c.length);
            if (n < 0) break;
            sw.write(c, 0, n);
        }
        isr.close();
        return sw.toString();
    }

    public static void writeStringToFile(final String content, final String encoding, final String fileName)
            throws IOException {
        final File f = new File(fileName);
        final FileOutputStream fos = new FileOutputStream(f);
        try {
            final OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);
            final BufferedWriter bw = new BufferedWriter(osw);
            final PrintWriter pw = new PrintWriter(bw);
            try {
                pw.print(content);
                pw.flush();
                bw.flush();
            } finally {
                try {
                    pw.close();
                } catch (final Exception e) {
                    // ignore
                }
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            try {
                fos.close();
            } catch (final Exception e) {
                // ignore
            }
        }
        System.out.println("Wrote file: " + f.getAbsolutePath());
    }

    /**
     * Parses an integer from a string using less restrictive rules about which
     * characters we won't accept.  This scavenges the supplied string for any
     * numeric character, while dropping all others.
     *
     * @param s The string to parse
     * @return The number represented by the passed string, or 0 if the string
     *         is null, empty, white-space only, contains only non-numeric
     *         characters, or simply evaluates to 0 after parsing (e.g. "0")
     */
    public static int parseIntRelaxed(final String s) {
        // An edge-case short circuit...
        if (s == null || s.length() == 0 || s.trim().length() == 0) {
            return 0;
        }

        final StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);

            if (Character.isDigit(c)) {
                buffer.append(c);
            } else {
                // If we hit a non-numeric with numbers already in the
                // buffer, we're done.
                if (buffer.length() > 0) {
                    break;
                }
            }
        }

        if (buffer.length() == 0) {
            return 0;
        }

        try {
            return Integer.parseInt(buffer.toString());
        } catch (final NumberFormatException exception) {
            // The only way we get here now is if s > Integer.MAX_VALUE
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Converts any special characters into their corresponding HTML entities , for example &lt; to &amp;lt;.
     * This is done using a character
     * by character test, so you may consider other approaches for large documents. Make sure you declare the
     * entities that might appear in this replacement, e.g. the latin-1 entities
     * This method was taken from a code-samples website, written and hosted by Real Gagnon, at
     * http://www.rgagnon.com/javadetails/java-0306.html.
     *
     * @param s The String which may contain characters to escape.
     * @return The string with the characters as HTML entities.
     */
    public static String escapeHTML(final String s){
    	if (s == null) {
    		return "";
    	}
        final StringBuffer sb = new StringBuffer();
        final int n = s.length();
        for (int i = 0; i < n; i++) {
            final char c = s.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                /*
             case 'ÔøΩ': sb.append("&agrave;");break;
             case 'ÔøΩ': sb.append("&Agrave;");break;
             case 'ÔøΩ': sb.append("&acirc;");break;
             case 'ÔøΩ': sb.append("&Acirc;");break;
             case 'ÔøΩ': sb.append("&auml;");break;
             case 'ÔøΩ': sb.append("&Auml;");break;
             case 'ÔøΩ': sb.append("&aring;");break;
             case 'ÔøΩ': sb.append("&Aring;");break;
             case 'ÔøΩ': sb.append("&aelig;");break;
             case 'ÔøΩ': sb.append("&AElig;");break;
             case 'ÔøΩ': sb.append("&ccedil;");break;
             case 'ÔøΩ': sb.append("&Ccedil;");break;
             case 'ÔøΩ': sb.append("&eacute;");break;
             case 'ÔøΩ': sb.append("&Eacute;");break;
             case 'ÔøΩ': sb.append("&egrave;");break;
             case 'ÔøΩ': sb.append("&Egrave;");break;
             case 'ÔøΩ': sb.append("&ecirc;");break;
             case 'ÔøΩ': sb.append("&Ecirc;");break;
             case 'ÔøΩ': sb.append("&euml;");break;
             case 'ÔøΩ': sb.append("&Euml;");break;
             case 'ÔøΩ': sb.append("&iuml;");break;
             case 'ÔøΩ': sb.append("&Iuml;");break;
             case 'ÔøΩ': sb.append("&ocirc;");break;
             case 'ÔøΩ': sb.append("&Ocirc;");break;
             case 'ÔøΩ': sb.append("&ouml;");break;
             case 'ÔøΩ': sb.append("&Ouml;");break;
             case 'ÔøΩ': sb.append("&oslash;");break;
             case 'ÔøΩ': sb.append("&Oslash;");break;
             case 'ÔøΩ': sb.append("&szlig;");break;
             case 'ÔøΩ': sb.append("&ugrave;");break;
             case 'ÔøΩ': sb.append("&Ugrave;");break;
             case 'ÔøΩ': sb.append("&ucirc;");break;
              case 'ÔøΩ': sb.append("&Ucirc;");break;
             case 'ÔøΩ': sb.append("&uuml;");break;
             case 'ÔøΩ': sb.append("&Uuml;");break;
             case 'ÔøΩ': sb.append("&reg;");break;
             case 'ÔøΩ': sb.append("&copy;");break;
             case 'ÔøΩ': sb.append("&euro;"); break;
                */
                // be carefull with this one (non-breaking whitee space)
                case ' ':
                    sb.append("&nbsp;");
                    break;

                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

}
