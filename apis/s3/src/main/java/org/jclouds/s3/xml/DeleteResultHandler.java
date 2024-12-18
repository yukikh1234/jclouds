
package org.jclouds.s3.xml;

import static org.jclouds.util.SaxUtils.equalsOrSuffix;

import org.jclouds.s3.domain.DeleteResult;
import org.jclouds.http.functions.ParseSax;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class DeleteResultHandler extends ParseSax.HandlerForGeneratedRequestWithResult<DeleteResult> {

   public static final String DELETED_TAG = "Deleted";
   public static final String ERROR_TAG = "Error";

   private final ErrorEntryHandler errorEntryHandler = new ErrorEntryHandler();

   private StringBuilder deletedEntryAccumulator = new StringBuilder();

   /**
    * Accumulator for the set of successfully deleted files
    */
   private final ImmutableSet.Builder<String> deleted = ImmutableSet.builder();

   /**
    * Accumulator for the set of errors
    */
   private final ImmutableMap.Builder<String, DeleteResult.Error> errors = ImmutableMap.builder();

   private boolean parsingDeletedEntry = false;
   private boolean parsingErrorEntry = false;

   /**
    * {@inheritDoc}
    */
   @Override
   public void startElement(String uri, String name, String qName, Attributes attributes)
      throws SAXException {
      handleStartElement(qName, uri, name, attributes);
   }

   private void handleStartElement(String qName, String uri, String name, Attributes attributes) throws SAXException {
      if (equalsOrSuffix(qName, DELETED_TAG)) {
         parsingDeletedEntry = true;
         deletedEntryAccumulator.setLength(0);
      } else if (equalsOrSuffix(qName, ERROR_TAG)) {
         parsingErrorEntry = true;
         errorEntryHandler.startElement(uri, name, qName, attributes);
      }
   }

   @Override
   public void characters(char[] chars, int start, int length) throws SAXException {
      if (parsingDeletedEntry) {
         deletedEntryAccumulator.append(chars, start, length);
      } else if (parsingErrorEntry) {
         errorEntryHandler.characters(chars, start, length);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void endElement(String uri, String name, String qName) throws SAXException {
      handleEndElement(qName, uri, name);
   }

   private void handleEndElement(String qName, String uri, String name) throws SAXException {
      if (equalsOrSuffix(qName, DELETED_TAG)) {
         parsingDeletedEntry = false;
         deleted.add(deletedEntryAccumulator.toString().trim());
      } else if (equalsOrSuffix(qName, ERROR_TAG)) {
         parsingErrorEntry = false;
         errors.put(errorEntryHandler.getResult());
      }

      if (parsingErrorEntry) {
         errorEntryHandler.endElement(uri, name, qName);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public DeleteResult getResult() {
      return new DeleteResult(deleted.build(), errors.build());
   }
}
