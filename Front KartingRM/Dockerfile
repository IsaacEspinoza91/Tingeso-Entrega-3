FROM nginx:alpine

# Copiamos la carpeta dist dentro del contendor (carpeta default) en /usr/..
COPY /dist /usr/share/nginx/html
COPY /nginx/conf.d /etc/nginx/conf.d

# Expose port 3000 for the Nginx server
EXPOSE 3000

# Start Nginx when the container runs
CMD ["nginx", "-g", "daemon off;"]