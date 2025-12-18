# Hitorro Features - Unit Tests

This document describes the unit test suite for the hitorro-features project.

## Test Structure

The test suite is organized to mirror the main source code structure:

```
src/test/java/ht/features/
├── AllTestsSuite.java                    # Test suite runner
├── DataTypeTest.java                     # Tests for DataType enum
├── FeatureTypeTest.java                  # Tests for FeatureType enum
├── FeatureCardinalityTest.java           # Tests for FeatureCardinality enum
├── FeatureKeyTypeTest.java               # Tests for FeatureKeyType enum
├── PartitionModelTest.java               # Tests for PartitionModel enum
├── RealmIdTest.java                      # Tests for RealmId class
└── collections/
    └── mapping/
        ├── ScoreFunctionBaseTest.java    # Tests for ScoreFunctionBase
        └── noop/
            ├── NoOpByteScoreMappingFunctionTest.java
            ├── NoOpIntegerScoreMappingFunctionTest.java
            ├── NoOpFloatScoreMappingFunctionTest.java
            └── NoOpStringScoreMappingFunctionTest.java
```

## Running Tests

### Run all tests using Maven
```bash
mvn test
```

### Run a specific test class
```bash
mvn test -Dtest=DataTypeTest
```

### Run the complete test suite
```bash
mvn test -Dtest=AllTestsSuite
```

### Run tests with verbose output
```bash
mvn test -X
```

## Test Coverage

The test suite covers the following components:

### Core Enumerations
- **DataTypeTest**: Tests all data types (byte, short, int, long, float, double, string) including null value handling, type conversions, and enum context
- **FeatureTypeTest**: Tests feature type variants (base, derived, function) and instance creation
- **FeatureCardinalityTest**: Tests cardinality types (single, named, index)
- **FeatureKeyTypeTest**: Tests key types (document, company, eid) and key bag operations
- **PartitionModelTest**: Tests partition models (None, Date, Hash)

### Core Classes
- **RealmIdTest**: Tests RealmId creation, string conversion, serialization metadata, and equality

### Score Mapping Functions
- **ScoreFunctionBaseTest**: Tests base functionality for score mapping functions
- **NoOpByteScoreMappingFunctionTest**: Tests byte to float score conversion including edge cases
- **NoOpIntegerScoreMappingFunctionTest**: Tests integer to float score conversion
- **NoOpFloatScoreMappingFunctionTest**: Tests float score handling including NaN and infinity
- **NoOpStringScoreMappingFunctionTest**: Tests string parsing to float including scientific notation

## Test Categories

### Unit Tests
All tests in this suite are unit tests that test individual components in isolation without requiring external dependencies or complex setup.

### Edge Case Testing
Tests include coverage for:
- Null values and empty strings
- Minimum and maximum values for numeric types
- Invalid input handling
- Type conversion accuracy
- Boundary conditions

### Error Handling Tests
Several tests verify proper exception handling:
- `NumberFormatException` for invalid string conversions
- `NullPointerException` for null inputs where applicable

## Dependencies

The test suite uses:
- **JUnit 4.13.2**: Testing framework
- Standard Java assertions

## Writing New Tests

When adding new tests, follow these conventions:

1. **Naming**: Test classes should be named `{ClassName}Test.java`
2. **Structure**: Use `@Before` for setup, `@After` for cleanup
3. **Assertions**: Use descriptive assertion messages
4. **Documentation**: Add JavaDoc comments for test classes
5. **Coverage**: Test normal cases, edge cases, and error conditions

### Example Test Method
```java
@Test
public void testMethodName() {
    // Arrange
    MyClass instance = new MyClass();
    
    // Act
    int result = instance.someMethod(5);
    
    // Assert
    assertEquals(10, result);
}
```

## Continuous Integration

These tests are designed to run in CI/CD pipelines and should:
- Complete quickly (< 30 seconds total)
- Not require external resources
- Be deterministic (no flaky tests)
- Have clear failure messages

## Future Test Additions

Areas that could benefit from additional testing:
- Feature class initialization and validation
- IO operations for serialization/deserialization
- Collection implementations (sparse and dense)
- Extractor functionality
- Index operations

## Troubleshooting

### Common Issues

**Issue**: Tests fail with `ClassNotFoundException`
- **Solution**: Ensure all dependencies in pom.xml are properly configured and Maven dependencies are updated

**Issue**: Tests fail with import errors
- **Solution**: Verify that hitorro-util and hitorro-base dependencies are available

**Issue**: Compilation errors in tests
- **Solution**: Run `mvn clean compile` before running tests

## Contact

For questions or issues with the test suite, please refer to the main project README or contact the development team.
