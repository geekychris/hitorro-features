# Migration Guide - Hitorro Features Extraction

## Overview

This document describes how the `hitorro-features` module was extracted from the main Hitorro monorepo into a standalone Maven project.

## Extraction Date

December 18, 2025

## Source Location

**Original**: `/Users/chris/hitorro/hitorro/hitorro-parent/hitorro-features/`
**Standalone**: `/Users/chris/hitorro/hitorro-features/`

## Changes Made

### 1. Project Structure

Created a standalone Maven project with:
- Independent `pom.xml` (no parent reference)
- Self-contained directory structure
- Updated groupId from `ht` to `com.hitorro`
- Updated version from `2.0` to `3.0.0`

### 2. Dependency Updates

**Removed**:
- Dependency on `hitorro-basedms` (was unnecessary - see analysis below)
- Parent POM inheritance

**Kept**:
- `hitorro-util` (3.0.0) - for core utilities and type system
- `hitorro-base` (3.0.0) - for base field abstractions

**Added**:
- Explicit dependencies for logging (SLF4J, Logback)
- Updated Maven plugins to current versions
- Trove4j for primitive collections

### 3. Why basedms Was Removed

Analysis showed that `hitorro-features` never actually required database persistence:

1. **No Database Persistence**: All feature types (`FeatureValue`, `FeatureSetKey`, `RealmId`) have `isPersisted = false`
2. **No Hibernate Usage**: No Hibernate imports or mappings
3. **File-Based Storage**: Features use file-based index (`.dictionary` and `.postings` files)
4. **Type System Only**: Only used basedms for type registration, which is available in `hitorro-util`

The original dependency on `hitorro-basedms` was likely:
- Legacy from when features might have used database storage
- Or inadvertent inclusion during refactoring
- Or confusion about where the type system lived

### 4. Package Structure

**Unchanged** - All packages remain under `ht.features.*`:
```
ht/
└── features/
    ├── FeatureEngine.java
    ├── FeatureService.java
    ├── FeatureValue.java
    ├── extractor/
    ├── index/
    ├── io/
    └── pipeline/
```

### 5. Build Configuration

Updated to modern Maven practices:
- Java 19 source/target
- Maven Compiler Plugin 3.11.0
- Maven Surefire Plugin 3.2.1
- Added source and javadoc jar generation
- Assembly plugin for fat jar creation

## Using the Standalone Module

### As a Dependency

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>com.hitorro</groupId>
    <artifactId>hitorro-features</artifactId>
    <version>3.0.0</version>
</dependency>
```

### Prerequisites

Ensure you have these modules available:
- `hitorro-util` 3.0.0+
- `hitorro-base` 3.0.0+

These can be:
1. Published to a Maven repository (local or remote)
2. Installed to local Maven repo with `mvn install`
3. Built as part of a multi-module reactor build

## Building

```bash
# Quick build
mvn clean install

# Or use the build script
./build.sh
```

## Testing

The module includes its original test suite. Run with:

```bash
mvn test
```

## Known Issues / TODOs

1. **Dependency Resolution**: You'll need `hitorro-util` and `hitorro-base` available in your Maven repository
2. **Package Rename**: Consider renaming packages from `ht.*` to `com.hitorro.*` for consistency
3. **Documentation**: Add more usage examples and API documentation

## Rollback

To revert to using the module from the monorepo:

1. Delete the standalone project directory
2. Add back to the parent pom's `<modules>` section
3. Update dependent projects to use the old coordinates:
   ```xml
   <groupId>ht</groupId>
   <artifactId>hitorro-features</artifactId>
   <version>2.0</version>
   ```

## Migration Checklist

- [x] Created standalone project structure
- [x] Copied source code
- [x] Updated pom.xml with correct dependencies
- [x] Removed unnecessary basedms dependency
- [x] Added .gitignore
- [x] Added README.md
- [x] Added build.sh script
- [x] Verified no basedms imports in code
- [ ] Build and test the standalone module
- [ ] Publish to Maven repository (if applicable)
- [ ] Update dependent projects to use new coordinates
- [ ] Remove from monorepo (if desired)

## Contact

For questions about this extraction, contact the Hitorro development team.
