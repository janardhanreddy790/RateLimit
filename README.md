# RateLimiterRedisApp

A Spring Boot application implementing efficient rate limiting using Redis without Bucket4j.

## 📌 Description

This application provides **rate limiting for selected API endpoints** using Redis. It’s designed to help prevent abuse, protect backend services, and ensure fair usage.

## 🎯 Why Use This

- Protect sensitive or high-load endpoints from abuse
- Apply limits only where needed
- Works in distributed environments with Redis
- Fully configurable, stateless, scalable

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
| Status Code     | 429                       |
| Response Format | Plain Text (not JSON)     |
| Applied To      | `/hello`, `/data` only    |

## 📁 Project Structure

```
RateLimit/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/com/nagam/example/ratelimiter/
        │   ├── Application.java
        │   ├── controller/HelloController.java
        │   ├── filter/RateLimitFilter.java
        │   └── limiter/RedisRateLimiter.java
        └── resources/
            └── application.yml
```

## 🔍 Rate Limited Endpoints

Only the following endpoints are protected by Redis rate limiting:
- `/hello`
- `/data`

Endpoints like `/status` and `/health` are excluded.

---

## 🚀 Running the Application

### 1. Start Redis

```bash
docker run -p 6379:6379 redis
```

### 2. Start the Spring Boot app

```bash
mvn clean install
mvn spring-boot:run
```

---

## 🧪 How to Test the Rate Limiting

### ✅ Test `/hello` (Rate Limited)

```bash
for i in {1..60}; do
  echo -n "$i: "
  curl -s http://localhost:8080/hello
done
```

**Expected Output:**

```
1: Hello endpoint
...
50: Hello endpoint
51: Rate limit exceeded. Please try again after 10 seconds.
...
60: Rate limit exceeded. Please try again after 10 seconds.
```

### ✅ Test `/data` (Rate Limited)

```bash
for i in {1..60}; do
  echo -n "$i: "
  curl -s http://localhost:8080/data
done
```

Same output pattern as `/hello`.

### ✅ Test `/status` (Not Limited)

```bash
for i in {1..100}; do
  curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8080/status
done
```

**Expected:** All 100 responses → `200`

---

## 🧪 TST / ACC / PRD Environment Support

| Environment | Notes |
|-------------|-------|
| **TST**     | Use low limits to validate behavior |
| **ACC**     | Simulate production load, use Redis service |
| **PRD**     | Use Redis HA (Sentinel or Cluster), adjust limits as needed |

---

## ⚙️ Scalability & Production Use

- Stateless (uses Redis)
- Horizontal scaling supported
- Works in Docker/Kubernetes
- HA-ready with Redis Sentinel/Cluster

---

## 📈 Future Enhancements

- Rate limit per API key or user
- Limit different paths with different values
- Add Prometheus/Grafana metrics
