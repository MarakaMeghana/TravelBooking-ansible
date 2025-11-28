# ===========================
# Stage 1 — Build React App
# ===========================
FROM node:20-alpine AS build

# Set working directory
WORKDIR /app

# Copy only dependency files first (for better layer caching)
COPY package*.json ./

# Install dependencies using npm ci for reproducible builds
RUN npm ci --silent

# Copy the rest of the source code
COPY . .

# Build the production-ready app
RUN npm run build

# ===========================
# Stage 2 — Serve with Nginx
# ===========================
FROM nginx:alpine

# Create directory for Nginx config (if missing)
RUN mkdir -p /etc/nginx/conf.d

# Default Nginx config for React/Vite apps (fallback to index.html)
# This avoids missing file errors and handles client-side routing
RUN echo 'server { \
    listen 80; \
    server_name localhost; \
    root /usr/share/nginx/html; \
    index index.html; \
    location / { \
        try_files $uri /index.html; \
    } \
    error_page 500 502 503 504 /50x.html; \
    location = /50x.html { \
        root /usr/share/nginx/html; \
    } \
}' > /etc/nginx/conf.d/default.conf

# Copy built static files from build stage
COPY --from=build /app/dist /usr/share/nginx/html

# Expose default HTTP port
EXPOSE 80

# Run Nginx in the foreground
CMD ["nginx", "-g", "daemon off;"]