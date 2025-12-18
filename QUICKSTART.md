# Quick Start Guide - Hitorro Features

## Prerequisites

Before building this module, you need:

1. **Java 19+**
   ```bash
   java -version
   # Should show version 19 or higher
   ```

2. **Maven 3.6+**
   ```bash
   mvn -version
   # Should show Maven 3.6.0 or higher
   ```

3. **Required Dependencies Built**
   - `hitorro-util` (3.0.0)
   - `hitorro-base` (3.0.0)

## Step-by-Step Setup

### Step 1: Build Dependencies

```bash
# Build hitorro-util first
cd /Users/chris/hitorro/hitorro-util
mvn clean install

# Build hitorro-base second
cd /Users/chris/hitorro/hitorro-base
mvn clean install
```

### Step 2: Build Hitorro Features

```bash
cd /Users/chris/hitorro/hitorro-features

# Option A: Use the build script (recommended)
./build.sh

# Option B: Use Maven directly
mvn clean install
```

### Step 3: Verify the Build

```bash
# Check the target directory
ls -lh target/

# You should see:
# - hitorro-features-3.0.0.jar
# - hitorro-features-3.0.0-jar-with-dependencies.jar
# - hitorro-features-3.0.0-sources.jar
# - hitorro-features-3.0.0-javadoc.jar
```

## Using in Your Project

### Add to Your POM

```xml
<dependencies>
    <dependency>
        <groupId>com.hitorro</groupId>
        <artifactId>hitorro-features</artifactId>
        <version>3.0.0</version>
    </dependency>
</dependencies>
```

### Basic Usage Example

```java
import com.hitorro.features.FeatureEngine;
import ht.features.*;
import com.hitorro.jsontypesystem.propreaders.JVSProperties;

// Initialize the feature engine
FeatureEngine engine = new FeatureEngine();
		List<ErrorCode> errors = new ArrayList<>();

try{
		engine.

		init(errors, JVSProperties.getProperties());
		FeatureEngine.

		setEngine(engine);
}catch(
		PropaccessError e){
		// Handle initialization error
		}

// Use the feature engine
// ... your feature extraction code ...
```

## Common Issues

### Issue: Cannot find hitorro-util or hitorro-base

**Solution**: Make sure you've built and installed both dependencies to your local Maven repository:

```bash
cd /Users/chris/hitorro/hitorro-util && mvn install
cd /Users/chris/hitorro/hitorro-base && mvn install
```

### Issue: Java version mismatch

**Solution**: This project requires Java 19+. Check your Java version:

```bash
java -version
export JAVA_HOME=/path/to/java19
```

### Issue: Build fails with "package does not exist"

**Solution**: This usually means a dependency is missing. Verify:

1. All dependencies are in your local Maven repo (`~/.m2/repository/`)
2. POM versions match what you've built
3. Run `mvn dependency:tree` to see what's missing

## What Gets Built

| Artifact | Description |
|----------|-------------|
| `hitorro-features-3.0.0.jar` | Main JAR with compiled classes |
| `hitorro-features-3.0.0-jar-with-dependencies.jar` | Fat JAR with all dependencies |
| `hitorro-features-3.0.0-sources.jar` | Source code JAR |
| `hitorro-features-3.0.0-javadoc.jar` | JavaDoc documentation |

## Development Workflow

```bash
# Make code changes
vim src/main/java/ht/features/...

# Quick compile check (no tests)
mvn compile

# Run tests
mvn test

# Full build
mvn clean install

# Skip tests if needed
mvn install -DskipTests
```

## Testing

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=FeatureEngineTest

# Run tests with debugging
mvn test -X
```

## Cleaning

```bash
# Remove all build artifacts
mvn clean

# Deep clean (including local repo cache)
rm -rf target/
rm -rf ~/.m2/repository/com/hitorro/hitorro-features/
```

## Next Steps

1. ✅ Build the project successfully
2. 📖 Read the [README.md](README.md) for architecture details
3. 📚 Check [MIGRATION.md](MIGRATION.md) to understand the extraction
4. 🚀 Start using features in your application

## Getting Help

- Check the [EXTRACTION_SUMMARY.md](EXTRACTION_SUMMARY.md) for detailed info
- Review build logs: `target/surefire-reports/` for test results
- Run `mvn help:effective-pom` to see the complete POM configuration

---

**Happy coding!** 🎉
