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

/*
 * This annotation configures ArchUnit to analyze all project classes except tests.
 * It's important to exclude tests as they might not follow the same architectural rules.
 *
 * The `importOptions` parameter specifies that test classes should not be included.
 * This ensures that only production code is analyzed.
 *
 * @version 1.0.0
 */
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
            /* Root layers */
            .layer("Application").definedBy("..application..")
            .layer("Domain").definedBy("..domain..")
            .layer("Infrastructure").definedBy("..infrastructure..")
            .layer("Shared").definedBy("..shared..")

            /* Application sublayers */
            .layer("ApplicationServices").definedBy("..application..service..")
            .layer("ApplicationConfig").definedBy("..application..config..")
            .layer("ApplicationUseCases").definedBy("..application..usecase..")

            /* Domain sublayers */
            .layer("DomainEntities").definedBy("..domain..entity..")
            .layer("DomainRepositories").definedBy("..domain..repository..")
            .layer("DomainValueObjects").definedBy("..domain..vo..")

            /* Infrastructure sublayers */
            .layer("InfrastructurePersistence").definedBy("..infrastructure..persistence..")
            .layer("InfrastructureConfig").definedBy("..infrastructure..config..")

            /* Dependencies rules */
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
            .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Application")
            .whereLayer("Shared").mayOnlyBeAccessedByLayers("Application", "Domain", "Infrastructure", "Shared")

            /* Application layer rules */
            .whereLayer("ApplicationServices").mayOnlyBeAccessedByLayers("ApplicationUseCases", "Infrastructure")
            .whereLayer("ApplicationUseCases").mayOnlyBeAccessedByLayers("ApplicationServices")
            .whereLayer("ApplicationConfig")
            .mayOnlyBeAccessedByLayers("ApplicationServices", "ApplicationUseCases", "Infrastructure")

            /* Domain layer rules */
            .whereLayer("DomainEntities")
            .mayOnlyBeAccessedByLayers("DomainRepositories", "ApplicationUseCases", "Domain")
            .whereLayer("DomainRepositories")
            .mayOnlyBeAccessedByLayers("InfrastructurePersistence", "ApplicationUseCases", "Domain")
            .whereLayer("DomainValueObjects")
            .mayOnlyBeAccessedByLayers("Domain", "DomainEntities", "ApplicationUseCases", "DomainRepositories")

            /* Infrastructure layer rules */
            .whereLayer("InfrastructurePersistence").mayOnlyBeAccessedByLayers("InfrastructureConfig")
            .whereLayer("InfrastructureConfig").mayOnlyBeAccessedByLayers("InfrastructurePersistence")
            .allowEmptyShould(true);

    /*
     * Root package structure rule:
     * - Enforces DDD layered architecture
     * - Ensures all classes are properly organized in their respective layers
     * - Excludes the main application class as it lives in the root package
     */
    @ArchTest
    static final ArchRule packageStructureRule = classes()
            .that().resideInAPackage("org.kolmanfreecss.kf_monolith_clients_bank")
            .and()
            .areNotAssignableFrom(org.kolmanfreecss.kf_monolith_clients_bank.KfMonolithClientsBankApplication.class)
            .should().resideInAnyPackage(
                    /* Application layer: Use cases, services, and business logic */
                    "..application..",
                    /* Domain layer: Entities, value objects, domain logic */
                    "..domain..",
                    /* Infrastructure layer: External interfaces, persistence */
                    "..infrastructure..",
                    /* Shared layer: Common utilities and cross-cutting concerns */
                    "..shared..")
            .allowEmptyShould(true);

    /*
     * Application layer rule:
     * - Enforces hexagonal architecture within application layer
     * - Organizes code by subdomains (e.g., client)
     * - Separates concerns into services, use cases, and permissions
     * - Ensures proper modularization of business logic
     */
    @ArchTest
    static final ArchRule applicationSubdomainRule = classes()
            .that().resideInAPackage("..application..")
            .should().resideInAnyPackage(
                    /* Services for client subdomain */
                    "..application..client..services..",
                    /* Use cases implementing business operations */
                    "..application..client..usecases..",
                    /* Permission-specific use cases */
                    "..application..client..usecases..permissions..",
                    /* Permission data models */
                    "..application..client..usecases..permissions..models..",
                    /* Ports for external interactions */
                    "..application..client..usecases..permissions..ports..",
                    /* Permission-specific services */
                    "..application..client..usecases..permissions..services..")
            .allowEmptyShould(true);

    /*
     * Domain layer rule:
     * - Enforces DDD tactical patterns
     * - Organizes domain objects by bounded contexts
     * - Separates core domain logic from shared concepts
     */
    @ArchTest
    static final ArchRule domainSubdomainRule = classes()
            .that().resideInAPackage("..domain..")
            .should().resideInAnyPackage(
                    /* Account bounded context */
                    "..domain..account..",
                    /* Client bounded context */
                    "..domain..client..",
                    /* Shared value objects across domains */
                    "..domain..shared..valueobjects..")
            .allowEmptyShould(true);

    /*
     * Infrastructure layer rule:
     * - Implements hexagonal architecture's adapters pattern
     * - Separates incoming (REST) from outgoing (external systems) adapters
     * - Isolates configuration from core infrastructure code
     */
    @ArchTest
    static final ArchRule infrastructureSubdomainRule = classes()
            .that().resideInAPackage("..infrastructure..")
            .should().resideInAnyPackage(
                    /* Incoming REST adapters */
                    "..infrastructure..adapters..in..rest..",
                    /* Outgoing external system adapters */
                    "..infrastructure..adapters..out..external..",
                    /* Infrastructure configuration */
                    "..infrastructure..config..")
            .allowEmptyShould(true);

    /*
     * Prevents package cycles to maintain clean dependencies.
     * This is crucial for maintaining a clear and maintainable codebase.
     */
    @ArchTest
    static final ArchRule noCyclicDependenciesRule = SlicesRuleDefinition.slices()
            .matching("org.kolmanfreecss.kf_monolith_clients_bank.(*)..")
            .should().beFreeOfCycles();

    /*
     * General coding rules that enforce good practices:
     * - Avoid generic exceptions for better error handling
     * - Use proper logging instead of standard streams
     * - Prefer modern date/time API over legacy ones
     */
    @ArchTest
    static final ArchRule noThrowingGenericExceptionsRule = GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

    @ArchTest
    static final ArchRule noAccessToStandardStreamsRule = GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    static final ArchRule noJavaUtilLoggingRule = GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    static final ArchRule noJodaTimeRule = GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

    /*
     * Naming convention rules:
     * These rules ensure consistent naming across the codebase.
     * Each architectural component must follow its specific naming pattern.
     */
    @ArchTest
    static final ArchRule controllerNamingConvention = classes()
            .that().resideInAPackage("..controller..")
            .should().haveSimpleNameEndingWith("Controller")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule serviceNamingConvention = classes()
            .that().resideInAPackage("..service..")
            .should().haveSimpleNameEndingWith("Service")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repositoryNamingConvention = classes()
            .that().resideInAPackage("..repository..")
            .should().haveSimpleNameEndingWith("Repository")
            .allowEmptyShould(true);

    /*
     * Model naming conventions:
     * - Entities: Domain objects with identity
     * - DTOs: Data transfer objects for API communication
     * - Request/Response: API contract objects
     */
    @ArchTest
    static final ArchRule modelNamingConvention = classes()
            .that().resideInAPackage("..model..")
            .should().haveSimpleNameEndingWith("Entity")
            .orShould().haveSimpleNameEndingWith("DTO")
            .orShould().haveSimpleNameEndingWith("Request")
            .orShould().haveSimpleNameEndingWith("Response")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule exceptionNamingConvention = classes()
            .that().resideInAPackage("..exception..")
            .should().haveSimpleNameEndingWith("Exception")
            .allowEmptyShould(true);

    /*
     * Dependency rules:
     * - Controllers should not access repositories directly (maintain layering)
     * - Ensures clean architecture principles are followed
     */
    @ArchTest
    static final ArchRule noControllerDependencies = noClasses()
            .that().resideInAPackage("..controller..")
            .should().dependOnClassesThat().resideInAPackage("..repository..")
            .allowEmptyShould(true);

    /*
     * Service layer dependency rules:
     * - Defines allowed dependencies for service layer
     * - Prevents unwanted coupling with other layers
     * - Maintains clean architecture boundaries
     */
    @ArchTest
    static final ArchRule serviceDependencies = classes()
            .that().resideInAPackage("..service..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                    /* Can depend on other services */
                    "..service..",
                    /* Can access repositories */
                    "..repository..",
                    /* Can use domain models */
                    "..model..",
                    /* Can use custom exceptions */
                    "..exception..",
                    /* Can use utility classes */
                    "..util..",
                    /* Can use Java standard library */
                    "java..",
                    /* Can use Java EE APIs */
                    "javax..",
                    /* Can use Spring framework */
                    "org.springframework..")
            .allowEmptyShould(true);

    /*
     * Manual test method:
     * Executes all architectural rules to verify the codebase.
     * This provides a way to run all rules in a single test.
     */
    @Test
    void testDependencyRules() {
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
