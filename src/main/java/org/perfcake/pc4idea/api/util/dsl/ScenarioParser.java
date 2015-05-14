package org.perfcake.pc4idea.api.util.dsl;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 5/14/15.
 */
public class ScenarioParser {

    ScenarioParser(){

    }

    Scenario parseScenario(String scenario) throws ScenarioParserException {
        Scenario model = new Scenario();

        String[] lines = scenario.split("\n");

        if (!lines[0].contains("scenario")){
            throw new IllegalArgumentException("First line does not contain \"scenario\" string!");
        }

        if (!lines[lines.length - 1].contains("end")){
            throw new IllegalArgumentException("Last line does not contain \"end\" string!");
        }

        String runLine = null;
        String generatorLine = null;
        String senderLine = null;

        /*TODO je povinne aby bol run na druhom(tretom riadku), etc. ???*/
        for (int i = 1;i<lines.length;i++){
            if (lines[i].contains("run ") && (i == 1 || i == 2)){
                runLine = lines[i];
            }
            if (lines[i].contains("generator ") && (i == 2 || i == 3)){
                generatorLine = lines[i];
            }
            if (lines[i].contains("sender ") && (i == 3 || i == 4)){
                senderLine = lines[i];
            }
        }

        model.setGenerator(parseGenerator(runLine, generatorLine));
        model.setSender(parseSender(senderLine));


        /*TODO check if model is valid*/
        return model;
    }

//    scenario "neww"
//    s "s"
//    run 2000.iterations with 200.threads
//    generator "DefaultMessageGenerator" shutdownPeriod "aaca"
//    sender "DummySender" b "b" a "a"
//    reporter "ThroughputStatsReporter" disabled
//    reporter "MemoryUsageReporter" disabled
//    destination "ConsoleDestination" enabled
//    destination "ChartDestination" every d iterations attributes "a" disabled
//    destination "CsvDestination" enabled
//    reporter "WarmUpReporter" disabled
//    message file:"a.txt" send 1.times validate "b"
//    message file:"b.txt" send 1.times headers h:"a",hh:"b"
//    validation disabled
//    validator "ScriptValidator" id "b" engine "a" script "b" scriptFile "c"
//    validator "RegExpValidator" id "a"
//    end

    private Scenario.Generator parseGenerator(String runLine, String generatorLine) {
        if (generatorLine == null) {
            throw new ScenarioParserException("generator line not found!");
        }

        Scenario.Generator model = parseRunLine(runLine);

        generatorLine = removeFirstSpaces(generatorLine);
        String[] parts = generatorLine.split(" ");
        if (!parts[0].equals("generator")) {
            throw new ScenarioParserException("generator line not starting with \"generator\"!");
        }

        model.setClazz(parts[1].substring(1, parts[1].length() - 1));
        if (parts.length > 2){
            model.getProperty().addAll(parseProperties(Arrays.copyOfRange(parts,2,parts.length)));
        }

        return model;
    }

    private Scenario.Sender parseSender(String senderLine){
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
        if (parts.length > 2){
            model.getProperty().addAll(parseProperties(Arrays.copyOfRange(parts, 2, parts.length)));
        }

        return model;
    }

    private Scenario.Generator parseRunLine(String runLine){
        if (runLine == null){
            throw new ScenarioParserException("run line not found!");
        }

        runLine = removeFirstSpaces(runLine);
        String[] parts = runLine.split(" ");
        if (!parts[0].equals("run")) {
            throw new ScenarioParserException("run line not starting with \"run\"!");
        }

        Scenario.Generator model = new Scenario.Generator();

        Scenario.Generator.Run run = new Scenario.Generator.Run();
        Period period = parsePeriod(parts[1]);
        run.setType(period.getType());
        run.setValue(period.getValue());
        model.setRun(run);

        if (!parts[2].equals("with")){
            throw new ScenarioParserException("run line not containing \"with\"!");
        }
        if (!parts[3].contains("threads")){
            throw new ScenarioParserException("run line not containing \"threads\"!");
        }
        model.setThreads(parts[3].substring(0, parts[3].length() - 8));

        return model;
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

    private Period parsePeriod(String periodAsString){
        if (periodAsString == null) {
            throw new ScenarioParserException("unable to parse period!");
        }
        if (periodAsString.contains("%")){
            Period period = new Period();
            period.setType("percentage");
            period.setValue(periodAsString.substring(0, periodAsString.length() - 1));
            return period;
        }
        if (periodAsString.contains(".iterations")){
            Period period = new Period();
            period.setType("iteration");
            period.setValue(periodAsString.substring(0, periodAsString.length() - 11));
            return period;
        }
        if (periodAsString.contains(".ms")){
            Period period = new Period();
            period.setType("time");
            period.setValue(periodAsString.substring(0, periodAsString.length() - 3));
            return period;
        }
        if (periodAsString.contains(".s")){
            Period period = new Period();
            period.setType("time");
            period.setValue(countMilliseconds(periodAsString.substring(0, periodAsString.length() - 2),1000L));
            return period;
        }
        if (periodAsString.contains(".m")){
            Period period = new Period();
            period.setType("time");
            period.setValue(countMilliseconds(periodAsString.substring(0, periodAsString.length() - 2),60000L));
            return period;
        }
        if (periodAsString.contains(".h")){
            Period period = new Period();
            period.setType("time");
            period.setValue(countMilliseconds(periodAsString.substring(0, periodAsString.length() - 2),3600000L));
            return period;
        }
        if (periodAsString.contains(".d")){
            Period period = new Period();
            period.setType("time");
            period.setValue(countMilliseconds(periodAsString.substring(0, periodAsString.length() - 2),86400000L));
            return period;
        }
        throw new ScenarioParserException("unable to parse period - invalid period type!");
    }

    private String countMilliseconds(String value, Long multiplier){
        try {
            Long number = Long.parseLong(value);
            number = number * multiplier;
            return String.valueOf(number);
        } catch (NumberFormatException e){
            return value;
        }
    }

    private String removeFirstSpaces(String stringToTransform){
        while (stringToTransform.startsWith(" ")){
            stringToTransform = stringToTransform.substring(1);
        }
        return stringToTransform;
    }

    private class Period{
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
