package pl.put.poznan.sqc;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.put.poznan.sqc.logic.RemoteXmlService;
import pl.put.poznan.sqc.logic.visitor.Visitor;
import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;
import pl.put.poznan.sqc.rest.ScenarioQualityCheckerController;

@ExtendWith(MockitoExtension.class)
class ComponentInteractionTest {

    @Mock
    private RemoteXmlService remoteXmlService;

    @Mock
    private Visitor mockVisitor;

    @InjectMocks
    private ScenarioQualityCheckerController controller;

    @Test
    void testScenarioAcceptsVisitor() {
        Scenario scenario = new Scenario();
        scenario.accept(mockVisitor);
        verify(mockVisitor, times(1)).visit(scenario);
    }

    @Test
    void testScenarioAcceptsVisitorAndPropagatesToSteps() {
        Scenario scenario = new Scenario();
        Step step = new Step();
        scenario.setSteps(Collections.singletonList(step));
        scenario.accept(mockVisitor);
        verify(mockVisitor, times(1)).visit(scenario);
        verify(mockVisitor, times(1)).visit(step);
    }

    @Test
    void testStepAcceptsVisitor() {
        Step step = new Step();
        step.accept(mockVisitor);
        verify(mockVisitor, times(1)).visit(step);
    }

    @Test
    void testStepPropagatesVisitorToSubSteps() {
        Step parent = new Step();
        Step child = new Step();
        parent.setSubSteps(Collections.singletonList(child));
        parent.accept(mockVisitor);
        verify(mockVisitor, times(2)).visit(any(Step.class));
    }

    @Test
    void testGetXml_CallsService() {
        Scenario scenario = new Scenario();
        scenario.setTitle("XML Test");
        when(remoteXmlService.convertScenarioToXml(scenario)).thenReturn("<xml>OK</xml>");
        String result = controller.getScenarioAsXml(scenario);
        verify(remoteXmlService, times(1)).convertScenarioToXml(scenario);
        assertThat(result).isEqualTo("<xml>OK</xml>");
    }

    @Test
    void testGetXml_HandlesServiceError() {
        Scenario scenario = new Scenario();
        when(remoteXmlService.convertScenarioToXml(any(Scenario.class))).thenReturn("<Error>Failed</Error>");
        String result = controller.getScenarioAsXml(scenario);
        verify(remoteXmlService).convertScenarioToXml(scenario);
        assertThat(result).contains("<Error>Failed</Error>");
    }

    @Test
    void testCalculateSteps_IntegrationStructure() {
        Scenario scenario = new Scenario();
        scenario.setTitle("Count Test");
        String response = controller.calculateSteps(scenario);
        assertThat(response).contains("\"count\": 0");
    }
    
    @Test
    void testCalculateKeywords_IntegrationStructure() {
        Scenario scenario = new Scenario();
        scenario.setTitle("Key Test");
        String response = controller.calculateKeywords(scenario);
        assertThat(response).contains("\"count\": 0");
    }

    @Test
    void testVerifyScenario_ReturnsNoErrorsJson() {
        Scenario scenario = new Scenario();
        scenario.setTitle("Valid");
        scenario.setSystemActor("System");
        scenario.setActors(Collections.singletonList("User"));
        Step step = new Step();
        step.setContent("User does something");
        scenario.setSteps(Collections.singletonList(step));
        String response = controller.verifyScenario(scenario);
        assertThat(response).contains("\"errors\":").contains("[]");
    }

    @Test
    void testVerifyScenario_ReturnsErrorsJson() {
        Scenario scenario = new Scenario();
        scenario.setTitle("Invalid");
        scenario.setSystemActor("System");
        scenario.setActors(Collections.singletonList("User"));
        Step step = new Step();
        step.setContent("UnknownActor does something"); // Błąd
        scenario.setSteps(Collections.singletonList(step));
        String response = controller.verifyScenario(scenario);
        assertThat(response).contains("UnknownActor does something");
    }
}