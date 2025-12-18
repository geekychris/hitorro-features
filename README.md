# Hitorro Features

Feature extraction and processing engine extracted from the Hitorro project.

## Overview

The Hitorro Features module provides a flexible feature extraction and indexing system that:

- **Feature Extraction**: Extract features from documents using configurable extractors
- **Feature Indexing**: Store and retrieve features using an efficient file-based index
- **Feature Processing Pipeline**: Process documents through a chain of feature extractors
- **Realm-based Partitioning**: Support for partitioning features by realm/tenant

## Architecture

### Core Components

- **FeatureEngine**: Central coordinator for feature extraction and management
- **FeatureIndexer**: Manages the on-disk storage of features
- **FeatureExtractor**: Base interface for feature extraction implementations
- **FeatureValue**: Represents a single extracted feature value
- **FeatureSetKey**: Key for identifying feature sets

### Key Features

- **No Database Required**: Uses file-based storage (dictionary + postings files)
- **Serializable Types**: All feature types use HTSerializable for efficient I/O
- **Pluggable Extractors**: Easy to add custom feature extractors
- **Date-based Partitioning**: Built-in support for time-based feature partitioning

## Dependencies

This module depends on:
- `hitorro-util` (3.0.0+) - Core utilities, type system, and serialization
- `hitorro-base` (3.0.0+) - Base field abstractions and document processing

## Building

```bash
mvn clean install
```

## Usage Example

```java
// Initialize the feature engine
FeatureEngine engine = new FeatureEngine();
List<ErrorCode> errors = new ArrayList<>();
engine.init(errors, JVSProperties.getProperties());

// Extract features from a document
ExtractionContext context = new ExtractionContext();
// ... configure extractors ...

Results results = extractorManager.extractFeatures(context, document);

// Features are automatically indexed and can be retrieved later
```

## Configuration

Feature extractors are configured via properties or JVS (JSON Value Source) configuration files.

## History

Extracted from the main Hitorro project (December 2025) as a standalone module.
Originally designed to support document feature extraction for ML pipelines and search ranking.

## License

[Original Hitorro License]

## Notes

- This module was originally part of `hitorro-basedms` but doesn't actually require database persistence
- All feature types have `isPersisted = false` - they use serialization only
- Features are stored in `.dictionary` and `.postings` files on the filesystem
