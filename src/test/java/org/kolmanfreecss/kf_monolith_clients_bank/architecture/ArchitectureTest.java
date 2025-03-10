package org.kolmanfreecss.kf_monolith_clients_bank.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

@AnalyzeClasses(packages = "org.kolmanfreecss.kf_monolith_clients_bank", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("org.kolmanfreecss.kf_monolith_clients_bank");
    }

    @ArchTest
    static final ArchRule layeredArchitectureRule = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controllers").definedBy("..controller..")
            .layer("Services").definedBy("..service..")
            .layer("Repositories").definedBy("..repository..")
            .layer("Models").definedBy("..model..")
            .layer("Config").definedBy("..config..")
            .layer("Exception").definedBy("..exception..")
            .layer("Util").definedBy("..util..")
            .whereLayer("Controllers").mayOnlyBeAccessedByLayers("Services", "Config")
            .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers", "Config")
            .whereLayer("Repositories").mayOnlyBeAccessedByLayers("Services", "Config")
            .whereLayer("Models")
            .mayOnlyBeAccessedByLayers("Controllers", "Services", "Repositories", "Config", "Exception", "Util")
            .whereLayer("Config")
            .mayOnlyBeAccessedByLayers("Controllers", "Services", "Repositories", "Models", "Exception", "Util")
            .whereLayer("Exception")
            .mayOnlyBeAccessedByLayers("Controllers", "Services", "Repositories", "Models", "Config", "Util")
            .whereLayer("Util")
            .mayOnlyBeAccessedByLayers("Controllers", "Services", "Repositories", "Models", "Config", "Exception");

    @ArchTest
    static final ArchRule noCyclicDependenciesRule = SlicesRuleDefinition.slices()
            .matching("org.kolmanfreecss.kf_monolith_clients_bank.(*)..")
            .should().beFreeOfCycles();

    @ArchTest
    static final ArchRule noThrowingGenericExceptionsRule = GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

    @ArchTest
    static final ArchRule noAccessToStandardStreamsRule = GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    static final ArchRule noJavaUtilLoggingRule = GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    static final ArchRule noJodaTimeRule = GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

    @ArchTest
    static final ArchRule controllerNamingConvention = classes()
            .that().resideInAPackage("..controller..")
            .should().haveSimpleNameEndingWith("Controller");

    @ArchTest
    static final ArchRule serviceNamingConvention = classes()
            .that().resideInAPackage("..service..")
            .should().haveSimpleNameEndingWith("Service");

    @ArchTest
    static final ArchRule repositoryNamingConvention = classes()
            .that().resideInAPackage("..repository..")
            .should().haveSimpleNameEndingWith("Repository");

    @ArchTest
    static final ArchRule modelNamingConvention = classes()
            .that().resideInAPackage("..model..")
            .should().haveSimpleNameEndingWith("Entity")
            .orShould().haveSimpleNameEndingWith("DTO")
            .orShould().haveSimpleNameEndingWith("Request")
            .orShould().haveSimpleNameEndingWith("Response");

    @ArchTest
    static final ArchRule exceptionNamingConvention = classes()
            .that().resideInAPackage("..exception..")
            .should().haveSimpleNameEndingWith("Exception");

    @ArchTest
    static final ArchRule noControllerDependencies = noClasses()
            .that().resideInAPackage("..controller..")
            .should().dependOnClassesThat().resideInAPackage("..repository..");

    @ArchTest
    static final ArchRule serviceDependencies = classes()
            .that().resideInAPackage("..service..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                    "..service..",
                    "..repository..",
                    "..model..",
                    "..exception..",
                    "..util..",
                    "java..",
                    "javax..",
                    "org.springframework..");

    @Test
    void testDependencyRules() {
        // Test all rules
        layeredArchitectureRule.check(importedClasses);
        noCyclicDependenciesRule.check(importedClasses);
        noThrowingGenericExceptionsRule.check(importedClasses);
        noAccessToStandardStreamsRule.check(importedClasses);
        noJavaUtilLoggingRule.check(importedClasses);
        noJodaTimeRule.check(importedClasses);
        controllerNamingConvention.check(importedClasses);
        serviceNamingConvention.check(importedClasses);
        repositoryNamingConvention.check(importedClasses);
        modelNamingConvention.check(importedClasses);
        exceptionNamingConvention.check(importedClasses);
        noControllerDependencies.check(importedClasses);
        serviceDependencies.check(importedClasses);
    }
}
