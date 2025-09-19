# Use the official Node image as the base
FROM node:18.17.1-slim

# Set the working directory inside the container
RUN mkdir klient
WORKDIR /klient

# Copy the package.json and package-lock.json into the container
COPY package*.json ./

# Install the frontend dependencies
RUN npm install

# Copy the rest of the frontend code into the container
COPY . .

# Expose the port the Vue.js app will run on
EXPOSE 3000

# Start the react.js app
CMD ["npm", "start"] 