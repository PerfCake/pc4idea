package org.perfcake.pc4idea.api.util.dsl;

import org.perfcake.model.Header;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;

import java.util.*;

/**
 * Created by Stanislav Kaleta on 5/14/15.
 */
public class ScenarioParser {

   ScenarioParser() {

   }

   Scenario parseScenario(String scenario) throws ScenarioParserException {
      Scenario model = new Scenario();

      String[] lines = scenario.split("\n");

      if (!lines[0].contains("scenario")) {
         throw new IllegalArgumentException("First line does not contain \"scenario\" string!");
      }

      if (!lines[lines.length - 1].contains("end")) {
         throw new IllegalArgumentException("Last line does not contain \"end\" string!");
      }

      String propertiesLine = null;
      String runLine = null;
      String generatorLine = null;
      String senderLine = null;
      Map<String, List<String>> reporterLines = new LinkedHashMap<>();
      String validationLine = null;
      List<String> validatorLines = new ArrayList<>();
      List<String> messageLines = new ArrayList<>();

        /*TODO je povinne aby bol run na druhom(tretom riadku), etc. ???*/
      for (int i = 1; i < lines.length; i++) {
         if (i == 1 && !lines[i].contains("run ")) {
            propertiesLine = lines[i];
         }
         if (lines[i].contains("run ") && (i == 1 || i == 2)) {
            runLine = lines[i];
         }
         if (lines[i].contains("generator ") && (i == 2 || i == 3)) {
            generatorLine = lines[i];
         }
         if (lines[i].contains("sender ") && (i == 3 || i == 4)) {
            senderLine = lines[i];
         }
         if (lines[i].contains("reporter ")) {
            String reporterLine = lines[i];
            List<String> destinationLines = new ArrayList<>();
            for (int d = i + 1; d < lines.length; d++) {
               if (lines[d].contains("destination ")) {
                  destinationLines.add(lines[d]);
               } else {
                  d = lines.length;
               }
            }
            reporterLines.put(reporterLine, destinationLines);
         }
         if (lines[i].contains("validation ")) {
            validationLine = lines[i];
            for (int v = i + 1; v < lines.length; v++) {
               if (lines[v].contains("validator ")) {
                  validatorLines.add(lines[v]);
               } else {
                  v = lines.length;
               }
            }
         }
         if (lines[i].contains("message")) {
            messageLines.add(lines[i]);
         }

      }

      model.setRun(parseRun(runLine));
      model.setGenerator(parseGenerator(runLine, generatorLine));
      model.setSender(parseSender(senderLine));
      model.setReporting(parseReporting(reporterLines));
      model.setValidation(parseValidation(validationLine, validatorLines));
      model.setMessages(parseMessages(messageLines));
      model.setProperties(parseProperties(propertiesLine));


        /*TODO check if model is valid*/
      return model;
   }

   private Scenario.Run parseRun(String runLine) {
      if (runLine == null) {
         throw new ScenarioParserException("run line not found!");
      }

      runLine = removeFirstSpaces(runLine);
      String[] parts = runLine.split(" ");
      if (!parts[0].equals("run")) {
         throw new ScenarioParserException("run line not starting with \"run\"!");
      }

      Scenario.Run run = new Scenario.Run();
      Period period = parsePeriod(parts[1]);
      run.setType(period.getType());
      run.setValue(period.getValue());

      if (!parts[2].equals("with")) {
         throw new ScenarioParserException("run line not containing \"with\"!");
      }

      return run;
   }

   private Scenario.Generator parseGenerator(String runLine, String generatorLine) {
      if (generatorLine == null) {
         throw new ScenarioParserException("generator line not found!");
      }
      Scenario.Generator model = new Scenario.Generator();
      model.setThreads(parseThreads(runLine));

      generatorLine = removeFirstSpaces(generatorLine);
      String[] parts = generatorLine.split(" ");
      if (!parts[0].equals("generator")) {
         throw new ScenarioParserException("generator line not starting with \"generator\"!");
      }

      model.setClazz(parts[1].substring(1, parts[1].length() - 1));
      if (parts.length > 2) {
         model.getProperty().addAll(parseProperties(Arrays.copyOfRange(parts, 2, parts.length)));
      }

      return model;
   }

