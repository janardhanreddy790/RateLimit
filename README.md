# RateLimiterRedisApp

A Spring Boot application implementing efficient rate limiting using Redis without Bucket4j.

## 📌 Description

This application provides rate limiting based on IP and API endpoint using Redis. It’s designed to help prevent abuse, protect backend services, and fairly distribute traffic.

## 🎯 Why Use This

- Protect backend APIs from being overwhelmed by too many requests
- Ensure fair usage across clients
- Fully configurable and scalable
- Lightweight, no third-party rate-limiting libraries needed

## 🔧 Technology Stack

- Java 17
- Spring Boot 3.2.4
- Spring Web
- Spring Data Redis
- Redis (tested with Docker)

## 🚦 Rate Limiting Logic

| Feature         | Configuration            |
|----------------|---------------------------|
| Redis Key       | `rate-limit:{IP}:{path}` |
| Request Limit   | 50 requests               |
| Time Window     | 10 seconds                |
| Action on Limit | HTTP 429 Too Many Requests |

## 📁 Project Structure

```
RateLimit/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/com/nagam/example/ratelimiter/
        │   ├── Application.java
        │   ├── config/RedisConfig.java
        │   ├── controller/HelloController.java
        │   ├── filter/RateLimitFilter.java
        │   └── limiter/RedisRateLimiter.java
        └── resources/
            └── application.yml
```

## 🚀 Running the Application

### 1. Start Redis locally

```bash
docker run -p 6379:6379 redis
```

### 2. Start Spring Boot App

```bash
mvn clean install
mvn spring-boot:run
```

### 3. Test the API

```bash
curl http://localhost:8080/hello
```

Send 50+ requests within 10 seconds to see rate limiting kick in.

---

## 🧪 Usage in TST, ACC, PRD Environments

| Environment | Notes |
|-------------|-------|
| **TST** (Test) | Use lower limits like 10 requests/10s to validate behavior |
| **ACC** (Acceptance) | Simulate production load with pre-production Redis |
| **PRD** (Production) | Run with proper Redis HA setup (cluster/sentinel) and scaling configurations |

### ✅ Configure via Spring Profiles

Use `application-{profile}.yml` to define different limits or Redis configs per environment.

Example:
```yaml
# application-prod.yml
spring:
  redis:
    host: redis-prd.mycompany.com
    port: 6379
```

---

## ⚙️ Scalability & Deployment

✅ This implementation is:
- Stateless (uses Redis as central storage)
- Horizontally scalable (can run across multiple app pods)
- Works in Docker/Kubernetes/cloud
- Safe for distributed systems

Use Redis Sentinel or Cluster for HA production deployments.

---

## 📈 Future Enhancements (Optional)

- Per-user or token-based limits
- Role-based request policies
- Admin UI for live usage stats
- Prometheus/Grafana metrics
