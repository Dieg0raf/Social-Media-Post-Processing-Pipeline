# Social Media Post Processing Pipeline

## Project Overview

This project implements a microservice-based pipeline for processing social media posts from Bluesky. The pipeline consists of two microservices:

1. **Moderation Service**: Scans post content for banned words and either fails the post or forwards it to the next service.
2. **Hashtag Service**: Uses an LLM (LLAMA-3) to analyze post content and generate relevant hashtags.

The pipeline processes the top 10 most-liked posts and their replies from the provided dataset.

## Architecture

The system follows a pipeline microservice architecture:

- Client sends requests to the Moderation Service
- Moderation Service validates content and forwards to Hashtag Service if passed
- Hashtag Service generates hashtags using LLAMA-3
- Results flow back to the client

## Technologies Used

- **Java 17**
- **Spring Boot**: Framework for creating microservices
- **LLAMA-3**: Large Language Model for content analysis
- **Ollama**: For running LLM locally
- **Ollama4j**: Java library for interacting with Ollama
- **Gson**: For JSON serialization/deserialization
- **JUnit**: For testing

## Project Structure

```
├── moderation-service/       # Moderation microservice
├── hashtag-service/          # Hashtag generation microservice
├── client-application/       # Client that processes posts and calls the pipeline
├── input.json                # Dataset containing Bluesky posts
└── README.md                 # This file
```

## Setup and Installation

### Prerequisites

- Java 17 or higher
- Maven
- Ollama (for local LLM)

### Installing Ollama and LLAMA-3

1. Download and install Ollama from [https://ollama.com](https://ollama.com)
2. Install LLAMA-3:
   ```
   ollama pull llama3
   ```

### Building the Services

Build each component separately:

```bash
# Build Moderation Service
cd moderation-service
mvn clean install

# Build Hashtag Service
cd ../hashtag-service
mvn clean install

# Build Client Application
cd ../client-application
mvn clean install
```

## Running the Application

1. **Start the Hashtag Service** (on port 30002):

   ```bash
   cd hashtag-service
   mvn spring-boot:run
   ```

2. **Start the Moderation Service** (on port 30001):

   ```bash
   cd moderation-service
   mvn spring-boot:run
   ```

3. **Run the Client Application**:
   ```bash
   cd HW3-MainApp
   mvn exec:java -Dexec.mainClass="com.ecs160.hw3.Main"
   ```

## Testing

### Testing Individual Services

Each service can be tested with curl:

```bash
# Test Moderation Service bash
curl -X POST http://localhost:30001/moderate -H "Content-type: application/json" -d '{"postContent": "Hello, Spring Boot!"}'

# Test Hashtag Service
curl -X POST http://localhost:30002/hash-tag -H "Content-type: application/json" -d '{"postContent": "I love #hiking in the #mountains on weekends. #outdoors #nature"}'
```

### Running Unit Tests

```bash
mvn test
```

## Output Format

The application processes the top 10 most-liked posts and their replies. Output format:

- Posts that fail moderation: `[DELETED]`
- Posts that pass moderation: `post content #generated_hashtag`
- For replies, each line is prefixed with `-->`

Example output:

```
Original post content #travel
--> reply content #vacation
--> [DELETED]
--> another reply #photography
```

## Banned Words List

Posts containing any of these words will fail moderation:

- illegal
- fraud
- scam
- exploit
- dox
- swatting
- hack
- crypto
- bots

## Acknowledgements

- This project was created as part of ECS160 coursework
- Uses the LLAMA-3 model by Meta