   private Scenario.Sender parseSender(String senderLine) {
      if (senderLine == null) {
         throw new ScenarioParserException("sender line not found!");
      }

      senderLine = removeFirstSpaces(senderLine);
      String[] parts = senderLine.split(" ");
      if (!parts[0].equals("sender")) {
         throw new ScenarioParserException("sender line not starting with \"sender\"!");
      }

      Scenario.Sender model = new Scenario.Sender();
      model.setClazz(parts[1].substring(1, parts[1].length() - 1));
      if (parts.length > 2) {
         model.getProperty().addAll(parseProperties(Arrays.copyOfRange(parts, 2, parts.length)));
      }

      return model;
   }

   private Scenario.Reporting parseReporting(Map<String, List<String>> reporterLines) {
      if (reporterLines.keySet().size() == 0) {
         return null;
      }
      Scenario.Reporting reportingModel = new Scenario.Reporting();

      for (String reporterLine : reporterLines.keySet()) {
         List<String> destinationLines = reporterLines.get(reporterLine);
         reporterLine = removeFirstSpaces(reporterLine);
         String[] parts = reporterLine.split(" ");
         if (!parts[0].equals("reporter")) {
            throw new ScenarioParserException("reporter line not starting with \"reporter\"!");
         }
         Scenario.Reporting.Reporter reporterModel = new Scenario.Reporting.Reporter();
         reporterModel.setClazz(parts[1].substring(1, parts[1].length() - 1));
         if (parts.length - 2 > 1) {
            String[] properties = Arrays.copyOfRange(parts, 2, parts.length - 1);
            reporterModel.getProperty().addAll(parseProperties(properties));
            reporterModel.setEnabled(parseEnabled(parts[parts.length - 1]));
         } else {
            reporterModel.setEnabled(parseEnabled(parts[2]));
         }
         for (String destinationLine : destinationLines) {
            reporterModel.getDestination().add(parseDestination(destinationLine));
         }
         reportingModel.getReporter().add(reporterModel);
      }
      return reportingModel;
   }

   private Scenario.Validation parseValidation(String validationLine, List<String> validatorLines) {
      if (validationLine == null) {
         return null;
      }
      validationLine = removeFirstSpaces(validationLine);
      String[] parts = validationLine.split(" ");
      if (!parts[0].equals("validation")) {
         throw new ScenarioParserException("validation line not starting with \"validation\"!");
      }
      Scenario.Validation validationModel = new Scenario.Validation();
      if (parts.length > 2) {
         if (parts[1].equals("fast")) {
            validationModel.setFastForward(true);
         } else {
            throw new ScenarioParserException("string " + parts[1] + " where \"fast\" is expected!");
         }
         validationModel.setEnabled(parseEnabled(parts[2]));
      } else {
         validationModel.setEnabled(parseEnabled(parts[1]));
      }
      for (String validatorLine : validatorLines) {
         validationModel.getValidator().add(parseValidator(validatorLine));
      }
      return validationModel;
   }

   private Scenario.Messages parseMessages(List<String> messageLines) {
      if (messageLines.size() == 0) {
         return null;
      }
      Scenario.Messages messagesModel = new Scenario.Messages();
      for (String messageLine : messageLines) {
         messagesModel.getMessage().add(parseMessage(messageLine));
      }
      return messagesModel;
   }

   private Scenario.Properties parseProperties(String propertiesLine) {
      if (propertiesLine == null) {
         return null;
      }
      propertiesLine = removeFirstSpaces(propertiesLine);
      String[] parts = propertiesLine.split(" ");
      Scenario.Properties propertiesModel = new Scenario.Properties();
      propertiesModel.getProperty().addAll(parseProperties(parts));

      return propertiesModel;
   }

