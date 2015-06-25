package org.perfcake.pc4idea.api.util.dsl;

import org.perfcake.model.Header;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;

import java.util.List;

/**
 * User: Stanislav Kaleta
 * Date: 14.5.2015
 */
public class ScenarioBuilder {

   ScenarioBuilder() {

   }

   String buildScenario(Scenario model, String name) {
      StringBuilder sb = new StringBuilder();

      String scenarioDefinition = "scenario \"" + name + "\"\n";
      sb.append(scenarioDefinition);

      if (model.getProperties() != null) {
         sb.append(buildProperties(model.getProperties()));
      }

      sb.append(buildRunAndGenerator(model.getRun(), model.getGenerator()));

      sb.append(buildSender(model.getSender()));

      if (model.getReporting() != null) {
         sb.append(buildReporting(model.getReporting()));
      }

      if (model.getMessages() != null) {
         sb.append(buildMessages(model.getMessages()));
      }

      if (model.getValidation() != null) {
         sb.append(buildValidation(model.getValidation()));
      }

      sb.append("end");

      return sb.toString();
   }

   private String buildRunAndGenerator(Scenario.Run run, Scenario.Generator generator) {
      String runLine = "  run " + buildPeriod(run.getType(), run.getValue()) +
            " with " + generator.getThreads() + ".threads\n";

      String generatorLine = "  generator \"" + generator.getClazz() + "\"" + buildProperties(generator.getProperty());

      return runLine + generatorLine + "\n";
   }

   private String buildSender(Scenario.Sender model) {
      String sender = "  sender \"" + model.getClazz() + "\"" + buildProperties(model.getProperty());

      return sender + "\n";
   }

   private String buildReporting(Scenario.Reporting model) {
      String reporting = "";

      for (Scenario.Reporting.Reporter reporterModel : model.getReporter()) {
         String reporter = "  reporter \"" + reporterModel.getClazz() + "\"" +
               buildProperties(reporterModel.getProperty()) + buildEnabled(reporterModel.isEnabled()) + "\n";

         for (Scenario.Reporting.Reporter.Destination destinationModel : reporterModel.getDestination()) {
            String destination = "    destination \"" + destinationModel.getClazz() + "\"";

            for (Scenario.Reporting.Reporter.Destination.Period periodModel : destinationModel.getPeriod()) {
               String period = " every " + buildPeriod(periodModel.getType(), periodModel.getValue());
               destination = destination + period;
            }

            destination = destination + buildProperties(destinationModel.getProperty()) + buildEnabled(destinationModel.isEnabled());
            reporter = reporter + destination + "\n";
         }

         reporting = reporting + reporter;
      }
        /*TODO reporting properties? + how ""/"enabled"/"disabled" */
      return reporting;
   }

   private String buildMessages(Scenario.Messages model) {
      String messages = "";
        /*TODO content:"c" / file:"f" / "file" / "text" + ako naraz + send/"" + headers name:"value",name:"value" + validate "v" validate "v"*/
      for (Scenario.Messages.Message messageModel : model.getMessage()) {
         String uri = (messageModel.getUri() != null) ? "file:\"" + messageModel.getUri() + "\" " : "";
         String content = (messageModel.getContent() != null) ?
               "content:\"" + messageModel.getContent() + "\" " : "";
         if (uri.equals("") && content.equals("")) {
            content = "content:\"\" ";
         }
         String message = "  message " + content + uri + "send " + messageModel.getMultiplicity() + ".times" +
               buildProperties(messageModel.getProperty());

         for (int i = 0; i < messageModel.getHeader().size(); i++) {
            if (i == 0) {
               message = message + " headers ";
            }

            Header header = messageModel.getHeader().get(i);
            message = message + header.getName() + ":\"" + header.getValue() + "\"";

            if (i < messageModel.getHeader().size() - 1) {
               message = message + ",";
            }
         }

         for (Scenario.Messages.Message.ValidatorRef ref : messageModel.getValidatorRef()) {
            message = message + " validate \"" + ref.getId() + "\"";
         }
         messages = messages + message + "\n";
      }

      return messages;
   }

   private String buildValidation(Scenario.Validation model) {
      String validation = "  validation" +
            ((model.isFastForward()) ? " fast" : "") + buildEnabled(model.isEnabled()) + "\n";

      for (Scenario.Validation.Validator validatorModel : model.getValidator()) {
         String validator = "    validator \"" + validatorModel.getClazz() + "\" id \"" +
               validatorModel.getId() + "\"" + buildProperties(validatorModel.getProperty()) + "\n";
         validation = validation + validator;
      }

      return validation;
   }

   private String buildProperties(Scenario.Properties model) {
      return " " + buildProperties(model.getProperty()) + "\n";
   }

   private String buildPeriod(String type, String value) {
      switch (type) {
         case "time": /*TODO ms,s,m,h,d / .it... or " it..."?*/
            return value + ".ms";
         case "iteration":
            return value + ".iterations";
         case "percentage":
            return value + "%";
         default:
            throw new IllegalArgumentException("Invalid period type!");
      }
   }

   private String buildProperties(List<Property> propertyList) {
      String properties = "";
      for (Property property : propertyList) {
         properties = properties + " " + property.getName() + " \"" + property.getValue() + "\"";
      }
      return properties;
   }

   private String buildEnabled(boolean isEnabled) {
      return (isEnabled) ? " enabled" : " disabled";
   }

}
