package pl.put.poznan.sqc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.put.poznan.sqc.logic.visitor.ActorCheckVisitor;
import pl.put.poznan.sqc.logic.visitor.KeywordCounterVisitor;
import pl.put.poznan.sqc.logic.visitor.LevelFilterVisitor;
import pl.put.poznan.sqc.logic.visitor.StepCounterVisitor;
import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

class VisitorLogicTest {

    private Scenario scenario;

    @BeforeEach
    void setUp() {
        scenario = new Scenario();
        scenario.setTitle("Test Scenario");
        scenario.setSystemActor("System");
        scenario.setActors(Arrays.asList("User", "Admin"));
    }

    @Test
    void testStepCounter_EmptySteps() {
        scenario.setSteps(new ArrayList<>());
        StepCounterVisitor visitor = new StepCounterVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getStepCount()).isEqualTo(0);
    }

    @Test
    void testStepCounter_FlatSteps() {
        scenario.setSteps(createSteps("Step 1", "Step 2", "Step 3"));
        StepCounterVisitor visitor = new StepCounterVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getStepCount()).isEqualTo(3);
    }

    @Test
    void testStepCounter_NestedSteps() {
        Step parent = new Step();
        parent.setContent("Parent");
        parent.setSubSteps(createSteps("Child 1", "Child 2"));
        
        scenario.setSteps(Collections.singletonList(parent));
        
        StepCounterVisitor visitor = new StepCounterVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getStepCount()).isEqualTo(3);
    }

    @Test
    void testStepCounter_DeeplyNested() {
        Step level1 = new Step();
        Step level2 = new Step();
        Step level3 = new Step();
        
        level2.setSubSteps(Collections.singletonList(level3));
        level1.setSubSteps(Collections.singletonList(level2));
        scenario.setSteps(Collections.singletonList(level1));

        StepCounterVisitor visitor = new StepCounterVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getStepCount()).isEqualTo(3);
    }

    @Test
    void testStepCounter_NullSubStepsSafe() {
        Step step = new Step();
        step.setSubSteps(null); // Explicit null
        scenario.setSteps(Collections.singletonList(step));

        StepCounterVisitor visitor = new StepCounterVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getStepCount()).isEqualTo(1);
    }

    @Test
    void testKeywordCounter_NoKeywords() {
        scenario.setSteps(createSteps("User logs in", "System shows error"));
        KeywordCounterVisitor visitor = new KeywordCounterVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getKeywordCount()).isEqualTo(0);
    }

    @Test
    void testKeywordCounter_AllKeywords() {
        scenario.setSteps(createSteps("IF User is valid", "ELSE show error", "FOR EACH item"));
        KeywordCounterVisitor visitor = new KeywordCounterVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getKeywordCount()).isEqualTo(3);
    }

    @Test
    void testKeywordCounter_MixedContent() {
        scenario.setSteps(createSteps("User clicks", "IF error occurs", "System shuts down"));
        KeywordCounterVisitor visitor = new KeywordCounterVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getKeywordCount()).isEqualTo(1);
    }

    @Test
    void testKeywordCounter_CaseSensitivity() {
        // Logika w kodzie używa startsWith("IF"), więc "if" (małe litery) nie powinno być zliczone
        scenario.setSteps(createSteps("if user exists", "IF user exists"));
        KeywordCounterVisitor visitor = new KeywordCounterVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getKeywordCount()).isEqualTo(1);
    }

    @Test
    void testKeywordCounter_InsideNested() {
        Step parent = new Step();
        parent.setContent("IF condition met"); // +1
        parent.setSubSteps(createSteps("User accepts", "ELSE User rejects")); // +0, +1
        
        scenario.setSteps(Collections.singletonList(parent));
        KeywordCounterVisitor visitor = new KeywordCounterVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getKeywordCount()).isEqualTo(2);
    }

    @Test
    void testActorCheck_AllValid() {
        scenario.setSteps(createSteps("User logs in", "System verifies data", "Admin approves"));
        ActorCheckVisitor visitor = new ActorCheckVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getErrors()).isEmpty();
    }

    @Test
    void testActorCheck_InvalidActor() {
        scenario.setSteps(createSteps("Hacker tries access", "User logs out"));
        ActorCheckVisitor visitor = new ActorCheckVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getErrors()).hasSize(1).contains("Hacker tries access");
    }

    @Test
    void testActorCheck_WithKeywords() {
        scenario.setSteps(createSteps("IF User provides password"));
        ActorCheckVisitor visitor = new ActorCheckVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getErrors()).isEmpty();
    }

    @Test
    void testActorCheck_WithColon() {
        scenario.setSteps(createSteps("User: provides data"));
        ActorCheckVisitor visitor = new ActorCheckVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getErrors()).isEmpty();
    }

    @Test
    void testActorCheck_EmptyStep() {
        scenario.setSteps(createSteps(""));
        ActorCheckVisitor visitor = new ActorCheckVisitor();
        scenario.accept(visitor);
        assertThat(visitor.getErrors()).hasSize(1);
    }

    @Test
    void testLevelFilter_Level1() {
        Step parent = new Step();
        parent.setContent("Root");
        parent.setSubSteps(createSteps("Child"));
        scenario.setSteps(Collections.singletonList(parent));

        LevelFilterVisitor visitor = new LevelFilterVisitor();
        visitor.setMaxLevel(1);
        scenario.accept(visitor);

        Scenario result = visitor.getFilteredScenario();
        assertThat(result.getSteps()).hasSize(1);
        assertThat(result.getSteps().get(0).getSubSteps()).isEmpty();
    }

    @Test
    void testLevelFilter_Level2() {
        Step root = new Step();
        Step child = new Step();
        Step grandChild = new Step();
        
        child.setSubSteps(Collections.singletonList(grandChild));
        root.setSubSteps(Collections.singletonList(child));
        scenario.setSteps(Collections.singletonList(root));

        LevelFilterVisitor visitor = new LevelFilterVisitor();
        visitor.setMaxLevel(2);
        scenario.accept(visitor);

        Scenario result = visitor.getFilteredScenario();
        assertThat(result.getSteps().get(0).getSubSteps().get(0).getSubSteps()).isEmpty();
    }

    @Test
    void testLevelFilter_DeepStructureFullCopy() {
        Step root = new Step();
        root.setSubSteps(createSteps("Child"));
        scenario.setSteps(Collections.singletonList(root));

        LevelFilterVisitor visitor = new LevelFilterVisitor();
        visitor.setMaxLevel(5);
        scenario.accept(visitor);

        Scenario result = visitor.getFilteredScenario();
        assertThat(result.getSteps().get(0).getSubSteps()).hasSize(1);
    }

    @Test
    void testLevelFilter_PreservesMetadata() {
        scenario.setSteps(new ArrayList<>());
        LevelFilterVisitor visitor = new LevelFilterVisitor();
        visitor.setMaxLevel(1);
        scenario.accept(visitor);

        Scenario result = visitor.getFilteredScenario();
        assertThat(result.getTitle()).isEqualTo(scenario.getTitle());
        assertThat(result.getSystemActor()).isEqualTo(scenario.getSystemActor());
        assertThat(result.getActors()).isEqualTo(scenario.getActors());
    }

    @Test
    void testLevelFilter_ZeroLevel() {
        scenario.setSteps(createSteps("Step 1"));
        LevelFilterVisitor visitor = new LevelFilterVisitor();
        visitor.setMaxLevel(0);
        scenario.accept(visitor);

        Scenario result = visitor.getFilteredScenario();
        assertThat(result.getSteps()).isNullOrEmpty();
    }

    private List<Step> createSteps(String... contents) {
        List<Step> steps = new ArrayList<>();
        for (String c : contents) {
            Step s = new Step();
            s.setContent(c);
            steps.add(s);
        }
        return steps;
    }
}