   private String parseThreads(String runLine) {
      if (runLine == null) {
         throw new ScenarioParserException("run line not found!");
      }

      runLine = removeFirstSpaces(runLine);
      String[] parts = runLine.split(" ");
      if (!parts[0].equals("run")) {
         throw new ScenarioParserException("run line not starting with \"run\"!");
      }

      if (!parts[2].equals("with")) {
         throw new ScenarioParserException("run line not containing \"with\"!");
      }
      if (!parts[3].contains("threads")) {
         throw new ScenarioParserException("run line not containing \"threads\"!");
      }
      return parts[3].substring(0, parts[3].length() - 8);
   }

   private Scenario.Reporting.Reporter.Destination parseDestination(String destinationLine) {
      destinationLine = removeFirstSpaces(destinationLine);
      String[] parts = destinationLine.split(" ");
      if (!parts[0].equals("destination")) {
         throw new ScenarioParserException("destination line not starting with \"destination\"!");
      }
      Scenario.Reporting.Reporter.Destination destinationModel = new Scenario.Reporting.Reporter.Destination();
      destinationModel.setClazz(parts[1].substring(1, parts[1].length() - 1));
      if (!parts[2].equals("every")) {
         throw new ScenarioParserException("destination line not containing \"every\"!");
      } else {
         Period p = parsePeriod(parts[3]);
         Scenario.Reporting.Reporter.Destination.Period period = new Scenario.Reporting.Reporter.Destination.Period();
         period.setType(p.getType());
         period.setValue(p.getValue());
         destinationModel.getPeriod().add(period);
      }
      if (parts.length - 4 > 1) {
         String[] properties = Arrays.copyOfRange(parts, 4, parts.length - 1);
         destinationModel.getProperty().addAll(parseProperties(properties));
         destinationModel.setEnabled(parseEnabled(parts[parts.length - 1]));
      } else {
         destinationModel.setEnabled(parseEnabled(parts[4]));
      }
      return destinationModel;
   }

   private Scenario.Validation.Validator parseValidator(String validatorLine) {
      validatorLine = removeFirstSpaces(validatorLine);
      String[] parts = validatorLine.split(" ");
      if (!parts[0].equals("validator")) {
         throw new ScenarioParserException("validator line not starting with \"validator\"!");
      }
      Scenario.Validation.Validator validatorModel = new Scenario.Validation.Validator();
      validatorModel.setClazz(parts[1].substring(1, parts[1].length() - 1));
      if (!parts[2].equals("id")) {
         throw new ScenarioParserException("validator line not containing \"id\"!");
      } else {
         validatorModel.setId(parts[3].substring(1, parts[3].length() - 1));
      }
      if (parts.length > 4) {
         validatorModel.getProperty().addAll(parseProperties(Arrays.copyOfRange(parts, 4, parts.length)));
      }
      return validatorModel;
   }

   private Scenario.Messages.Message parseMessage(String messageLine) {
      messageLine = removeFirstSpaces(messageLine);
      String[] parts = messageLine.split(" ");
      if (!parts[0].equals("message")) {
         throw new ScenarioParserException("message line not starting with \"message\"!");
      }
      Scenario.Messages.Message messageModel = new Scenario.Messages.Message();
      if (parts[1].contains("content")) {
         messageModel.setContent(parts[1].substring(9, parts[1].length() - 1));
      } else {
         if (parts[2].contains("content")) {
            messageModel.setContent(parts[2].substring(9, parts[2].length() - 1));
         }
      }
      if (parts[1].contains("file")) {
         messageModel.setUri(parts[1].substring(6, parts[1].length() - 1));
      } else {
         if (parts[2].contains("file")) {
            messageModel.setUri(parts[2].substring(6, parts[2].length() - 1));
         }
      }
      for (int i = 0; i < parts.length; i++) {
         if (parts[i].equals("send")) {
            String multiplicity = parts[i + 1];
            messageModel.setMultiplicity(multiplicity.substring(0, multiplicity.length() - 6));
         }
      }
      for (int i = 0; i < parts.length; i++) {
         if (parts[i].equals("headers")) {
            String[] headers = parts[i + 1].split(",");
            if ((headers.length % 2) != 0) {
               throw new ScenarioParserException("unable to parse headers");
            }
            for (String headerAsString : headers) {
               String[] headerParts = headerAsString.split(":");
               if (headerParts.length != 2) {
                  throw new ScenarioParserException("unable to parse header");
               }
               Header header = new Header();
               header.setName(headerParts[0]);
               header.setValue(headerParts[1].substring(1, headerParts[1].length() - 1));
               messageModel.getHeader().add(header);
            }
         }
      }
      for (int i = 0; i < parts.length; i++) {
         if (parts[i].equals("validate")) {
            Scenario.Messages.Message.ValidatorRef ref = new Scenario.Messages.Message.ValidatorRef();
            ref.setId(parts[i + 1].substring(1, parts[i + 1].length() - 1));
            messageModel.getValidatorRef().add(ref);
         }
      }
      return messageModel;
   }

