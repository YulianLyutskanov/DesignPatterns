package com.fmi.dp.visitor.formateditors;

import com.fmi.dp.checksumcalculator.ChecksumCalculator;
import com.fmi.dp.exceptions.XmlParseException;
import com.fmi.dp.filesystem.FileSystemEntity;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class XmlFormatEditor implements FormatEditor {
    private static final String STARTING_TEXT = "<checksums>";
    private static final String ENDING_TEXT = "</checksums>";

    @Override
    public void writeChecksum(@NotNull OutputStreamWriter streamWriter,
                              @NotNull FileSystemEntity fileInfo, @NotNull String checksum) throws IOException {
        streamWriter.write(String.format(
                """
                \t<item>
                \t\t<mode>binary</mode>
                \t\t<checksum>%s</checksum>
                \t\t<path>%s</path>
                \t\t<size>%d</size>
                \t</item>
                """, checksum, fileInfo.getAbsolutePath().toString().replace("\\", "/"), fileInfo.getSize()));
    }

    @Override
    public void writeChecksumAtBeginning(@NotNull OutputStreamWriter streamWriter,
                                         @NotNull ChecksumCalculator checksumCalculator)
            throws IOException {
        streamWriter.write( STARTING_TEXT + System.lineSeparator());
    }

    @Override
    public void writeChecksumAtEnd(@NotNull OutputStreamWriter streamWriter,
                                   @NotNull ChecksumCalculator checksumCalculator)
            throws IOException {
        streamWriter.write( ENDING_TEXT + System.lineSeparator());
        streamWriter.flush();
    }

    @Override
    public Map<Path, String> getChecksums(@NotNull InputStream inputStream) {
        Map<Path, String> checksumsMap = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();

            NodeList itemList = document.getElementsByTagName("item");

            for (int i = 0; i < itemList.getLength(); i++) {
                Node node = itemList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String checksum = element.getElementsByTagName("checksum").item(0).getTextContent();
                    String filePath = element.getElementsByTagName("path").item(0).getTextContent();
                    checksumsMap.put(Path.of(filePath), checksum);
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new XmlParseException("Invalid xml file while reading: " + inputStream, e);
        }
        return checksumsMap;
    }
}