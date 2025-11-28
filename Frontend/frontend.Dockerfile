# Stage 1: Build
FROM node:20-alpine AS build
WORKDIR /app

# Copy only package.json and lock files
COPY ./frontend/portal/package*.json ./

# Install dependencies
RUN npm install --legacy-peer-deps

# Copy the rest of the app
COPY ./frontend/portal .

# Build the Vite/React project
RUN npm run build

# Stage 2: Nginx for production
FROM nginx:alpine

# Copy built app to nginx html folder
COPY --from=build /app/dist /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Run Nginx
CMD ["nginx", "-g", "daemon off;"]