   private List<Property> parseProperties(String[] propertyArray) {
      if (propertyArray == null || (propertyArray.length % 2) != 0) {
         throw new ScenarioParserException("unable to parse properties!");
      }

      List<Property> properties = new ArrayList<>();

      for (int i = 0; i < propertyArray.length; i = i + 2) {
         Property property = new Property();
         property.setName(propertyArray[i]);
         property.setValue(propertyArray[i + 1].substring(1, propertyArray[i + 1].length() - 1));
         properties.add(property);
      }

      return properties;
   }

   private boolean parseEnabled(String enabled) {
      if (enabled.equals("enabled")) {
         return true;
      }
      if (enabled.equals("disabled")) {
         return false;
      }
      throw new ScenarioParserException("unable to parse enabled/disabled");
   }

   private Period parsePeriod(String periodAsString) {
      if (periodAsString == null) {
         throw new ScenarioParserException("unable to parse period!");
      }
      if (periodAsString.contains("%")) {
         Period period = new Period();
         period.setType("percentage");
         period.setValue(periodAsString.substring(0, periodAsString.length() - 1));
         return period;
      }
      if (periodAsString.contains(".iterations")) {
         Period period = new Period();
         period.setType("iteration");
         period.setValue(periodAsString.substring(0, periodAsString.length() - 11));
         return period;
      }
      if (periodAsString.contains(".ms")) {
         Period period = new Period();
         period.setType("time");
         period.setValue(periodAsString.substring(0, periodAsString.length() - 3));
         return period;
      }
      if (periodAsString.contains(".s")) {
         Period period = new Period();
         period.setType("time");
         period.setValue(countMilliseconds(periodAsString.substring(0, periodAsString.length() - 2), 1000L));
         return period;
      }
      if (periodAsString.contains(".m")) {
         Period period = new Period();
         period.setType("time");
         period.setValue(countMilliseconds(periodAsString.substring(0, periodAsString.length() - 2), 60000L));
         return period;
      }
      if (periodAsString.contains(".h")) {
         Period period = new Period();
         period.setType("time");
         period.setValue(countMilliseconds(periodAsString.substring(0, periodAsString.length() - 2), 3600000L));
         return period;
      }
      if (periodAsString.contains(".d")) {
         Period period = new Period();
         period.setType("time");
         period.setValue(countMilliseconds(periodAsString.substring(0, periodAsString.length() - 2), 86400000L));
         return period;
      }
      throw new ScenarioParserException("unable to parse period - invalid period type!");
   }

   private String countMilliseconds(String value, Long multiplier) {
      try {
         Long number = Long.parseLong(value);
         number = number * multiplier;
         return String.valueOf(number);
      } catch (NumberFormatException e) {
         return value;
      }
   }

   private String removeFirstSpaces(String stringToTransform) {
      while (stringToTransform.startsWith(" ")) {
         stringToTransform = stringToTransform.substring(1);
      }
      return stringToTransform;
   }

   private class Period {
      private String type;
      private String value;

      public String getType() {
         return type;
      }

      public void setType(String type) {
         this.type = type;
      }

      public String getValue() {
         return value;
      }

      public void setValue(String value) {
         this.value = value;
      }
   }
}
