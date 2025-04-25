# Spring Boot Webhook

This is a Spring Boot application that interacts with a remote API, solves the assigned problem, and sends the result to a provided webhook using JWT authentication. 

## Features

- Makes a POST request to the `/generateWebhook` endpoint on application startup.
- Solves the **Mutual Followers** problem (identifies mutual follow pairs).
- Sends the result to a provided webhook with JWT authentication.

## Problem Description

### Endpoint 1: `POST /generateWebhook`

When the application starts, it makes a POST request to the `/generateWebhook` endpoint with the following body:

```json
{
  "name": "John Doe",
  "regNo": "REG12347",
  "email": "john@example.com"
}
